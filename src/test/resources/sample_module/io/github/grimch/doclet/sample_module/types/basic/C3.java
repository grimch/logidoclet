package io.github.grimch.doclet.sample_module.types.basic;

import java.io.Serializable;

// *** Final Class extending C2 (Must be final, sealed, or non-sealed) ***
public final class C3 extends C2<C1> {

    // Private constructor
    private C3() {}

    // Implementation of abstract method
    @Override
    protected void m7() {
        // Implementation
    }

    // Method hiding the superclass's static method
    public static void hiddenStaticMethod() {
        System.out.println("C3 static (hiding C2's)");
    }

    // Public method with an unchecked exception
    public void m9() throws NullPointerException {
        throw new NullPointerException();
    }
}
