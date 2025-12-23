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
import io.github.grimch.doclet.prolog.*;
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

    private final List<Term> indexModuleList = new ArrayList<>();
    private final List<Term> indexPackageList = new ArrayList<>();
    private final Set<String> internalPackageNames = new HashSet<>();


    public ElementVisitor(DocWriter docWriter, DocletEnvironment docEnv, Reporter reporter) {
        this.docWriter = docWriter;
        this.docEnv = docEnv;
        this.treeUtils = docEnv.getDocTrees();
        this.reporter = reporter;
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
                .map(TypeMirror::toString)
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
            element.asType().toString(),
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
                            parameter.asType().toString(),
                            getAnnotations(parameter.getAnnotationMirrors())
                        )
                    )
                    .toList()
            );

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
            Map<ElementKind, List<String>> groupedElementKinds = acceptElementChilds(e);
            docWriter.writePackageDoc(
                new PackageDoc(
                    packageName,
                    groupedElementKinds.get(CLASS),
                    groupedElementKinds.get(INTERFACE),
                    groupedElementKinds.get(ENUM),
                    groupedElementKinds.get(RECORD)
                )
            );
        }
        return null;

    }

    @Override
    public Pair<ElementKind, String> visitType(TypeElement e, Void p) {
        String qualifiedTypeName = e.getQualifiedName().toString();
        String packageName = docEnv.getElementUtils().getPackageOf(e).getQualifiedName().toString();
        String typeName = e.getSimpleName().toString();

        Map<ElementKind, List<Object>> groupedElementKinds = acceptElementChilds(e);

        switch (e.getKind()) {
            case CLASS ->
                docWriter.writeTypeDoc(
                    new ClassDoc(
                        typeName,
                        packageName,
                        getModfiers(e.getModifiers()),
                        e.getSuperclass().toString(),
                        getTypeNames(e.getInterfaces()),
                        convertList(groupedElementKinds.get(CLASS), String.class),
                        convertList(groupedElementKinds.get(INTERFACE), String.class),
                        convertList(groupedElementKinds.get(ENUM), String.class),
                        convertList(groupedElementKinds.get(RECORD), String.class),
                        convertList(groupedElementKinds.get(FIELD), VariableDoc.class),
                        convertList(groupedElementKinds.get(CONSTRUCTOR), ConstructorDoc.class),
                        convertList(groupedElementKinds.get(METHOD), MethodDoc.class),
                        getAnnotations(e.getAnnotationMirrors()),
                        getElementComment(e)
                    )
                );
            case INTERFACE ->
                docWriter.writeTypeDoc(
                    new InterfaceDoc(
                        typeName,
                        packageName,
                        getModfiers(e.getModifiers()),
                        e.getSuperclass().toString(),
                        getTypeNames(e.getInterfaces()),
                        getTypeNames(e.getPermittedSubclasses()),
                        convertList(groupedElementKinds.get(CLASS), String.class),
                        convertList(groupedElementKinds.get(INTERFACE), String.class),
                        convertList(groupedElementKinds.get(ENUM), String.class),
                        convertList(groupedElementKinds.get(RECORD), String.class),
                        convertList(groupedElementKinds.get(FIELD), VariableDoc.class),
                        convertList(groupedElementKinds.get(METHOD), MethodDoc.class),
                        getAnnotations(e.getAnnotationMirrors()),
                        getElementComment(e)
                    )
                );
            case ENUM ->
                docWriter.writeTypeDoc(
                    new EnumDoc(
                        typeName,
                        packageName,
                        getModfiers(e.getModifiers()),
                        getTypeNames(e.getInterfaces()),
                        convertList(groupedElementKinds.get(CLASS), String.class),
                        convertList(groupedElementKinds.get(INTERFACE), String.class),
                        convertList(groupedElementKinds.get(ENUM), String.class),
                        convertList(groupedElementKinds.get(RECORD), String.class),
                        convertList(groupedElementKinds.get(FIELD), VariableDoc.class),
                        convertList(groupedElementKinds.get(CONSTRUCTOR), ConstructorDoc.class),
                        convertList(groupedElementKinds.get(METHOD), MethodDoc.class),
                        getAnnotations(e.getAnnotationMirrors()),
                        getElementComment(e)
                    )
                );
            case RECORD -> {
                docWriter.writeTypeDoc(
                    new RecordDoc(
                        typeName,
                        packageName,
                        getModfiers(e.getModifiers()),
                        e.getSuperclass().toString(),
                        getTypeNames(e.getInterfaces()),
                        convertList(groupedElementKinds.get(CLASS), String.class),
                        convertList(groupedElementKinds.get(INTERFACE), String.class),
                        convertList(groupedElementKinds.get(ENUM), String.class),
                        convertList(groupedElementKinds.get(RECORD), String.class),
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
            }
            default -> {
                reporter.print(Diagnostic.Kind.WARNING, "Unsupported type kind: " + e.getKind() + " for " + qualifiedTypeName);
                return null;
            }
        }

        // to do: write out
        String fullyQualifiedName = e.asType().toString();
        return new Pair<ElementKind, String>(e.getKind(), fullyQualifiedName.substring(fullyQualifiedName.lastIndexOf('.') + 1));
    }

    @Override
    public Pair<ElementKind, ExecutableDoc> visitExecutable(ExecutableElement e, Void p) {
        switch (e.getKind()) {
            case METHOD -> {
                return new Pair<>(e.getKind(),
                    new MethodDoc(
                        e.getSimpleName().toString(),
                        getModfiers(e.getModifiers()),
                        e.getReturnType().toString(),
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
                e.asType().toString(),
                getElementComment(e)
            )
        );
    }
}

