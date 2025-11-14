package io.github.grimch.doclet.prolog;

/**
 * Represents a Prolog atom.
 */
public class Atom extends Term {
    private final String value;

    public Atom(String value) {
        // Escape single quotes for Prolog atom representation
        this.value = value.replace("'", "''");
    }

    @Override
    public String toString() {
        // If the atom contains special characters or starts with an uppercase letter,
        // it needs to be quoted in Prolog. A simple heuristic is to always quote it
        // to avoid issues, or check for specific patterns. For simplicity,
        // we'll quote if it contains non-alphanumeric characters or starts with uppercase.
        // Or, if it's a keyword like "true", "false", "null"
        if (value.matches("^[a-z][a-zA-Z0-9_]*$") &&
            !value.equals("true") && !value.equals("false") && !value.equals("null")) {
            return value;
        } else {
            return "'" + value + "'";
        }
    }
}
