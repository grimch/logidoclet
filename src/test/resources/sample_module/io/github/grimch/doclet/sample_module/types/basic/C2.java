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

import java.io.Serializable;
import java.util.List;

/**
 * Represents a sealed abstract generic class with complex type parameter bounds.
 * This class demonstrates the use of generics, abstract methods, and method hiding.
 * It is part of a sealed hierarchy, permitting only {@link C3} as a direct subclass.
 *
 * @param <T> The type parameter, which must extend both {@link C1} and {@link Serializable}.
 * @see C1
 * @see C3
 * @see Serializable
 */

public abstract sealed class C2<T extends C1 & Serializable> permits C3 {
    /**
     * Default constructor.
     */
    protected C2() {}
    // --- FIELDS ---
    /**
     * A protected generic field of type {@code T}.
     */
    protected T protectedGenericField;
    /**
     * A public generic list of type {@code T}.
     */
    public List<T> publicGenericList;

    // --- METHODS ---
    /**
     * A protected abstract method that must be implemented by concrete subclasses.
     */
    protected abstract void m7();

    /**
     * A generic method demonstrating a wildcard type parameter with a 'super' bound.
     *
     * @param <L> A generic type parameter for the method.
     * @param list A list whose elements are supertypes of {@link C1}.
     */
    public <L> void m8(java.util.List<? super C1> list) {
        // Implementation
    }

    /**
     * A static method used for testing method hiding.
     * This method prints a message indicating it's from C2.
     */
    public static void hiddenStaticMethod() { System.out.println("C2 static"); }
}

