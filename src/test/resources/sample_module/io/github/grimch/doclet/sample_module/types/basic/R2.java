package io.github.grimch.doclet.sample_module.types.basic;

/**
 * Represents a generic record demonstrating generic record components.
 *
 * @param <X> The type for the first field.
 * @param <Y> The type for the second field.
 * @param fieldX The first field of type {@code X}.
 * @param fieldY The second field of type {@code Y}.
 */
public record R2<X, Y>(X fieldX, Y fieldY) {
    /**
     * A private static final string constant representing a default type.
     */
    private static final String DEFAULT_TYPE = "GENERIC";
}
