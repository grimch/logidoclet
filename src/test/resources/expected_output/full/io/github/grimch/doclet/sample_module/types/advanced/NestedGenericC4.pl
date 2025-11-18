class(
    'NestedGenericC4',
    'io.github.grimch.doclet.sample_module.types.advanced',
    [
        modifier(private),
        modifier(static),
        modifier(final)
    ],
    [
        type_parameter(
            'T',
            [
                declared_type('java.lang.Object', [])
            ],
            []
        )
    ],
    'null',
    [],
    [],
    [
        constructor(
            '<init>',
            [
                modifier(private)
            ],
            [],
            [],
            [],
            [],
            ''
        ),
        field(
            data,
            [
                modifier(public)
            ],
            type(type_variable, 'T'),
            [],
            ' A public field to hold data of type {@code T}.\n'
        )
    ],
    [],
    ' A private static final nested generic class within C4.\n @param <T> The type parameter for the nested class.\n'
).
