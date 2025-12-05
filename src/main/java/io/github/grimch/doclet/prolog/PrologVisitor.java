/*
 * This file is part of LogiDoclet.
 *
 * Copyright (c) 2025 The LogiDoclet Authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.grimch.doclet.prolog;

import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;
import javax.lang.model.util.SimpleElementVisitor9;
import javax.lang.model.util.SimpleTypeVisitor9;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.*;
import java.util.stream.Collectors;

import static javax.lang.model.element.ElementKind.*;

/**
 * A visitor that traverses the Java Abstract Syntax Tree (AST) and converts each element
 * into a corresponding Prolog {@link Fact}.
 * <p>
 * This class implements the {@link SimpleElementVisitor9} pattern to handle different kinds of
 * {@link Element}s such as modules, packages, types, methods, and fields. For each element,
 * it constructs a {@code Fact} that represents the element's properties and relationships
 * in a structured, machine-readable format.
 * <p>
 * The visitor collaborates with a {@link DocletPrologWriter} to persist the generated facts
 * to the file system, creating a Prolog representation of the entire codebase.
 *
 * @see SimpleElementVisitor9
 * @see DocletPrologWriter
 * @see Fact
 */
public class PrologVisitor extends SimpleElementVisitor9<Void, Void> {

    private final DocletPrologWriter writer;
    private final DocletEnvironment docEnv;
    private final Reporter reporter;
    private final boolean outputCommentary;
    private final List<Term> indexModuleList = new ArrayList<>();
    private final List<Term> indexPackageList = new ArrayList<>();
    private final Set<String> internalPackageNames = new HashSet<>();
    private List<Fact> packageMembers = null;
    private List<Fact> typeMembers = null;
    private final Types typeUtils;

    /**
     * Constructs a new PrologVisitor.
     *
     * @param writer           The writer responsible for outputting the generated Prolog facts.
     * @param docEnv           The {@link DocletEnvironment} providing access to the source code elements.
     * @param reporter         The {@link Reporter} for logging messages and errors.
     * @param outputCommentary A boolean flag indicating whether to include Javadoc comments in the output.
     */
    public PrologVisitor(DocletPrologWriter writer, DocletEnvironment docEnv, Reporter reporter, boolean outputCommentary) {
        this.writer = writer;
        this.docEnv = docEnv;
        this.reporter = reporter;
        this.outputCommentary = outputCommentary;
        this.typeUtils = docEnv.getTypeUtils();
    }

    /**
     * Visits a {@link ModuleElement} to generate Prolog facts about a Java module.
     * It captures the module's name, directives (requires, exports, uses, provides), and contained packages.
     *
     * @param e The module element to visit.
     * @param p A visitor-specified parameter (unused).
     * @return Always returns {@code null}.
     */
    @Override
    public Void visitModule(ModuleElement e, Void p) {
        String moduleName = e.getQualifiedName().toString();
        List<Term> requires = e.getDirectives().stream()
                .filter(d -> d.getKind() == ModuleElement.DirectiveKind.REQUIRES)
                .map(d -> (ModuleElement.RequiresDirective) d)
                .filter(d -> !d.getDependency().getQualifiedName().contentEquals("java.base") || d.isTransitive() || d.isStatic()) // Only include java.base if explicitly declared with modifiers
                .map(this::toPrologRequires)
                .collect(Collectors.toList());
        List<Term> exports = e.getDirectives().stream()
                .filter(d -> d.getKind() == ModuleElement.DirectiveKind.EXPORTS)
                .map(d -> (ModuleElement.ExportsDirective) d)
                .filter(d -> d.getPackage() != null)
                .map(this::toPrologExports)
                .collect(Collectors.toList());
        List<Term> uses = e.getDirectives().stream()
                .filter(d -> d.getKind() == ModuleElement.DirectiveKind.USES)
                .map(d -> (ModuleElement.UsesDirective) d)
                .map(u -> toPrologType(u.getService().asType()))
                .collect(Collectors.toList());
        List<Term> provides = e.getDirectives().stream()
                .filter(d -> d.getKind() == ModuleElement.DirectiveKind.PROVIDES)
                .map(d -> (ModuleElement.ProvidesDirective) d)
                .map(this::toPrologProvides)
                .collect(Collectors.toList());

        List<Term> allPackages = e.getEnclosedElements().stream()
                .filter(element -> element.getKind() == PACKAGE)
                .map(element -> new Atom(((PackageElement) element).getQualifiedName().toString()))
                .collect(Collectors.toList());

        Atom moduleNameAtom = new Atom(moduleName);
        Fact moduleFact = new Fact("module",
                moduleNameAtom,
                toPrologModifierList(e.getModifiers()),
                new PrologList(requires),
                new PrologList(exports),
                new PrologList(uses),
                new PrologList(provides),
                new PrologList(allPackages) // New argument: all_packages
        );
        writer.writeModuleSummaryFile(moduleName, moduleFact);
        indexModuleList.add(moduleNameAtom);
        return null;
    }

