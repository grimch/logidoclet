record(
    'R2',
    'io.github.grimch.doclet.sample_module.types.basic',
    [
        modifier(public),
        modifier(final)
    ],
    [
        type_parameter(
            'X',
            [
                declared_type('java.lang.Object', [])
            ],
            []
        ),
        type_parameter(
            'Y',
            [
                declared_type('java.lang.Object', [])
            ],
            []
        )
    ],
    [],
    [
        record_component(
            fieldX,
            type(type_variable, 'X'),
            []
        ),
        record_component(
            fieldY,
            type(type_variable, 'Y'),
            []
        )
    ],
    [
        field(
            fieldX,
            [
                modifier(private),
                modifier(final)
            ],
            type(type_variable, 'X'),
            [],
            ''
        ),
        field(
            fieldY,
            [
                modifier(private),
                modifier(final)
            ],
            type(type_variable, 'Y'),
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
                    fieldX,
                    type(type_variable, 'X'),
                    [],
                    []
                ),
                parameter(
                    fieldY,
                    type(type_variable, 'Y'),
                    [],
                    []
                )
            ],
            [],
            [],
            ''
        ),
        field(
            'DEFAULT_TYPE',
            [
                modifier(private),
                modifier(static),
                modifier(final)
            ],
            declared_type('java.lang.String', []),
            [],
            ' A private static final string constant representing a default type.\n'
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
            fieldX,
            [
                modifier(public)
            ],
            [],
            type(type_variable, 'X'),
            [],
            [],
            [],
            ''
        ),
        method(
            fieldY,
            [
                modifier(public)
            ],
            [],
            type(type_variable, 'Y'),
            [],
            [],
            [],
            ''
        )
    ],
    [],
    ' Represents a generic record demonstrating generic record components.\n\n @param <X> The type for the first field.\n @param <Y> The type for the second field.\n @param fieldX The first field of type {@code X}.\n @param fieldY The second field of type {@code Y}.\n'
).
