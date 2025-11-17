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
