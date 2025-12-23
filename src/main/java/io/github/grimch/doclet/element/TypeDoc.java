package io.github.grimch.doclet.element;

import java.util.List;

public sealed interface TypeDoc
    extends NamedDoc
    permits ClassDoc, InterfaceDoc, EnumDoc, RecordDoc {
    String packageName();
    List<String> modifiers();
}
