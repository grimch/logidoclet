package io.github.grimch.doclet.sample_module.types.basic;

import java.io.Serializable;
import java.util.List;

// *** Sealed Abstract Generic Class with Complex Bounds ***
public abstract sealed class C2<T extends C1 & Serializable> permits C3 {
    // --- FIELDS ---
    protected T protectedGenericField;
    public List<T> publicGenericList;

    // --- METHODS ---
    // Protected abstract method
    protected abstract void m7();

    // Generic method with wildcard (super)
    public <L> void m8(java.util.List<? super C1> list) {
        // Implementation
    }

    // Static method for method hiding test
    public static void hiddenStaticMethod() { System.out.println("C2 static"); }
}