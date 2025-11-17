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

/**
 * Represents a basic public enum demonstrating enum constants, fields, and constructors.
 */
public enum E1 {
    /**
     * The first enum constant, with a default code of 0.
     */
    VALUE_A,
    /**
     * The second enum constant, initialized with a specific code.
     */
    VALUE_B(10);

    /**
     * A private final integer field representing the code associated with each enum constant.
     */
    private final int code;
    /**
     * A public string field providing a description for the enum value.
     */
    public String description = "Enum value";

    /**
     * Default constructor for enum constants without explicit arguments.
     * Initializes the code to 0.
     */
    E1() { this.code = 0; }

    /**
     * Constructor for enum constants with a specific code.
     * @param code The integer code to associate with this enum constant.
     */
    E1(int code) { this.code = code; }

    /**
     * Returns the code associated with this enum constant.
     * @return The integer code.
     */
    public int getCode() { return code; }
}
