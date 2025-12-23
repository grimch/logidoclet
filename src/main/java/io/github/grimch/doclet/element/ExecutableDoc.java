package io.github.grimch.doclet.element;

import java.util.List;

public interface ExecutableDoc extends NamedDoc {
    String name();
    List<String> modifiers();
    List<VariableDoc> parameters();
    List<String> throwsTypes();
    boolean isVarArgs();
}
