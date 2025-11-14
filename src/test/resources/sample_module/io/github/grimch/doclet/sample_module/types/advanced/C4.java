package io.github.grimch.doclet.sample_module.types.advanced;

import io.github.grimch.doclet.sample_module.types.basic.C1;
import io.github.grimch.doclet.sample_module.types.basic.I1;

import java.io.FileNotFoundException;
import java.util.List;


/**
 * Represents a non-sealed class implementing multiple interfaces,
 * demonstrating various field types, methods, and nested types.
 * It implements {@link I2} and a generic {@link I1} interface.
 * <p>
 * This class is part of a sealed hierarchy, allowing it to be a direct implementation of {@link I1}.
 * </p>
 * @see I2
 * @see I1
 * @see C1
 */
public non-sealed class C4 implements I2, I1<Double, Boolean> {
    // --- FIELDS ---
    /**
     * A package-private static integer field.
     */
    static int PACKAGE_PRIVATE_STATIC_INT = 5;

    /**
     * A protected field referencing an external class {@link C1}.
     */
    protected C1 protectedExternalReference;

    // --- METHODS ---
    /**
     * A protected method that declares multiple checked exceptions.
     * @throws IllegalArgumentException if an illegal argument is provided.
     * @throws java.sql.SQLException if a SQL error occurs.
     */
    protected void m12() throws IllegalArgumentException, java.sql.SQLException {}
    /**
     * A package-private method.
     */
    void m13() {} // Package-private method
    /**
     * A public native method.
     */
    public native void m14();

    // Interface implementations
    /**
     * Implements the {@code m1} method from the {@link I1} interface.
     * @param arg1 A Double argument as specified by the interface.
     */
    @Override
    public void m1(Double arg1) { /* ... */ }

    /**
     * Implements the {@code m2} method from the {@link I1} interface.
     * @param arg2 A Boolean argument as specified by the interface.
     * @return The input argument {@code arg2}.
     */
    @Override
    public Boolean m2(Boolean arg2) { return arg2; }

    /**
     * A generic method with complex bounds and a wildcard type.
     * Implements the {@code m10} method from the {@link I2} interface.
     * @param <K> The type parameter, which must extend {@link Comparable} of itself.
     * @param list A list of numbers, where elements extend {@link Number}.
     * @return A value of type {@code K}.
     * @throws FileNotFoundException if a file is not found.
     */
    public <K extends Comparable<K>> K m10(List<? extends Number> list) throws FileNotFoundException {
        return null;
    }

    // --- NESTED TYPES ---
    /**
     * An inner class within C4.
     */
    class InnerC3 {
        /**
         * A public field referencing the outer instance of {@link C4}.
         */
        public C4 outerInstance = C4.this;
    }

    /**
     * A private static final nested generic class within C4.
     * @param <T> The type parameter for the nested class.
     */
    private static final class NestedGenericC4<T> {
        /**
         * A public field to hold data of type {@code T}.
         */
        public T data;
    }
}
