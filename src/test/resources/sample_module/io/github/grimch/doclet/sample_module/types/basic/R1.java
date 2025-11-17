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
 * Represents a public record demonstrating record components, a compact constructor,
 * a static field, and an instance method. It implements {@link java.io.Serializable}.
 *
 * @param id The unique identifier for the record.
 * @param name The name associated with the record.
 */
public record R1(int id, String name) implements java.io.Serializable {
    /**
     * Compact constructor for R1.
     * Validates that the ID is not negative.
     * @throws IllegalArgumentException if the provided ID is less than 0.
     */
    public R1 {
        if (id < 0) throw new IllegalArgumentException("ID cannot be negative");
    }
    /**
     * A static final integer constant representing the maximum allowed ID.
     */
    public static final int MAX_ID = 100;
    /**
     * Returns a formatted string representation of the record's ID and name.
     * @return A string in the format "id: name".
     */
    public String getDisplay() { return id + ": " + name; }
}

