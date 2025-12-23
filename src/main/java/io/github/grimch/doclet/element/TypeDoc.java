package io.github.grimch.doclet.element;

import java.util.List;

public sealed interface TypeDoc
    permits ClassDoc, InterfaceDoc, EnumDoc, RecordDoc {
    NamedHeader header();
}
