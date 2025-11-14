package io.github.grimch.doclet.prolog;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Prolog list.
 */
public class PrologList extends Term {
    private final List<Term> elements;

    public PrologList(List<Term> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        String elems = elements.stream()
                .map(Term::toString)
                .collect(Collectors.joining(", "));
        return "[" + elems + "]";
    }
}