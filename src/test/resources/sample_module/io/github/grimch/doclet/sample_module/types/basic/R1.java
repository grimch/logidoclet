package io.github.grimch.doclet.sample_module.types.basic;

// *** Public Record ***
public record R1(int id, String name) implements java.io.Serializable {
    public R1 {
        if (id < 0) throw new IllegalArgumentException("ID cannot be negative");
    }
    public static final int MAX_ID = 100;
    public String getDisplay() { return id + ": " + name; }
}