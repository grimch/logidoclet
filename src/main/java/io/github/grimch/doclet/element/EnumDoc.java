package io.github.grimch.doclet.element;

import java.util.List;

public record EnumDoc(
    Header header,
    List<ClassDoc.Header> classes,
    List<InterfaceDoc.Header> interfaces,
    List<EnumDoc.Header> enums,
    List<RecordDoc.Header> records,
    List<VariableDoc> fields,
    List<ConstructorDoc> constructors,
    List<MethodDoc> methods,
    List<String> annotations,
    String documentation
) implements TypeDoc {
    public record Header(
        String name,
        List<String> modifiers,
        List<String> implementsTypes
    ) implements NamedHeader {}
}
