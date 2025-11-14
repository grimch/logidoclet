package io.github.grimch.doclet.sample_module.types.basic;

import java.io.Serializable;

/**
 * Represents a final class that extends {@link C2} with {@link C1} as its type argument.
 * As a final class, it cannot be subclassed further.
 * This class demonstrates implementing an abstract method from its superclass and method hiding.
 *
 * @see C2
 * @see C1
 */
public final class C3 extends C2<C1> {

    /**
     * Private constructor for C3.
     * This prevents direct instantiation from outside the class.
     */
    private C3() {}

    /**
     * Implements the abstract method {@code m7()} from {@link C2}.
     */
    @Override
    protected void m7() {
        // Implementation
    }

    /**
     * A static method that hides the {@code hiddenStaticMethod()} in {@link C2}.
     * This method prints a message indicating it's from C3.
     */
    public static void hiddenStaticMethod() {
        System.out.println("C3 static (hiding C2's)");
    }

    /**
     * A public method that throws an unchecked {@link NullPointerException}.
     *
     * @throws NullPointerException always, as it's explicitly thrown.
     */
    public void m9() throws NullPointerException {
        throw new NullPointerException();
    }
}