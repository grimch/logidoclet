package io.github.grimch.doclet.element;

import java.util.List;

public record MethodDoc(
    String name,
    String typeParameters,
    List<String> modifiers,
    String returnType,
    List<VariableDoc> parameters,
    List<String> throwsTypes,
    boolean isDefault,
    boolean isVarArgs,
    List<String> annotations,
    String documentation
) implements ExecutableDoc {}