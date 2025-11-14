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
