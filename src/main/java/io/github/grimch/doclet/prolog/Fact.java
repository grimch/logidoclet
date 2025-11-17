package io.github.grimch.doclet.prolog;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Prolog fact, which is a statement that declares a relationship between objects.
 * <p>
 * A fact consists of a predicate (the name of the relationship) and a list of terms (the objects).
 * It is represented in the format {@code predicate(term1, term2, ...)}.
 * <p>
 * For example, {@code parent(john, mary)} could be a fact where 'parent' is the predicate,
 * and 'john' and 'mary' are terms (Atoms).
 *
 * @see Term
 * @see Atom
 * @see PrologList
 */
public class Fact extends Term {
    private final String predicate;
    private final List<Term> arguments;

    /**
     * Constructs a new Prolog fact.
     *
     * @param predicate The name of the fact's predicate.
     * @param arguments A variable number of {@link Term} objects that are the arguments of the fact.
     */
    public Fact(String predicate, Term... arguments) {
        this.predicate = predicate;
        this.arguments = Arrays.asList(arguments);
    }

    /**
     * Generates the string representation of the Prolog fact.
     * <p>
     * The output is a string in the format {@code predicate(arg1, arg2, ...)}, where each argument
     * is recursively converted to its own string representation.
     *
     * @return A string representing the fact in valid Prolog syntax.
     */
    @Override
    public String toString() {
        String args = arguments.stream()
                .map(Term::toString)
                .collect(Collectors.joining(", "));
        return predicate + "(" + args + ")";
    }
}