package io.github.grimch.doclet.element;

import java.util.List;

public record EnumDoc(
    String name,
    String packageName,
    List<String> modifiers,
    List<String> implementsTypes,
    List<String> classes,
    List <String> interfaces,
    List <String> enums,
    List<String> records,
    List<VariableDoc> fields,
    List<ConstructorDoc> constructors,
    List<MethodDoc> methods,
    List<String> annotations,
    String documentation
) implements TypeDoc {}
