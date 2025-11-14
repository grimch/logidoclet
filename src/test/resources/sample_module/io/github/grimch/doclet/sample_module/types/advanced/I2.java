package io.github.grimch.doclet.sample_module.types.advanced;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Represents a non-sealed interface demonstrating a generic method with complex bounds
 * and a private interface method (Java 9+ feature).
 */
public interface I2 {
    /**
     * A generic method with bounds and a wildcard type.
     *
     * @param <K> The type parameter, which must extend {@link Comparable} of itself.
     * @param list A list of numbers, where elements extend {@link Number}.
     * @return A value of type {@code K}.
     * @throws FileNotFoundException if a file is not found.
     */
    <K extends Comparable<K>> K m10(List<? extends Number> list) throws FileNotFoundException;

    /**
     * A private method within the interface (Java 9+ feature).
     * This method is intended for internal use by default or static methods within this interface.
     */
    private void m11() {}
}
