enum(
    'E1',
    'io.github.grimch.doclet.sample_module.types.basic',
    [
        modifier(public),
        modifier(final)
    ],
    [],
    [
        method(
            values,
            [
                modifier(public),
                modifier(static)
            ],
            [],
            type(
                array,
                declared_type('io.github.grimch.doclet.sample_module.types.basic.E1', [])
            ),
            [],
            [],
            [],
            ''
        ),
        method(
            valueOf,
            [
                modifier(public),
                modifier(static)
            ],
            [],
            declared_type('io.github.grimch.doclet.sample_module.types.basic.E1', []),
            [
                parameter(
                    name,
                    declared_type('java.lang.String', []),
                    [],
                    []
                )
            ],
            [],
            [],
            ''
        ),
        field(
            code,
            [
                modifier(private),
                modifier(final)
            ],
            type(primitive, int),
            [],
            ' A private final integer field representing the code associated with each enum constant.\n'
        ),
        field(
            description,
            [
                modifier(public)
            ],
            declared_type('java.lang.String', []),
            [],
            ' A public string field providing a description for the enum value.\n'
        ),
        constructor(
            '<init>',
            [
                modifier(private)
            ],
            [],
            [],
            [],
            [],
            ' Default constructor for enum constants without explicit arguments.\n Initializes the code to 0.\n'
        ),
        constructor(
            '<init>',
            [
                modifier(private)
            ],
            [],
            [
                parameter(
                    code,
                    type(primitive, int),
                    [],
                    []
                )
            ],
            [],
            [],
            ' Constructor for enum constants with a specific code.\n @param code The integer code to associate with this enum constant.\n'
        ),
        method(
            getCode,
            [
                modifier(public)
            ],
            [],
            type(primitive, int),
            [],
            [],
            [],
            ' Returns the code associated with this enum constant.\n @return The integer code.\n'
        )
    ],
    [],
    ' Represents a basic public enum demonstrating enum constants, fields, and constructors.\n'
).
