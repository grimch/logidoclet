package io.github.grimch.doclet.sample_module.types.advanced;

import java.io.FileNotFoundException;
import java.util.List;

// *** Non-Sealed Interface with Generic Method ***
public interface I2 {
    // Generic method with bounds and a wildcard (extends)
    <K extends Comparable<K>> K m10(List<? extends Number> list) throws FileNotFoundException;

    // Private method (Java 9+)
    private void m11() {}
}