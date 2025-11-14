package io.github.grimch.doclet.sample_module.types.utility;

/**
 * Represents a package-private utility class that is {@link Deprecated}.
 * This class demonstrates a static package-private field and a synchronized method.
 * It is intended for internal use within its package.
 * @deprecated This class is deprecated and should not be used.
 */
@Deprecated
class C5 {
    /**
     * A static package-private integer field.
     */
    static int PACKAGE_PRIVATE_FIELD = 5;

    /**
     * A public synchronized method.
     * This method ensures thread-safe execution.
     */
    public synchronized void m15() {
        // Implementation
    }
}
