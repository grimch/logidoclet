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
package io.github.grimch.doclet.sample_module.types.basic;

// *** Interface with Generics and Wildcards ***
// Note: This interface is now non-sealed to participate in the sealed hierarchy test.
/**
 * Represents a sealed interface demonstrating generics, wildcards, default, and static methods.
 * This interface is part of a sealed hierarchy, permitting {@link C1} and {@link io.github.grimch.doclet.sample_module.types.advanced.C4}
 * as direct implementations.
 *
 * @param <T> The first type parameter, which must extend {@link Number}.
 * @param <U> The second type parameter.
 * @see C1
 * @see io.github.grimch.doclet.sample_module.types.advanced.C4
 */
public sealed interface I1<T extends Number, U> permits C1, io.github.grimch.doclet.sample_module.types.advanced.C4 {
    /**
     * An abstract method that takes an argument of type {@code T}.
     * @param arg1 The argument of type {@code T}.
     */
    void m1(T arg1);
    /**
     * A default method that returns its input argument.
     * @param arg2 The argument of type {@code U}.
     * @return The input argument {@code arg2}.
     */
    default U m2(U arg2) { return arg2; }
    /**
     * A static generic method that prints its argument to the console.
     * @param <V> The type parameter for this method.
     * @param arg3 The argument of type {@code V}.
     */
    static <V> void m3(V arg3) { System.out.println(arg3); }
}

