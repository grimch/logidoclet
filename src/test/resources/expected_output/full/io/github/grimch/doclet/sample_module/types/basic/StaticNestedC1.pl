class(
    'StaticNestedC1',
    'io.github.grimch.doclet.sample_module.types.basic',
    [
        modifier(public),
        modifier(static)
    ],
    [],
    'null',
    [],
    [],
    [
        constructor(
            '<init>',
            [
                modifier(public)
            ],
            [],
            [],
            [],
            [],
            ' Default constructor.\n'
        ),
        field(
            value,
            [
                modifier(private),
                modifier(final)
            ],
            type(primitive, int),
            [],
            ' A private final integer value.\n'
        ),
        method(
            getValue,
            [
                modifier(public)
            ],
            [],
            type(primitive, int),
            [],
            [],
            [],
            ' Returns the value of this nested class.\n @return The integer value.\n'
        )
    ],
    [],
    ' A public static nested class within C1.\n'
).
