record(
    'R1',
    'io.github.grimch.doclet.sample_module.types.basic',
    [
        modifier(public),
        modifier(final)
    ],
    [],
    [
        implements(
            declared,
            declared_type('java.io.Serializable', [])
        )
    ],
    [
        record_component(
            id,
            type(primitive, int),
            []
        ),
        record_component(
            name,
            declared_type('java.lang.String', []),
            []
        )
    ],
    [
        field(
            id,
            [
                modifier(private),
                modifier(final)
            ],
            type(primitive, int),
            [],
            ''
        ),
        field(
            name,
            [
                modifier(private),
                modifier(final)
            ],
            declared_type('java.lang.String', []),
            [],
            ''
        ),
        constructor(
            '<init>',
            [
                modifier(public)
            ],
            [],
            [
                parameter(
                    id,
                    type(primitive, int),
                    [],
                    []
                ),
                parameter(
                    name,
                    declared_type('java.lang.String', []),
                    [],
                    []
                )
            ],
            [],
            [],
            ' Compact constructor for R1.\n Validates that the ID is not negative.\n @throws IllegalArgumentException if the provided ID is less than 0.\n'
        ),
        field(
            'MAX_ID',
            [
                modifier(public),
                modifier(static),
                modifier(final)
            ],
            type(primitive, int),
            [],
            ' A static final integer constant representing the maximum allowed ID.\n'
        ),
        method(
            getDisplay,
            [
                modifier(public)
            ],
            [],
            declared_type('java.lang.String', []),
            [],
            [],
            [],
            ' Returns a formatted string representation of the record''s ID and name.\n @return A string in the format "id: name".\n'
        ),
        method(
            toString,
            [
                modifier(public),
                modifier(final)
            ],
            [],
            declared_type('java.lang.String', []),
            [],
            [],
            [],
            ''
        ),
        method(
            hashCode,
            [
                modifier(public),
                modifier(final)
            ],
            [],
            type(primitive, int),
            [],
            [],
            [],
            ''
        ),
        method(
            equals,
            [
                modifier(public),
                modifier(final)
            ],
            [],
            type(primitive, boolean),
            [
                parameter(
                    o,
                    declared_type('java.lang.Object', []),
                    [],
                    []
                )
            ],
            [],
            [],
            ''
        ),
        method(
            id,
            [
                modifier(public)
            ],
            [],
            type(primitive, int),
            [],
            [],
            [],
            ''
        ),
        method(
            name,
            [
                modifier(public)
            ],
            [],
            declared_type('java.lang.String', []),
            [],
            [],
            [],
            ''
        )
    ],
    [],
    ' Represents a public record demonstrating record components, a compact constructor,\n a static field, and an instance method. It implements {@link java.io.Serializable}.\n\n @param id The unique identifier for the record.\n @param name The name associated with the record.\n'
).
