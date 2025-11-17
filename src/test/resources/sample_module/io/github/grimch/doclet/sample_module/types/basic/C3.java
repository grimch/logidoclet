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

/**
 * Represents a final class that extends {@link C2} with {@link C1} as its type argument.
 * As a final class, it cannot be subclassed further.
 * This class demonstrates implementing an abstract method from its superclass and method hiding.
 *
 * @see C2
 * @see C1
 */
public final class C3 extends C2<C1> {

    /**
     * Private constructor for C3.
     * This prevents direct instantiation from outside the class.
     */
    private C3() {}

    /**
     * Implements the abstract method {@code m7()} from {@link C2}.
     */
    @Override
    protected void m7() {
        // Implementation
    }

    /**
     * A static method that hides the {@code hiddenStaticMethod()} in {@link C2}.
     * This method prints a message indicating it's from C3.
     */
    public static void hiddenStaticMethod() {
        System.out.println("C3 static (hiding C2's)");
    }

    /**
     * A public method that throws an unchecked {@link NullPointerException}.
     *
     * @throws NullPointerException always, as it's explicitly thrown.
     */
    public void m9() throws NullPointerException {
        throw new NullPointerException();
    }
}
