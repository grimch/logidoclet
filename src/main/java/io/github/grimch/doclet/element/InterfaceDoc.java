package io.github.grimch.doclet.element;

import java.util.List;

public record InterfaceDoc(
    Header header,
    List<ClassDoc.Header> classes,
    List<InterfaceDoc.Header> interfaces,
    List<EnumDoc.Header> enums,
    List<RecordDoc.Header> records,
    List<VariableDoc> fields,
    List<MethodDoc> methods,
    List<String> annotations,
    String documentation
) implements TypeDoc {
    public record Header(
        String name,
        String typeParameters,
        List<String> modifiers,
        String extendsType,
        List<String> implementsTypes,
        List<String>  permitsTypes
    ) implements NamedHeader {}
}
