package io.github.grimch.doclet.prolog;

import java.util.stream.Collectors;

/**
 * A utility class for pretty-printing Prolog {@link Fact} objects.
 * It formats facts and lists according to specific indentation and line-breaking rules
 * to enhance readability.
 */
public class PrettyPrinter {
    private final String indent;
    private static final String DEFAULT_INDENT = "    "; // 4 spaces for indent

    /**
     * Constructs a PrettyPrinter with the default indentation (4 spaces).
     */
    public PrettyPrinter() {
        this.indent = DEFAULT_INDENT;
    }

    /**
     * Constructs a PrettyPrinter with a custom indentation string.
     *
     * @param indent The string to use for each level of indentation (e.g., "  " for 2 spaces, "\t" for a tab).
     */
    public PrettyPrinter(String indent) {
        this.indent = indent;
    }

    /**
     * Pretty-prints a Prolog {@link Fact} object into a human-readable string format.
     * The formatting rules are:
     * <ul>
     *     <li>Fact name and opening bracket are on the same line.</li>
     *     <li>Facts without nested facts or lists (containing only {@link Atom}s or empty {@link PrologList}s)
     *         are written on a single line.</li>
     *     <li>Facts with at least one nested {@link Fact} or non-empty {@link PrologList} are written
     *         across multiple lines with indentation.</li>
     *     <li>{@link PrologList}s containing only {@link Atom}s are written on a single line.</li>
     *     <li>{@link PrologList}s containing non-atomic entries ({@link Fact}s or other {@link PrologList}s)
     *         are written across multiple lines with indentation.</li>
     * </ul>
     *
     * @param fact The {@link Fact} to pretty-print.
     * @return A string representation of the fact, formatted for readability.
     */
    public String prettyPrint(Fact fact) {
        StringBuilder sb = new StringBuilder();
        printFact(sb, fact, 0, true);
        return sb.toString();
    }

    /**
     * Recursively prints a {@link Fact} to the StringBuilder with appropriate indentation.
     *
     * @param sb The StringBuilder to append the formatted output to.
     * @param fact The {@link Fact} to print.
     * @param indentLevel The current level of indentation.
     * @param isTopLevel A flag indicating if this is the top-level fact being printed.
     */
    private void printFact(StringBuilder sb, Fact fact, int indentLevel, boolean isTopLevel) {
        sb.append(fact.getPredicate()).append("(");

        boolean isSimpleFact = isSimple(fact);

        if (isSimpleFact) {
            // Print simple facts on a single line
            sb.append(fact.getArguments().stream()
                    .map(Term::toString) // Use Term's toString for simple representation
                    .collect(Collectors.joining(", ")));
        } else {
            // Print complex facts on multiple lines with indentation
            sb.append("\n");
            for (int i = 0; i < fact.getArguments().size(); i++) {
                Term arg = fact.getArguments().get(i);
                appendIndentation(sb, indentLevel + 1);
                printTerm(sb, arg, indentLevel + 1);
                if (i < fact.getArguments().size() - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            appendIndentation(sb, indentLevel);
        }
        sb.append(")");
        if (isTopLevel) {
            sb.append("."); // Only add the final period for the top-level fact
        }
    }

    /**
     * Prints a {@link Term} (which can be an {@link Atom}, {@link Fact}, or {@link PrologList})
     * to the StringBuilder with appropriate indentation.
     *
     * @param sb The StringBuilder to append the formatted output to.
     * @param term The {@link Term} to print.
     * @param indentLevel The current level of indentation.
     */
    private void printTerm(StringBuilder sb, Term term, int indentLevel) {
        if (term instanceof Fact) {
            printFact(sb, (Fact) term, indentLevel, false); // Nested facts are not top-level
        } else if (term instanceof PrologList) {
            printPrologList(sb, (PrologList) term, indentLevel);
        } else { // Atom
            sb.append(term.toString());
        }
    }

    /**
     * Prints a {@link PrologList} to the StringBuilder with appropriate indentation.
     *
     * @param sb The StringBuilder to append the formatted output to.
     * @param list The {@link PrologList} to print.
     * @param indentLevel The current level of indentation.
     */
    private void printPrologList(StringBuilder sb, PrologList list, int indentLevel) {
        sb.append("[");
        boolean isSimpleList = isSimple(list);

        if (isSimpleList) {
            sb.append(list.getElements().stream()
                    .map(Term::toString)
                    .collect(Collectors.joining(", ")));
        } else {
            sb.append("\n");
            for (int i = 0; i < list.getElements().size(); i++) {
                Term element = list.getElements().get(i);
                appendIndentation(sb, indentLevel + 1);
                printTerm(sb, element, indentLevel + 1);
                if (i < list.getElements().size() - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            appendIndentation(sb, indentLevel);
        }
        sb.append("]");
    }

    /**
     * Determines if a {@link Fact} is "simple" enough to be printed on a single line.
     * A fact is considered simple if all its arguments are {@link Atom}s or empty {@link PrologList}s,
     * and no argument is a nested {@link Fact} or a non-empty {@link PrologList}.
     *
     * @param fact The {@link Fact} to check.
     * @return {@code true} if the fact is simple, {@code false} otherwise.
     */
    private boolean isSimple(Fact fact) {
        if (fact.getArguments().isEmpty()) {
            return true;
        }
        for (Term arg : fact.getArguments()) {
            if (arg instanceof Fact || (arg instanceof PrologList && !((PrologList) arg).getElements().isEmpty())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if a {@link PrologList} is "simple" enough to be printed on a single line.
     * A list is considered simple if all its elements are {@link Atom}s.
     *
     * @param list The {@link PrologList} to check.
     * @return {@code true} if the list is simple, {@code false} otherwise.
     */
    private boolean isSimple(PrologList list) {
        if (list.getElements().isEmpty()) {
            return true;
        }
        for (Term element : list.getElements()) {
            if (!(element instanceof Atom)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Appends the appropriate level of indentation to the StringBuilder.
     *
     * @param sb The StringBuilder to append to.
     * @param level The number of indentation levels.
     */
    private void appendIndentation(StringBuilder sb, int level) {
        for (int i = 0; i < level; i++) {
            sb.append(indent);
        }
    }
}
