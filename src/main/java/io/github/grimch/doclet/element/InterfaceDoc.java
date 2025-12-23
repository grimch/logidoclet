package io.github.grimch.doclet.element;

import java.util.List;

public record InterfaceDoc(
    String name,
    String packageName,
    List<String> modifiers,
    String extendsType,
    List<String> implementsTypes,
    List<String>  permitsTypes,
    List<String> classes,
    List <String> interfaces,
    List <String> enums,
    List<String> records,
    List<VariableDoc> fields,
    List<MethodDoc> methods,
    List<String> annotations,
    String documentation
) implements TypeDoc {}
