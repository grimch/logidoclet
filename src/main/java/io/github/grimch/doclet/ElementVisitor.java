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
package io.github.grimch.doclet;

import com.sun.source.util.DocTrees;
import io.github.grimch.doclet.element.*;
import static io.github.grimch.doclet.util.TypeUtils.*;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.*;
import javax.tools.Diagnostic;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javax.lang.model.element.ElementKind.*;

public class ElementVisitor extends SimpleElementVisitor14<Object, Void> {

    private record Pair<L, R>(L left, R right) {}

    private final DocWriter docWriter;
    private final DocletEnvironment docEnv;
    private final DocTrees treeUtils;
    private final Reporter reporter;

    private final List<String> packageList = new ArrayList<>();
    private final Set<String> internalPackageNames = new HashSet<>();


    public ElementVisitor(DocletEnvironment docEnv, Reporter reporter,DocWriter docWriter) {
        this.docEnv = docEnv;
        this.treeUtils = docEnv.getDocTrees();
        this.reporter = reporter;
        this.docWriter = docWriter;
    }

    public static <T extends Collection<?>> T nullIfEmpty(T collection) {
        return (collection == null || collection.isEmpty()) ? null : collection;
    }

    private boolean isPublicApi(Element e) {
        var mods = e.getModifiers();
        return mods.contains(Modifier.PUBLIC) || mods.contains(Modifier.PROTECTED);
    }

    private  <T> List<T> convertList(List<?> list, Class<T> targetType) {
        if (list == null || list.isEmpty()) {
            return null;
        } else {
            return
                list
                    .stream()
                    .filter(targetType::isInstance)
                    .map(targetType::cast)
                    .toList();
        }
    }

    private String getElementComment(Element e) {
        return treeUtils.getDocCommentTree(e) != null
            ? treeUtils.getDocCommentTree(e).toString()
            : null;
    }

    private <L, R> Map<L, List<R>> acceptElementChilds(Element element) {
        return element.getEnclosedElements()
            .stream()
            .filter(this::isPublicApi)
            .map(child -> child.accept(this, null))
            .flatMap(obj -> {
                if (obj instanceof Pair<?, ?> pair) {
                    @SuppressWarnings("unchecked")
                    Pair<L, R> castedPair = (Pair<L, R>) pair;
                    return Stream.of(castedPair);
                }
                return Stream.empty();
            })
            .collect(Collectors.groupingBy(
                Pair::left,
                Collectors.mapping(
                    Pair::right,
                    Collectors.toList()
                )
            ));
    }

    private List<String> getTypeNames(List<? extends TypeMirror> typeMirrors) {
        return
            nullIfEmpty(
                typeMirrors
                .stream()
                .map(typeMirror -> getFullTypeUsageFQN(typeMirror))
                .toList()
            );
    }

    private List<String> getAnnotations(List<? extends AnnotationMirror> annotationMirrors) {
        return
            nullIfEmpty(
                annotationMirrors
                    .stream()
                    .map(Object::toString)
                    .toList()
            );
    }

    private List<String> getModfiers(Set<Modifier> modifiers) {
        return
            nullIfEmpty(
                modifiers
                    .stream()
                    .map(Object::toString)
                    .toList()
            );
    }

    private VariableDoc getVariable(VariableElement element) {
        return new VariableDoc(
            element.getSimpleName().toString(),
            getModfiers(element.getModifiers()),
            getFullTypeUsageFQN(element.asType()),
            getAnnotations(element.getAnnotationMirrors())
        );
    }

    private List<VariableDoc> getParameters(ExecutableElement element) {
        return
            nullIfEmpty(
                element
                    .getParameters()
                    .stream()
                    .map(parameter ->
                        new VariableDoc(
                            parameter.getSimpleName().toString(),
                            getModfiers(parameter.getModifiers()),
                            getFullTypeUsageFQN(parameter.asType()),
                            getAnnotations(parameter.getAnnotationMirrors())
                        )
                    )
                    .toList()
            );

    }

    public void startVisit() {
        docEnv
            .getIncludedElements()
            .stream()
            .forEach(element -> element.accept(this, null));

        docWriter.writePackageListDoc(new PackageListDoc(packageList));
    }

    @Override
    public Void visitModule(ModuleElement moduleElement, Void p) {
        moduleElement
            .getDirectives()
            .stream()
            .filter(extendsDirective -> extendsDirective.getKind() == ModuleElement.DirectiveKind.EXPORTS)
            .map(extendsDirective -> (ModuleElement.ExportsDirective) extendsDirective)
            .filter(extendsDirective -> extendsDirective.getPackage() != null && extendsDirective.getTargetModules() != null)
            .map(exportsDirective -> exportsDirective.getPackage().getQualifiedName().toString())
            .forEach(internalPackageNames::add);
        return null;
    }

