package io.github.grimch.doclet.sample_module.types.basic;

/**
 * Represents a basic public enum demonstrating enum constants, fields, and constructors.
 */
public enum E1 {
    /**
     * The first enum constant, with a default code of 0.
     */
    VALUE_A,
    /**
     * The second enum constant, initialized with a specific code.
     */
    VALUE_B(10);

    /**
     * A private final integer field representing the code associated with each enum constant.
     */
    private final int code;
    /**
     * A public string field providing a description for the enum value.
     */
    public String description = "Enum value";

    /**
     * Default constructor for enum constants without explicit arguments.
     * Initializes the code to 0.
     */
    E1() { this.code = 0; }

    /**
     * Constructor for enum constants with a specific code.
     * @param code The integer code to associate with this enum constant.
     */
    E1(int code) { this.code = code; }

    /**
     * Returns the code associated with this enum constant.
     * @return The integer code.
     */
    public int getCode() { return code; }
}