    /**
     * Visits a {@link PackageElement} to generate Prolog facts about a Java package.
     * It collects all the types within the package and writes a summary file.
     *
     * @param e The package element to visit.
     * @param p A visitor-specified parameter (unused).
     * @return Always returns {@code null}.
     */
    @Override
    public Void visitPackage(PackageElement e, Void p) {
        packageMembers = new ArrayList<>();
        String packageName = e.getQualifiedName().toString();
        // Visit enclosed types
        e.getEnclosedElements().forEach(element -> element.accept(this, p));

        // construct and write package
        Atom packageNameAtom = new Atom(packageName);
        Fact packageFact = new Fact(
           "package_declaration",
                packageNameAtom,
                new PrologList(new ArrayList<>(packageMembers))
        );
        writer.writePackageSummaryFile(packageName, packageFact);
        if (! internalPackageNames.contains(packageName)) {
            indexPackageList.add(packageNameAtom);
        }
        return null;
    }

    /**
     * Visits a {@link TypeElement} to generate Prolog facts for a class, interface, enum, record, or annotation.
     * It recursively visits enclosed members (fields, methods, inner types) and then constructs and writes
     * a fact representing the type itself.
     *
     * @param e The type element to visit.
     * @param p A visitor-specified parameter (unused).
     * @return Always returns {@code null}.
     */
    @Override
    public Void visitType(TypeElement e, Void p) {
        typeMembers = new ArrayList<>();

        String qualifiedTypeName = e.getQualifiedName().toString();
        String packageName = docEnv.getElementUtils().getPackageOf(e).getQualifiedName().toString();
        String typeName = e.getSimpleName().toString();

        Fact typeFact = null;

        // Visit members first to collect their facts
        for (Element member : e.getEnclosedElements()) {
            // Only visit members that are not synthetic or compiler-generated
            if (member.getKind().isField() || member instanceof ExecutableElement || member.getKind() == CLASS || member.getKind() == INTERFACE || member.getKind() == ENUM || member.getKind() == RECORD || member.getKind() == ANNOTATION_TYPE) {
                member.accept(this, p);
            }
        }

        switch (e.getKind()) {
            case CLASS:
                typeFact = new Fact("class",
                        new Atom(typeName),
                        new Atom(packageName),
                        toPrologModifierList(e.getModifiers()),
                        toPrologTypeParameterList(e.getTypeParameters()),
                        toPrologExtends(e.getSuperclass()),
                        toPrologImplementsList(e.getInterfaces()),
                        new PrologList(
                            e
                                .getPermittedSubclasses()
                                .stream()
                                .map(t -> new Atom(((TypeElement) typeUtils.asElement(t)).getQualifiedName().toString()))
                                .collect(Collectors.toList()
                            )
                        ),
                        new PrologList(new ArrayList<>(typeMembers)), // Members are collected separately
                        toPrologAnnotationList(e.getAnnotationMirrors()),
                        new Atom(getDocComment(e))
                );
                break;
            case INTERFACE:
                typeFact = new Fact("interface",
                        new Atom(typeName),
                        new Atom(packageName),
                        toPrologModifierList(e.getModifiers()),
                        toPrologTypeParameterList(e.getTypeParameters()),
                        toPrologImplementsList(e.getInterfaces()), // Interfaces extend other interfaces
                        new PrologList(new ArrayList<>(typeMembers)),
                        toPrologAnnotationList(e.getAnnotationMirrors()),
                        new PrologList(
                            e
                                .getPermittedSubclasses()
                                .stream()
                                .map(t -> new Fact("declared_type", new Atom(((TypeElement) typeUtils.asElement(t)).getQualifiedName().toString()), new PrologList(new ArrayList<>())))
                                .collect(Collectors.toList()
                            )
                        ),
                        new Atom(getDocComment(e))
                );
                break;
            case ENUM:
                typeFact = new Fact("enum",
                        new Atom(typeName),
                        new Atom(packageName),
                        toPrologModifierList(e.getModifiers()),
                        toPrologImplementsList(e.getInterfaces()),
                        new PrologList(new ArrayList<>(typeMembers)),
                        toPrologAnnotationList(e.getAnnotationMirrors()),
                        new Atom(getDocComment(e))
                );
                break;
            case ANNOTATION_TYPE:
                typeFact = new Fact("annotation_type",
                        new Atom(typeName),
                        new Atom(packageName),
                        toPrologModifierList(e.getModifiers()),
                        toPrologAnnotationList(e.getAnnotationMirrors()),
                        new Atom(getDocComment(e))
                );
                break;
            case RECORD:
                typeFact = new Fact("record",
                        new Atom(typeName),
                        new Atom(packageName),
                        toPrologModifierList(e.getModifiers()),
                        toPrologTypeParameterList(e.getTypeParameters()),
                        toPrologImplementsList(e.getInterfaces()),
                        new PrologList(e.getEnclosedElements().stream()
                                .filter(el -> el.getKind() == RECORD_COMPONENT)
                                .map(this::toPrologRecordComponent)
                                .collect(Collectors.toList())),
                        new PrologList(new ArrayList<>(typeMembers)),
                        toPrologAnnotationList(e.getAnnotationMirrors()),
                        new Atom(getDocComment(e))
                );
                break;
            default:
                reporter.print(Diagnostic.Kind.WARNING, "Unsupported type kind: " + e.getKind() + " for " + qualifiedTypeName);
                return null;
        }

        if (typeFact != null) {
            packageMembers.add(new Fact("type_declaration", new Atom(typeName), new Atom(e.getKind().toString())));
            writer.writeTypeFile(packageName, typeName, typeFact);

        }

        return null;
    }

