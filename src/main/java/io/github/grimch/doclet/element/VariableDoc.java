package io.github.grimch.doclet.element;

import java.util.List;

public record VariableDoc(
    String name,
    List<String> modifiers,
    String type,
    List<String> annotations
) implements NamedDoc {};
