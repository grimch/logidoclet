package io.github.grimch.doclet.element;

import java.util.List;

public record PackageDoc(
    String name,
    List<String> classes,
    List<String> interfaces,
    List<String> enums,
    List<String> records
) implements NamedDoc {}
