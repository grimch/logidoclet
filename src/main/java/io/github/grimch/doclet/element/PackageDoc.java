package io.github.grimch.doclet.element;

import java.util.List;

public record PackageDoc(
    String name,
    List<ClassDoc.Header> classes,
    List<InterfaceDoc.Header> interfaces,
    List<EnumDoc.Header> enums,
    List<RecordDoc.Header> records
) {}
