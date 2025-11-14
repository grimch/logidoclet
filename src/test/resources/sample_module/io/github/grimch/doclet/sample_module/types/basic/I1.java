package io.github.grimch.doclet.sample_module.types.basic;

// *** Interface with Generics and Wildcards ***
// Note: This interface is now non-sealed to participate in the sealed hierarchy test.
/**
 * Represents a sealed interface demonstrating generics, wildcards, default, and static methods.
 * This interface is part of a sealed hierarchy, permitting {@link C1} and {@link io.github.grimch.doclet.sample_module.types.advanced.C4}
 * as direct implementations.
 *
 * @param <T> The first type parameter, which must extend {@link Number}.
 * @param <U> The second type parameter.
 * @see C1
 * @see io.github.grimch.doclet.sample_module.types.advanced.C4
 */
public sealed interface I1<T extends Number, U> permits C1, io.github.grimch.doclet.sample_module.types.advanced.C4 {
    /**
     * An abstract method that takes an argument of type {@code T}.
     * @param arg1 The argument of type {@code T}.
     */
    void m1(T arg1);
    /**
     * A default method that returns its input argument.
     * @param arg2 The argument of type {@code U}.
     * @return The input argument {@code arg2}.
     */
    default U m2(U arg2) { return arg2; }
    /**
     * A static generic method that prints its argument to the console.
     * @param <V> The type parameter for this method.
     * @param arg3 The argument of type {@code V}.
     */
    static <V> void m3(V arg3) { System.out.println(arg3); }
}
