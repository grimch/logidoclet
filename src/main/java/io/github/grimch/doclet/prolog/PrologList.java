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
    public List<Term> getElements() {
        return elements;
    }

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
