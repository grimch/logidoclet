package io.github.grimch.doclet.sample_module.types.advanced;

import io.github.grimch.doclet.sample_module.types.basic.C1;
import io.github.grimch.doclet.sample_module.types.basic.I1;

import java.io.FileNotFoundException;
import java.util.List;



// *** Class implementing multiple interfaces with Fields and Nested Types ***
public non-sealed class C4 implements I2, I1<Double, Boolean> {
    // --- FIELDS ---
    // Package-private field
    static int PACKAGE_PRIVATE_STATIC_INT = 5;

    // Protected field referencing an external class
    protected C1 protectedExternalReference;

    // --- METHODS ---
    protected void m12() throws IllegalArgumentException, java.sql.SQLException {}
    void m13() {} // Package-private method
    public native void m14();

    // Interface implementations
    @Override
    public void m1(Double arg1) { /* ... */ }

    @Override
    public Boolean m2(Boolean arg2) { return arg2; }

    public <K extends Comparable<K>> K m10(List<? extends Number> list) throws FileNotFoundException {
        return null;
    }

    // --- NESTED TYPES ---
    class InnerC3 {
        public C4 outerInstance = C4.this;
    }

    private static final class NestedGenericC4<T> {
        public T data;
    }
}