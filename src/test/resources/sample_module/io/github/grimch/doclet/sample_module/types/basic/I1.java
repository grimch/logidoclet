package io.github.grimch.doclet.sample_module.types.basic;

// *** Interface with Generics and Wildcards ***
// Note: This interface is now non-sealed to participate in the sealed hierarchy test.
public sealed interface I1<T extends Number, U> permits C1, io.github.grimch.doclet.sample_module.types.advanced.C4 {
    void m1(T arg1);
    default U m2(U arg2) { return arg2; }
    static <V> void m3(V arg3) { System.out.println(arg3); }
}