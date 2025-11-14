package io.github.grimch.doclet.prolog;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Prolog fact with a predicate and arguments.
 */
public class Fact extends Term {
    private final String predicate;
    private final List<Term> arguments;

    public Fact(String predicate, Term... arguments) {
        this.predicate = predicate;
        this.arguments = Arrays.asList(arguments);
    }

    @Override
    public String toString() {
        String args = arguments.stream()
                .map(Term::toString)
                .collect(Collectors.joining(", "));
        return predicate + "(" + args + ")";
    }
}