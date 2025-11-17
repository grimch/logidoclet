/*
 * This file is part of LogiDoclet.
 *
 * Copyright (c) 2025 The LogiDoclet Authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.grimch.doclet.prolog;

/**
 * Represents a Prolog atom, a fundamental data type in Prolog for representing constant values.
 * <p>
 * An atom is a general-purpose, symbolic name. In this context, it is used to represent identifiers,
 * strings, and other constant values from the Java source code in a format that Prolog can understand.
 * <p>
 * This class handles the necessary escaping and quoting to ensure that the string representation
 * is a valid Prolog atom.
 *
 * @see Term
 * @see Fact
 */
public class Atom extends Term {
    private final String value;

    /**
     * Constructs a new Atom with the specified string value.
     * The constructor automatically escapes any single quotes within the value
     * to ensure it is a valid Prolog string literal when quoted.
     *
     * @param value The string value of the atom.
     */
    public Atom(String value) {
        // Escape single quotes for Prolog atom representation
        this.value = value.replace("'", "''");
    }

    /**
     * Returns the Prolog representation of this atom.
     * <p>
     * The method applies Prolog's quoting rules:
     * <ul>
     *     <li>If the atom's value is a simple, unquoted atom (starts with a lowercase letter and contains only
     *     alphanumeric characters and underscores), it is returned as is.</li>
     *     <li>Otherwise, the value is enclosed in single quotes to form a valid Prolog quoted atom.</li>
     * </ul>
     * This ensures that atoms with special characters, spaces, or those starting with an uppercase letter
     * are correctly interpreted by a Prolog engine.
     *
     * @return A string representing the atom in valid Prolog syntax.
     */
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