    /**
     * Visits an {@link ExecutableElement} to generate Prolog facts for a method or constructor.
     *
     * @param e The executable element to visit.
     * @param p A visitor-specified parameter (unused).
     * @return Always returns {@code null}.
     */
    @Override
    public Void visitExecutable(ExecutableElement e, Void p) {
        Fact memberFact = null;

        switch (e.getKind()) {
            case METHOD:
                memberFact = new Fact("method",
                        new Atom(e.getSimpleName().toString()),
                        toPrologModifierList(e.getModifiers()),
                        toPrologTypeParameterList(e.getTypeParameters()),
                        toPrologType(e.getReturnType()),
                        toPrologParameterList(e.getParameters()),
                        toPrologThrowsList(e.getThrownTypes()),
                        toPrologAnnotationList(e.getAnnotationMirrors()),
                        new Atom(getDocComment(e))
                );
                break;
            case CONSTRUCTOR:
                memberFact = new Fact("constructor",
                        new Atom(e.getSimpleName().toString()),
                        toPrologModifierList(e.getModifiers()),
                        toPrologTypeParameterList(e.getTypeParameters()),
                        toPrologParameterList(e.getParameters()),
                        toPrologThrowsList(e.getThrownTypes()),
                        toPrologAnnotationList(e.getAnnotationMirrors()),
                        new Atom(getDocComment(e))
                );
                break;
            default:
                reporter.print(Diagnostic.Kind.WARNING, "Unsupported executable kind: " + e.getKind() + " for " + e.getSimpleName());
                return null;
        }

        if (memberFact != null) {
            typeMembers.add(memberFact);
        }
        return null;
    }

    /**
     * Visits a {@link VariableElement} to generate Prolog facts for a field.
     * Other kinds of variables (parameters, local variables) are handled in their respective contexts.
     *
     * @param e The variable element to visit.
     * @param p A visitor-specified parameter (unused).
     * @return Always returns {@code null}.
     */
    @Override
    public Void visitVariable(VariableElement e, Void p) {
        Fact memberFact = null;

        switch (e.getKind()) {
            case FIELD:
                memberFact = new Fact("field",
                        new Atom(e.getSimpleName().toString()),
                        toPrologModifierList(e.getModifiers()),
                        toPrologType(e.asType()),
                        toPrologAnnotationList(e.getAnnotationMirrors()),
                        new Atom(getDocComment(e))
                );
                break;
            case ENUM_CONSTANT:
            case PARAMETER:
            case RESOURCE_VARIABLE:
            case LOCAL_VARIABLE:
            case EXCEPTION_PARAMETER:
                // These are handled in other visit methods or ignored.
                return null;
            default:
                reporter.print(Diagnostic.Kind.WARNING, "Unsupported variable kind: " + e.getKind() + " for " + e.getSimpleName());
                return null;
        }

        if (memberFact != null) {
            typeMembers.add(memberFact);
        }
        return null;
    }

