package io.github.grimch.doclet.prolog;

import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;
import javax.lang.model.util.SimpleElementVisitor9;
import javax.lang.model.util.SimpleTypeVisitor9;
import javax.lang.model.util.Types;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import static javax.lang.model.element.ElementKind.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A visitor that traverses Java elements and converts them into Prolog facts.
 * It uses a DocletPrologWriter to persist the generated facts.
 */
public class PrologVisitor extends SimpleElementVisitor9<Void, Void> {

    private final DocletPrologWriter writer;
    private final DocletEnvironment docEnv;
    private final Reporter reporter;
    private final boolean outputCommentary; // New field
    private List<Term> indexModuleList = new ArrayList<>();
    private List<Fact> packageMembers = null;
    private List<Fact> typeMembers = null;
    private Types typeUtils; ;

    public PrologVisitor(DocletPrologWriter writer, DocletEnvironment docEnv, Reporter reporter, boolean outputCommentary) {
        this.writer = writer;
        this.docEnv = docEnv;
        this.reporter = reporter;
        this.outputCommentary = outputCommentary; // Initialize new field
        typeUtils = docEnv.getTypeUtils();
    }

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

        Atom moduleNameAtom = new Atom(moduleName);
        Fact moduleFact = new Fact("module",
                moduleNameAtom,
                toPrologModifierList(e.getModifiers()),
                new PrologList(requires),
                new PrologList(exports),
                new PrologList(uses),
                new PrologList(provides)
        );
        writer.writeModuleSummaryFile(moduleName, moduleFact);
        indexModuleList.add(moduleNameAtom);