    @Override
    public Void visitPackage(PackageElement e, Void p) {
        String packageName = e.getQualifiedName().toString();
        if (!internalPackageNames.contains(packageName)) {
            Map<ElementKind, List<Object>> groupedElementKinds = acceptElementChilds(e);
            docWriter.writePackageDoc(
                new PackageDoc(
                    packageName,
                    convertList(groupedElementKinds.get(CLASS), ClassDoc.Header.class),
                    convertList(groupedElementKinds.get(INTERFACE), InterfaceDoc.Header.class),
                    convertList(groupedElementKinds.get(ENUM), EnumDoc.Header.class),
                    convertList(groupedElementKinds.get(RECORD), RecordDoc.Header.class)
                )
            );
            packageList.add(packageName);
        }
        return null;
    }

    @Override
    public Pair<ElementKind, ? extends Object> visitType(TypeElement e, Void p) {
        String qualifiedTypeName = e.getQualifiedName().toString();
        String packageName = docEnv.getElementUtils().getPackageOf(e).getQualifiedName().toString();
        String typeName = qualifiedTypeName.substring(packageName.length() + 1);

        Map<ElementKind, List<Object>> groupedElementKinds = acceptElementChilds(e);

        switch (e.getKind()) {
            case CLASS -> {
                ClassDoc.Header header =
                    new ClassDoc.Header(
                        typeName,
                        getTypeParametersListFQN(e.getTypeParameters()),
                        getModfiers(e.getModifiers()),
                        getFullTypeUsageFQN(e.getSuperclass()),
                        getTypeNames(e.getInterfaces())
                    );
                docWriter.writeTypeDoc(
                    packageName,
                    new ClassDoc(
                        header,
                        convertList(groupedElementKinds.get(CLASS), ClassDoc.Header.class),
                        convertList(groupedElementKinds.get(INTERFACE), InterfaceDoc.Header.class),
                        convertList(groupedElementKinds.get(ENUM), EnumDoc.Header.class),
                        convertList(groupedElementKinds.get(RECORD), RecordDoc.Header.class),
                        convertList(groupedElementKinds.get(FIELD), VariableDoc.class),
                        convertList(groupedElementKinds.get(CONSTRUCTOR), ConstructorDoc.class),
                        convertList(groupedElementKinds.get(METHOD), MethodDoc.class),
                        getAnnotations(e.getAnnotationMirrors()),
                        getElementComment(e)
                    )
                );
                return new Pair<ElementKind, ClassDoc.Header>(e.getKind(), header);
            }
            case INTERFACE -> {
                InterfaceDoc.Header header =
                    new InterfaceDoc.Header(
                        typeName,
                        getTypeParametersListFQN(e.getTypeParameters()),
                        getModfiers(e.getModifiers()),
                        getFullTypeUsageFQN(e.getSuperclass()),
                        getTypeNames(e.getInterfaces()),
                        getTypeNames(e.getPermittedSubclasses())
                    );

                docWriter.writeTypeDoc(
                    packageName,
                    new InterfaceDoc(
                        header,
                        convertList(groupedElementKinds.get(CLASS), ClassDoc.Header.class),
                        convertList(groupedElementKinds.get(INTERFACE), InterfaceDoc.Header.class),
                        convertList(groupedElementKinds.get(ENUM), EnumDoc.Header.class),
                        convertList(groupedElementKinds.get(RECORD), RecordDoc.Header.class),
                        convertList(groupedElementKinds.get(FIELD), VariableDoc.class),
                        convertList(groupedElementKinds.get(METHOD), MethodDoc.class),
                        getAnnotations(e.getAnnotationMirrors()),
                        getElementComment(e)
                    )
                );
                return new Pair<ElementKind, InterfaceDoc.Header>(e.getKind(), header);

            }
            case ENUM -> {
                EnumDoc.Header header =
                    new EnumDoc.Header(
                        typeName,
                        getModfiers(e.getModifiers()),
                        getTypeNames(e.getInterfaces())
                    );

                docWriter.writeTypeDoc(
                    packageName,
                    new EnumDoc(
                        header,
                        convertList(groupedElementKinds.get(CLASS), ClassDoc.Header.class),
                        convertList(groupedElementKinds.get(INTERFACE), InterfaceDoc.Header.class),
                        convertList(groupedElementKinds.get(ENUM), EnumDoc.Header.class),
                        convertList(groupedElementKinds.get(RECORD), RecordDoc.Header.class),
                        convertList(groupedElementKinds.get(FIELD), VariableDoc.class),
                        convertList(groupedElementKinds.get(CONSTRUCTOR), ConstructorDoc.class),
                        convertList(groupedElementKinds.get(METHOD), MethodDoc.class),
                        getAnnotations(e.getAnnotationMirrors()),
                        getElementComment(e)
                    )
                );
                return new Pair<ElementKind, EnumDoc.Header>(e.getKind(), header);
            }
            case RECORD -> {
                RecordDoc.Header header =
                    new RecordDoc.Header(
                        typeName,
                        getTypeParametersListFQN(e.getTypeParameters()),
                        getModfiers(e.getModifiers()),
                        getFullTypeUsageFQN(e.getSuperclass()),
                        getTypeNames(e.getInterfaces())
                    );


                docWriter.writeTypeDoc(
                    packageName,
                    new RecordDoc(
                        header,
                        convertList(groupedElementKinds.get(CLASS), ClassDoc.Header.class),
                        convertList(groupedElementKinds.get(INTERFACE), InterfaceDoc.Header.class),
                        convertList(groupedElementKinds.get(ENUM), EnumDoc.Header.class),
                        convertList(groupedElementKinds.get(RECORD), RecordDoc.Header.class),
                        e
                            .getRecordComponents()
                            .stream()
                            .map(component -> ((Pair<ElementKind, RecordComponentDoc>) component.accept(this, null)).right())
                            .toList(),
                        convertList(groupedElementKinds.get(FIELD), VariableDoc.class),
                        convertList(groupedElementKinds.get(CONSTRUCTOR), ConstructorDoc.class),
                        convertList(groupedElementKinds.get(METHOD), MethodDoc.class),
                        getAnnotations(e.getAnnotationMirrors()),
                        getElementComment(e)
                    )
                );
                return new Pair<ElementKind, RecordDoc.Header>(e.getKind(), header);
            }
            default -> {
                reporter.print(Diagnostic.Kind.WARNING, "Unsupported type kind: " + e.getKind() + " for " + qualifiedTypeName);
                return null;
            }
        }
    }