    // Helper methods to convert Java model elements to Prolog terms

    /**
     * Converts a {@link ModuleElement.RequiresDirective} to a Prolog {@code requires} fact.
     */
    private Term toPrologRequires(ModuleElement.RequiresDirective r) {
        List<Term> modifiers = r.getDependency().getModifiers().stream()
                .map(m -> new Atom(m.toString().toLowerCase()))
                .collect(Collectors.toList());
        return new Fact("requires",
                new PrologList(modifiers),
                new Atom(r.getDependency().getQualifiedName().toString()),
                toPrologAnnotationList(r.getDependency().getAnnotationMirrors())
        );
    }

    /**
     * Converts a {@link ModuleElement.ExportsDirective} to a Prolog {@code exports} fact.
     */
    private Term toPrologExports(ModuleElement.ExportsDirective e) {
        String packageName = e.getPackage().getQualifiedName().toString();
        List<Term> toModules =
            Optional.ofNullable(e.getTargetModules())
                .orElse(Collections.emptyList())
                .stream()
                .map(m -> {
                    internalPackageNames.add(packageName);
                    return new Atom(m.getQualifiedName().toString());
                })
                .collect(Collectors.toList());

        return new Fact("exports",
            new Atom(e.getPackage().getQualifiedName().toString()),
            new PrologList(toModules),
            toPrologAnnotationList(e.getPackage().getAnnotationMirrors())
        );
    }

    /**
     * Converts a {@link ModuleElement.ProvidesDirective} to a Prolog {@code provides} fact.
     */
    private Term toPrologProvides(ModuleElement.ProvidesDirective p) {
        List<Term> withImplementations = p.getImplementations().stream()
                .map(t -> toPrologType(t.asType()))
                .collect(Collectors.toList());
        return new Fact("provides",
                toPrologType(p.getService().asType()),
                new PrologList(withImplementations),
                toPrologAnnotationList(p.getService().getAnnotationMirrors())
        );
    }

    /**
     * Converts a {@link RecordComponentElement} to a Prolog {@code record_component} fact.
     */
    private Term toPrologRecordComponent(Element e) {
        RecordComponentElement recordComponent = (RecordComponentElement) e;
        return new Fact("record_component",
                new Atom(recordComponent.getSimpleName().toString()),
                toPrologType(recordComponent.asType()),
                toPrologAnnotationList(recordComponent.getAnnotationMirrors())
        );
    }

    /**
     * Converts a set of {@link Modifier}s to a Prolog list of modifier facts.
     */
    private PrologList toPrologModifierList(Set<Modifier> modifiers) {
        return new PrologList(modifiers.stream()
                .map(m -> new Fact("modifier", new Atom(m.toString().toLowerCase())))
                .collect(Collectors.toList()));
    }

    /**
     * Converts a list of {@link TypeParameterElement}s to a Prolog list of type parameter facts.
     */
    private PrologList toPrologTypeParameterList(List<? extends TypeParameterElement> typeParameters) {
        return new PrologList(typeParameters.stream()
                .map(this::toPrologTypeParameter)
                .collect(Collectors.toList()));
    }

    /**
     * Converts a single {@link TypeParameterElement} to a Prolog {@code type_parameter} fact.
     */
    private Term toPrologTypeParameter(TypeParameterElement e) {
        List<Term> bounds = e.getBounds().stream()
                .map(this::toPrologType)
                .collect(Collectors.toList());
        return new Fact("type_parameter",
                new Atom(e.getSimpleName().toString()),
                new PrologList(bounds),
                toPrologAnnotationList(e.getAnnotationMirrors())
        );
    }

    /**
     * Converts a superclass {@link TypeMirror} to a Prolog {@code extends} fact.
     */
    private Term toPrologExtends(TypeMirror superclass) {
        if (superclass == null || superclass.getKind() == TypeKind.NONE || superclass.toString().equals("java.lang.Object")) {
            return new Atom("null");
        }
        return new Fact("extends", new Atom(superclass.getKind().toString().toLowerCase()), toPrologType(superclass));
    }

