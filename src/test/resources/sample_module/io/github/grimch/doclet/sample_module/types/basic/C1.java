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
import java.util.Map;

/**
 * Represents a basic public non-sealed class demonstrating various field types,
 * constructors, methods, and nested types. It implements the {@link I1} interface
 * with specific type arguments and {@link java.io.Serializable}.
 * <p>
 * This class is part of a sealed hierarchy, permitting specific subclasses.
 * </p>
 * @see I1
 * @see java.io.Serializable
 * @see C2
 */
public non-sealed class C1 implements I1<Integer, String>, Serializable {
    // --- FIELDS ---
    // 1. Primitive Fields
    /**
     * A public static final string constant.
     */
    public static final String PUBLIC_STATIC_FINAL_STRING = "Test";
    /**
     * A public integer field.
     */
    public int publicInt;
    /**
     * A protected volatile double field.
     */
    protected volatile double protectedVolatileDouble;
    /**
     * A private final boolean field, initialized to true.
     */
    private final boolean privateFinalBoolean = true;
    /**
     * A transient short field.
     */
    transient short transientShort;
    /**
     * A static character field, initialized to 'Z'.
     */
    static char staticChar = 'Z';

    // 2. Array Fields
    /**
     * A public array of strings.
     */
    public String[] publicStringArray;

    // 3. Complex/Generic Fields with Type Annotation
    /**
     * A public generic list of Integers, annotated with {@link TestAnno}.
     */
    public @TestAnno List<Integer> publicGenericList;
    /**
     * A private final self-referencing instance of C1.
     */
    private final C1 privateSelfReference = new C1();
    /**
     * A protected map with wildcard types for keys and values.
     * Keys extend {@link Number} and values are super of {@link String}.
     */
    protected Map<? extends Number, ? super String> protectedWildcardMap;

    // --- CONSTRUCTORS START ---
    /**
     * Public canonical constructor for C1.
     * Initializes default values for fields.
     */
    public C1() {}

    /**
     * Overloaded protected constructor for C1.
     * @param id An integer identifier to initialize {@link #publicInt}.
     */
    protected C1(int id) {
        this.publicInt = id;
    }

    /**
     * Package-private constructor for C1.
     * @param name A string name (usage not specified in implementation).
     */
    C1(String name) {
        // ...
    }
    // --- CONSTRUCTORS END ---

    /**
     * Implements the {@code m1} method from the {@link I1} interface.
     * @param arg1 An Integer argument as specified by the interface.
     */
    @Override
    public void m1(Integer arg1) {
        // Implementation
    }

    /**
     * A public method demonstrating a checked exception.
     * This method is {@link Deprecated}.
     * @throws java.io.IOException if an I/O error occurs.
     * @deprecated This method is deprecated and should not be used.
     */
    @Deprecated
    public void m4() throws java.io.IOException {
        // Implementation
    }

    /**
     * Demonstrates a method with a receiver parameter annotation and a local record declaration.

     * @param input A list of strings to be processed.
     * @return The input list of strings.
     */
    public @TestAnno List<String> getList(C1 this, List<String> input) {
        // Local Record declaration inside a method
        record LocalR(String value) {}
        LocalR local = new LocalR("Local Test");

        // Use of local variable
        System.out.println(local.value());
        return input;
    }


    /**
     * A static synchronized method.
     * This method performs a synchronized operation.
     */
    public static synchronized void m5() {
        // Implementation
    }

    // --- NESTED TYPES START ---
    /**
     * A public static nested class within C1.
     */
    public static class StaticNestedC1 {
        /**
         * Default constructor.
         */
        public StaticNestedC1() {}
        /**
         * A private final integer value.
         */
        private final int value = 1;
        /**
         * Returns the value of this nested class.
         * @return The integer value.
         */
        public int getValue() { return value; }
    }

    /**
     * A protected non-static inner class within C1.
     */
    protected class InnerC2 {
        /**
         * Default constructor.
         */
        protected InnerC2() {}
        /**
         * A method within the inner class that accesses the outer class's field.
         */
        public void innerMethod() {
            System.out.println(publicInt);
        }
    }
    // --- NESTED TYPES END ---
}
