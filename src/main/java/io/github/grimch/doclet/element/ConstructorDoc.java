package io.github.grimch.doclet.element;

import java.util.List;

public record ConstructorDoc(
    String name,
    List<String> modifiers,
    List<VariableDoc> parameters,
    List<String> throwsTypes,
    boolean isVarArgs,
    List<String> annotations,
    String documentation
) implements ExecutableDoc {}