    /**
     * Converts a list of interface {@link TypeMirror}s to a Prolog list of implements facts.
     */
    private PrologList toPrologImplementsList(List<? extends TypeMirror> interfaces) {
        return new PrologList(interfaces.stream()
                .map(this::toPrologImplements)
                .collect(Collectors.toList()));
    }

    /**
     * Converts a single interface {@link TypeMirror} to a Prolog {@code implements} fact.
     */
    private Term toPrologImplements(TypeMirror iface) {
        return new Fact("implements", new Atom(iface.getKind().toString().toLowerCase()), toPrologType(iface));
    }

    /**
     * Converts a list of method/constructor {@link VariableElement} parameters to a Prolog list of parameter facts.
     */
    private PrologList toPrologParameterList(List<? extends VariableElement> parameters) {
        return new PrologList(parameters.stream()
                .map(this::toPrologParameter)
                .collect(Collectors.toList()));
    }

    /**
     * Converts a single {@link VariableElement} parameter to a Prolog {@code parameter} fact.
     */
    private Term toPrologParameter(VariableElement e) {
        return new Fact("parameter",
                new Atom(e.getSimpleName().toString()),
                toPrologType(e.asType()),
                toPrologModifierList(e.getModifiers()),
                toPrologAnnotationList(e.getAnnotationMirrors())
        );
    }

    /**
     * Converts a list of thrown {@link TypeMirror}s to a Prolog list of throws facts.
     */
    private PrologList toPrologThrowsList(List<? extends TypeMirror> thrownTypes) {
        return new PrologList(thrownTypes.stream()
                .map(this::toPrologThrows)
                .collect(Collectors.toList()));
    }

    /**
     * Converts a single thrown {@link TypeMirror} to a Prolog {@code throws} fact.
     */
    private Term toPrologThrows(TypeMirror t) {
        return new Fact("throws", toPrologType(t));
    }

    /**
     * Retrieves the Javadoc comment for a given element, if the {@code outputCommentary} flag is enabled.
     *
     * @param e The element whose comment is to be retrieved.
     * @return The formatted Javadoc comment as a string, or an empty string if not available or disabled.
     */
    private String getDocComment(Element e) {
        if (outputCommentary) {
            String comment = docEnv.getElementUtils().getDocComment(e);
            return comment != null ? comment.replace("\n", "\\n").replace("\r", "") : "";
        }
        return "";
    }

    /**
     * Converts a list of {@link AnnotationMirror}s to a Prolog list of annotation facts.
     */
    private PrologList toPrologAnnotationList(List<? extends AnnotationMirror> annotations) {
        return new PrologList(annotations.stream()
                .map(this::toPrologAnnotation)
                .collect(Collectors.toList()));
    }

    /**
     * Converts a single {@link AnnotationMirror} to a Prolog {@code annotation} fact.
     */
    private Term toPrologAnnotation(AnnotationMirror annotation) {
        DeclaredType annotationType = annotation.getAnnotationType();
        String annotationName = ((TypeElement) annotationType.asElement()).getQualifiedName().toString();
        List<Term> arguments = annotation.getElementValues().entrySet().stream()
                .map(entry -> new Fact("annotation_argument",
                        new Atom(entry.getKey().getSimpleName().toString()),
                        toPrologAnnotationValue(entry.getValue())))
                .collect(Collectors.toList());
        return new Fact("annotation", new Atom(annotationName), new PrologList(arguments));
    }

