package io.github.grimch.doclet.util;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.*;
import java.util.List;
import java.util.stream.Collectors;

public class TypeUtils {

    /**
     * 1. USAGE SITE: For fields, return types, parameters, and super-interfaces.
     * Returns: "java.util.List<T>"
     */
    public static String getFullTypeUsageFQN(TypeMirror mirror) {
        if (mirror == null || mirror.getKind().equals(TypeKind.NONE)) return null;

        if (mirror instanceof DeclaredType declaredType) {
            TypeElement element = (TypeElement) declaredType.asElement();
            String fqn = element.getQualifiedName().toString();
            List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();

            if (typeArgs.isEmpty()) return fqn;

            return fqn + typeArgs.stream()
                .map(TypeUtils::getFullTypeUsageFQN)
                .collect(Collectors.joining(", ", "<", ">"));
        }

        if (mirror instanceof TypeVariable typeVar) {
            return typeVar.asElement().getSimpleName().toString();
        }

        if (mirror instanceof WildcardType wildcard) {
            if (wildcard.getExtendsBound() != null) {
                String bound = getFullTypeUsageFQN(wildcard.getExtendsBound());
                return bound.equals("java.lang.Object") ? "?" : "? extends " + bound;
            }
            if (wildcard.getSuperBound() != null) {
                return "? super " + getFullTypeUsageFQN(wildcard.getSuperBound());
            }
            return "?";
        }

        return mirror.toString();
    }

    /**
     * 2. SINGLE PARAMETER DECLARATION: Helper for one parameter.
     * Returns: "T extends java.lang.Number"
     */
    public static String getSingleParameterDeclarationFQN(TypeParameterElement tp) {
        String name = tp.getSimpleName().toString();
        List<String> explicitBounds = tp.getBounds().stream()
            .map(TypeUtils::getFullTypeUsageFQN)
            .filter(b -> !b.equals("java.lang.Object"))
            .toList();

        if (explicitBounds.isEmpty()) return name;
        return name + " extends " + String.join(" & ", explicitBounds);
    }

    /**
     * 3. TYPE PARAMETERS LIST: For Class/Interface headers.
     * Returns: "<T extends java.lang.Number, U>"
     */
    public static String getTypeParametersListFQN(List<? extends TypeParameterElement> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        return params.stream()
            .map(TypeUtils::getSingleParameterDeclarationFQN)
            .collect(Collectors.joining(", ", "<", ">"));
    }

    /**
     * 4. EXECUTABLE SIGNATURE: For Method headers.
     * Example: <K extends java.lang.Comparable<K>> K m10
     */
    public static String getExecutableSignatureFQN(ExecutableElement method) {
        if (! method.getTypeParameters().isEmpty()) {
            return getTypeParametersListFQN(method.getTypeParameters());
        } else {
            return null;
        }
    }
}