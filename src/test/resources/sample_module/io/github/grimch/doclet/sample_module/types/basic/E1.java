package io.github.grimch.doclet.sample_module.types.basic;

// *** Public Enum (Basic) ***
public enum E1 {
    VALUE_A,
    VALUE_B(10);
    private final int code;
    public String description = "Enum value";
    E1() { this.code = 0; }
    E1(int code) { this.code = code; }
    public int getCode() { return code; }
}