    /**
     * Converts an {@link AnnotationValue} to its corresponding Prolog {@link Term}.
     * This uses a nested visitor to handle the different types of annotation values (literals, arrays, enums, etc.).
     */
    private Term toPrologAnnotationValue(AnnotationValue value) {
        return new SimpleAnnotationValueVisitor8<Term, Void>() {
            @Override
            public Term visitBoolean(boolean b, Void aVoid) {
                return new Atom(Boolean.toString(b));
            }

            @Override
            public Term visitByte(byte b, Void aVoid) {
                return new Atom(Byte.toString(b));
            }

            @Override
            public Term visitChar(char c, Void aVoid) {
                return new Atom("'" + c + "'"); // Prolog character atom
            }

            @Override
            public Term visitDouble(double d, Void aVoid) {
                return new Atom(Double.toString(d));
            }

            @Override
            public Term visitFloat(float f, Void aVoid) {
                return new Atom(Float.toString(f));
            }

            @Override
            public Term visitInt(int i, Void aVoid) {
                return new Atom(Integer.toString(i));
            }

            @Override
            public Term visitLong(long l, Void aVoid) {
                return new Atom(Long.toString(l));
            }

            @Override
            public Term visitShort(short s, Void aVoid) {
                return new Atom(Short.toString(s));
            }

            @Override
            public Term visitString(String s, Void aVoid) {
                return new Atom("'" + s + "'"); // Prolog string atom
            }

            @Override
            public Term visitType(TypeMirror t, Void aVoid) {
                return toPrologType(t);
            }

            @Override
            public Term visitEnumConstant(VariableElement c, Void aVoid) {
                return new Atom(c.getEnclosingElement().getSimpleName().toString() + "." + c.getSimpleName().toString());
            }

            @Override
            public Term visitAnnotation(AnnotationMirror a, Void aVoid) {
                return toPrologAnnotation(a);
            }

            @Override
            public Term visitArray(List<? extends AnnotationValue> vals, Void aVoid) {
                return new PrologList(vals.stream()
                        .map(val -> val.accept(this, aVoid))
                        .collect(Collectors.toList()));
            }

@Override
            public Term visitUnknown(AnnotationValue av, Void p) {
                reporter.print(Diagnostic.Kind.WARNING, "Unknown annotation value type: " + av);
                return new Atom("unknown_annotation_value");
            }
        }.visit(value);
    }

    /**
     * Converts a {@link TypeMirror} into a structured Prolog {@link Term}.
     * This uses a nested visitor to handle different kinds of types (declared, primitive, array, etc.).
     */
    private Term toPrologType(TypeMirror typeMirror) {
        return typeMirror.accept(new SimpleTypeVisitor9<Term, Void>() {
            @Override
            public Term visitDeclared(DeclaredType t, Void aVoid) {
                Element element = t.asElement();
                String qualifiedName = (element instanceof TypeElement) ? ((TypeElement) element).getQualifiedName().toString() : element.getSimpleName().toString();
                List<Term> typeArguments = t.getTypeArguments().stream()
                        .map(arg -> arg.accept(this, aVoid))
                        .collect(Collectors.toList());
                return new Fact("declared_type", new Atom(qualifiedName), new PrologList(typeArguments));
            }

            @Override
            public Term visitPrimitive(PrimitiveType t, Void aVoid) {
                return new Fact("type", new Atom("primitive"), new Atom(t.getKind().toString().toLowerCase()));
            }

            @Override
            public Term visitArray(ArrayType t, Void aVoid) {
                return new Fact("type", new Atom("array"), t.getComponentType().accept(this, aVoid));
            }

            @Override
            public Term visitTypeVariable(TypeVariable t, Void aVoid) {
                return new Fact("type", new Atom("type_variable"), new Atom(t.asElement().getSimpleName().toString()));
            }

            @Override
            public Term visitWildcard(WildcardType t, Void aVoid) {
                if (t.getExtendsBound() != null) {
                    return new Fact("type", new Atom("wildcard_extends"), t.getExtendsBound().accept(this, aVoid));
                } else if (t.getSuperBound() != null) {
                    return new Fact("type", new Atom("wildcard_super"), t.getSuperBound().accept(this, aVoid));
                } else {
                    return new Fact("type", new Atom("wildcard_unbounded"), new Atom("null"));
                }
            }

            @Override
            public Term visitNoType(NoType t, Void aVoid) {
                return new Fact("type", new Atom("no_type"), new Atom(t.getKind().toString().toLowerCase()));
            }

            @Override
            protected Term defaultAction(TypeMirror e, Void aVoid) {
                reporter.print(Diagnostic.Kind.WARNING, "Unsupported type mirror kind: " + e.getKind() + " for " + e);
                return new Atom("unknown_type");
            }
        }, null);
    }

    /**
     * Tells if the underlying source has any modules defined or just packages.
     *
     * @return A boolean which tells if the underlying source has any modules defined or just packages.
     */
    public boolean hasModulesDefined() {
        return ! indexModuleList.isEmpty();
    }

    /**
     * Returns the final index fact, which contains a list of all modules  processed.
     *
     * @return A {@link Fact} representing the top-level index for modules.
     */
    public Fact getModuleIndex() {
        return new Fact("module_index", new PrologList(indexModuleList));
    }

    /**
     * Returns the final index fact, which contains a list of all packages processed.
     *
     * @return A {@link Fact} representing the top-level index for packages.
     */
    public Fact getPackageIndex() {
        return new Fact("package_index", new PrologList(indexPackageList));
    }

}

