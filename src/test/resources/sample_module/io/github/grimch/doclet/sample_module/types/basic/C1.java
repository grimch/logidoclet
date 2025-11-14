package io.github.grimch.doclet.sample_module.types.basic;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

// *** Basic Public Class implementing I1 with Fields and Nested Types ***
public non-sealed class C1 implements I1<Integer, String>, Serializable {
    // --- FIELDS ---
    // 1. Primitive Fields
    public static final String PUBLIC_STATIC_FINAL_STRING = "Test";
    public int publicInt;
    protected volatile double protectedVolatileDouble;
    private final boolean privateFinalBoolean = true;
    transient short transientShort;
    static char staticChar = 'Z';

    // 2. Array Fields
    public String[] publicStringArray;

    // 3. Complex/Generic Fields with Type Annotation
    public @TestAnno List<Integer> publicGenericList;
    private final C1 privateSelfReference = new C1();
    protected Map<? extends Number, ? super String> protectedWildcardMap;

    // --- CONSTRUCTORS START ---
    // Public canonical constructor
    public C1() {}

    // Overloaded protected constructor
    protected C1(int id) {
        this.publicInt = id;
    }

    // Package-private constructor
    C1(String name) {
        // ...
    }
    // --- CONSTRUCTORS END ---

    // Method overriding m1 from I1
    @Override
    public void m1(Integer arg1) {
        // Implementation
    }

    // Public method with checked exception, now Deprecated
    @Deprecated
    public void m4() throws java.io.IOException {
        // Implementation
    }

    // Method demonstrating a receiver parameter annotation and local declaration
    public @TestAnno List<String> getList(C1 this, List<String> input) {
        // Local Record declaration inside a method
        record LocalR(String value) {}
        LocalR local = new LocalR("Local Test");

        // Use of local variable
        System.out.println(local.value());
        return input;
    }


    // Static synchronized method
    public static synchronized void m5() {
        // Implementation
    }

    // --- NESTED TYPES START ---
    /** 1. Public Static Nested Class **/
    public static class StaticNestedC1 {
        private final int value = 1;
        public int getValue() { return value; }
    }

    /** 2. Protected Non-Static Inner Class **/
    protected class InnerC2 {
        public void innerMethod() {
            System.out.println(publicInt);
        }
    }
    // --- NESTED TYPES END ---
}
