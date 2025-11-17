package io.github.grimch.doclet.prolog;

/**
 * Represents the abstract base for all Prolog terms, which are the fundamental data structures in Prolog.
 * <p>
 * In this implementation, a term can be a simple term like an {@link Atom} or a compound term
 * like a {@link Fact} or a {@link PrologList}. This class provides a common supertype
 * for all these structures.
 *
 * @see Atom
 * @see Fact
 * @see PrologList
 */
public abstract class Term {
    /**
     * Returns the string representation of the Prolog term.
     * <p>
     * Each subclass must implement this method to provide a valid Prolog syntax
     * for that specific term. For example, an Atom might be represented as {@code 'my_atom'},
     * a Fact as {@code predicate(arg1, arg2)}, and a List as {@code [elem1, elem2]}.
     *
     * @return A string that can be parsed by a Prolog interpreter.
     */
    public abstract String toString();
}