    @Override
    public Pair<ElementKind, ExecutableDoc> visitExecutable(ExecutableElement e, Void p) {
        switch (e.getKind()) {
            case METHOD -> {
                return new Pair<>(e.getKind(),
                    new MethodDoc(
                        e.getSimpleName().toString(),
                        getExecutableSignatureFQN(e),
                        getModfiers(e.getModifiers()),
                        getFullTypeUsageFQN(e.getReturnType()),
                        getParameters(e),
                        getTypeNames(e.getThrownTypes()),
                        e.isDefault(),
                        e.isVarArgs(),
                        getAnnotations(e.getAnnotationMirrors()),
                        getElementComment(e)
                    )
                );
            }

            case CONSTRUCTOR -> {
                return new Pair<>(e.getKind(),
                    new ConstructorDoc(
                        e.getSimpleName().toString(),
                        getModfiers(e.getModifiers()),
                        getParameters(e),
                        getTypeNames(e.getThrownTypes()),
                        e.isVarArgs(),
                        getAnnotations(e.getAnnotationMirrors()),
                        getElementComment(e)
                    )
                );

            }
            default -> {
                reporter.print(Diagnostic.Kind.WARNING, "Unsupported executable kind: " + e.getKind() + " for " + e.getSimpleName());
                return null;
            }
        }

    }

    @Override
    public Pair<ElementKind, VariableDoc> visitVariable(VariableElement e, Void p) {
        switch (e.getKind()) {
            case FIELD -> {
                return new Pair(e.getKind(), getVariable(e));
            }
            case ENUM_CONSTANT, PARAMETER, RESOURCE_VARIABLE, LOCAL_VARIABLE, EXCEPTION_PARAMETER -> {
                // These are handled in other visit methods or ignored.
                return null;
            }
            default -> {
                reporter.print(Diagnostic.Kind.WARNING, "Unsupported variable kind: " + e.getKind() + " for " + e.getSimpleName());
                return null;
            }
        }
    }

    @Override
    public Pair<ElementKind, RecordComponentDoc> visitRecordComponent(RecordComponentElement e, Void p) {
        return new Pair<>(
            e.getKind(),
            new RecordComponentDoc(
                e.getSimpleName().toString(),
                getFullTypeUsageFQN(e.asType()),
                getElementComment(e)
            )
        );
    }
}

