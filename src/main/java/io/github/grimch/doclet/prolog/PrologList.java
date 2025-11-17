package io.github.grimch.doclet.prolog;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Prolog list, which is an ordered collection of terms.
 * <p>
 * A Prolog list is denoted by square brackets {@code []} with its elements separated by commas.
 * For example, {@code [apple, banana, orange]} is a list of three atoms.
 * Lists can contain other terms, including other lists.
 *
 * @see Term
 */
public class PrologList extends Term {
    private final List<Term> elements;

    /**
     * Constructs a new Prolog list from a Java {@link List} of {@link Term}s.
     *
     * @param elements The list of terms that will form the elements of the Prolog list.
     */
    public PrologList(List<Term> elements) {
        this.elements = elements;
    }

    /**
     * Generates the string representation of the Prolog list.
     * <p>
     * The output is a string in the format {@code [elem1, elem2, ...]}, where each element
     * is recursively converted to its own string representation.
     *
     * @return A string representing the list in valid Prolog syntax.
     */
    @Override
    public String toString() {
        String elems = elements.stream()
                .map(Term::toString)
                .collect(Collectors.joining(", "));
        return "[" + elems + "]";
    }
}