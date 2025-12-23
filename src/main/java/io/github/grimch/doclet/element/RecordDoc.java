package io.github.grimch.doclet.element;

import java.util.List;

public record RecordDoc(
    String name,
    String packageName,
    String typeParameters,
    List<String> modifiers,
    String extendsType,
    List<String> implementsTypes,
    List<String> classes,
    List <String> interfaces,
    List <String> enums,
    List<String> records,
    List<RecordComponentDoc> recordComponents,
    List<VariableDoc> fields,
    List<ConstructorDoc> constructors,
    List<MethodDoc> methods,
    List<String> annotations,
    String documentation
) implements TypeDoc { }