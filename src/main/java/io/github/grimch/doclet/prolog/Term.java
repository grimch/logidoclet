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