        // Visit enclosed packages
        e.getEnclosedElements().forEach(element -> element.accept(this, p));
        return null;
    }

    @Override
    public Void visitPackage(PackageElement e, Void p) {
        packageMembers = new ArrayList<>();
        String packageName = e.getQualifiedName().toString();
        // Visit enclosed types
        e.getEnclosedElements().forEach(element -> element.accept(this, p));

        // construct and write package
        Fact packageFact = new Fact(
           "package_declaration",
            new Atom(packageName), new PrologList(new ArrayList<>(packageMembers))
        );
        writer.writePackageSummaryFile(packageName, packageFact);
        return null;
    }

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
            if (member.getKind().isField() || member.getKind().isExecutable() || member.getKind() == CLASS || member.getKind() == INTERFACE || member.getKind() == ENUM || member.getKind() == RECORD || member.getKind() == ANNOTATION_TYPE) {
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
                reporter.print(javax.tools.Diagnostic.Kind.WARNING, "Unsupported type kind: " + e.getKind() + " for " + qualifiedTypeName);
                return null;
        }

        if (typeFact != null) {
            packageMembers.add(new Fact("type_declaration", new Atom(typeName), new Atom(e.getKind().toString())));
            writer.writeTypeFile(packageName, typeName, typeFact);

        }

        return null;
    }

    @Override
    public Void visitExecutable(ExecutableElement e, Void p) {
        String qualifiedTypeName = ((TypeElement) e.getEnclosingElement()).getQualifiedName().toString();
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
                reporter.print(javax.tools.Diagnostic.Kind.WARNING, "Unsupported executable kind: " + e.getKind() + " for " + e.getSimpleName());
                return null;
        }

        if (memberFact != null) {
            typeMembers.add(memberFact);
        }
        return null;
    }

    @Override
    public Void visitVariable(VariableElement e, Void p) {
        String qualifiedTypeName = ((TypeElement) e.getEnclosingElement()).getQualifiedName().toString();
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
                // Handled within visitType for enum constants
                return null;
            case PARAMETER:
                // Handled within visitExecutable for parameters
                return null;
            case RESOURCE_VARIABLE:
            case LOCAL_VARIABLE:
            case EXCEPTION_PARAMETER:
                // These are not typically part of the API documentation
                return null;
            default:
                reporter.print(javax.tools.Diagnostic.Kind.WARNING, "Unsupported variable kind: " + e.getKind() + " for " + e.getSimpleName());
                return null;
        }

        if (memberFact != null) {
            typeMembers.add(memberFact);
        }
        return null;
    }

    // Helper methods to convert Java model elements to Prolog terms

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

    private Term toPrologExports(ModuleElement.ExportsDirective e) {
        List<Term> toModules =
            Optional.ofNullable(e.getTargetModules())
                .orElse(Collections.emptyList())
                .stream()
                .map(m -> new Atom(m.getQualifiedName().toString()))
                .collect(Collectors.toList());

        return new Fact("exports",
            new Atom(e.getPackage().getQualifiedName().toString()),
            new PrologList(toModules),
            toPrologAnnotationList(e.getPackage().getAnnotationMirrors())
        );
    }

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

    private Term toPrologEnumConstant(Element e) {
        VariableElement enumConstant = (VariableElement) e;
        // Extracting actual constructor arguments for enum constants is complex with the current Element API.
        // It would typically require using the DocTree API to inspect the EnumConstantTree.
        // For now, we provide an empty list to match the metadata arity.
        List<Term> constructorArguments = new ArrayList<>(); // Placeholder for actual arguments
        return new Fact("enum_constant",
                new Atom(enumConstant.getSimpleName().toString()),
                toPrologAnnotationList(enumConstant.getAnnotationMirrors()),
                new PrologList(constructorArguments)
        );
    }

    private Term toPrologAnnotationMember(ExecutableElement e) {
        // Annotation members are essentially methods with a default value
        Term defaultValue = (e.getDefaultValue() != null) ? toPrologAnnotationValue(e.getDefaultValue()) : new Atom("null");
        return new Fact("annotation_member",
                new Atom(e.getSimpleName().toString()),
                toPrologType(e.getReturnType()),
                defaultValue,
                toPrologAnnotationList(e.getAnnotationMirrors()),
                new Atom("") // text_block - not directly available from ExecutableElement
        );
    }

    private Term toPrologRecordComponent(Element e) {
        RecordComponentElement recordComponent = (RecordComponentElement) e;
        return new Fact("record_component",
                new Atom(recordComponent.getSimpleName().toString()),
                toPrologType(recordComponent.asType()),
                toPrologAnnotationList(recordComponent.getAnnotationMirrors())
        );
    }

    private PrologList toPrologModifierList(Set<Modifier> modifiers) {
        return new PrologList(modifiers.stream()
                .map(m -> new Fact("modifier", new Atom(m.toString().toLowerCase())))
                .collect(Collectors.toList()));
    }

    private PrologList toPrologTypeParameterList(List<? extends TypeParameterElement> typeParameters) {
        return new PrologList(typeParameters.stream()
                .map(this::toPrologTypeParameter)
                .collect(Collectors.toList()));
    }

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

    private Term toPrologExtends(TypeMirror superclass) {
        if (superclass == null || superclass.getKind() == TypeKind.NONE || superclass.toString().equals("java.lang.Object")) {
            return new Atom("null");
        }
        return new Fact("extends", new Atom(superclass.getKind().toString().toLowerCase()), toPrologType(superclass));
    }

    private PrologList toPrologImplementsList(List<? extends TypeMirror> interfaces) {
        return new PrologList(interfaces.stream()
                .map(this::toPrologImplements)
                .collect(Collectors.toList()));
    }

    private Term toPrologImplements(TypeMirror iface) {
        return new Fact("implements", new Atom(iface.getKind().toString().toLowerCase()), toPrologType(iface));
    }

    private PrologList toPrologParameterList(List<? extends VariableElement> parameters) {
        return new PrologList(parameters.stream()
                .map(this::toPrologParameter)
                .collect(Collectors.toList()));
    }

    private Term toPrologParameter(VariableElement e) {
        return new Fact("parameter",
                new Atom(e.getSimpleName().toString()),
                toPrologType(e.asType()),
                toPrologModifierList(e.getModifiers()),
                toPrologAnnotationList(e.getAnnotationMirrors())
        );
    }

    private PrologList toPrologThrowsList(List<? extends TypeMirror> thrownTypes) {
        return new PrologList(thrownTypes.stream()
                .map(this::toPrologThrows)
                .collect(Collectors.toList()));
    }

    private Term toPrologThrows(TypeMirror t) {
        return new Fact("throws", toPrologType(t));
    }

    private String getDocComment(Element e) {
        if (outputCommentary) {
            String comment = docEnv.getElementUtils().getDocComment(e);
            return comment != null ? comment.replace("\n", "\\n").replace("\r", "") : "";
        }
        return "";
    }

    private PrologList toPrologAnnotationList(List<? extends AnnotationMirror> annotations) {
        return new PrologList(annotations.stream()
                .map(this::toPrologAnnotation)
                .collect(Collectors.toList()));
    }

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
                reporter.print(javax.tools.Diagnostic.Kind.WARNING, "Unknown annotation value type: " + av);
                return new Atom("unknown_annotation_value");
            }
        }.visit(value);
    }

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
                reporter.print(javax.tools.Diagnostic.Kind.WARNING, "Unsupported type mirror kind: " + e.getKind() + " for " + e);
                return new Atom("unknown_type");
            }
        }, null);
    }

    public Fact getIndex() {
        return new Fact("index", new PrologList(indexModuleList));
    }
}
