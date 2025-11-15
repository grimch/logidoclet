package io.github.grimch.doclet.sample_module.types.basic;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a sealed abstract generic class with complex type parameter bounds.
 * This class demonstrates the use of generics, abstract methods, and method hiding.
 * It is part of a sealed hierarchy, permitting only {@link C3} as a direct subclass.
 *
 * @param <T> The type parameter, which must extend both {@link C1} and {@link Serializable}.
 * @see C1
 * @see C3
 * @see Serializable
 */

public abstract sealed class C2<T extends C1 & Serializable> permits C3 {
    /**
     * Default constructor.
     */
    protected C2() {}
    // --- FIELDS ---
    /**
     * A protected generic field of type {@code T}.
     */
    protected T protectedGenericField;
    /**
     * A public generic list of type {@code T}.
     */
    public List<T> publicGenericList;

    // --- METHODS ---
    /**
     * A protected abstract method that must be implemented by concrete subclasses.
     */
    protected abstract void m7();

    /**
     * A generic method demonstrating a wildcard type parameter with a 'super' bound.
     *
     * @param <L> A generic type parameter for the method.
     * @param list A list whose elements are supertypes of {@link C1}.
     */
    public <L> void m8(java.util.List<? super C1> list) {
        // Implementation
    }

    /**
     * A static method used for testing method hiding.
     * This method prints a message indicating it's from C2.
     */
    public static void hiddenStaticMethod() { System.out.println("C2 static"); }
}
