class(
    'C2',
    'io.github.grimch.doclet.sample_module.types.basic',
    [
        modifier(public),
        modifier(abstract),
        modifier(sealed)
    ],
    [
        type_parameter(
            'T',
            [
                declared_type('io.github.grimch.doclet.sample_module.types.basic.C1', []),
                declared_type('java.io.Serializable', [])
            ],
            []
        )
    ],
    'null',
    [],
    ['io.github.grimch.doclet.sample_module.types.basic.C3'],
    [
        constructor(
            '<init>',
            [
                modifier(protected)
            ],
            [],
            [],
            [],
            [],
            ' Default constructor.\n'
        ),
        field(
            protectedGenericField,
            [
                modifier(protected)
            ],
            type(type_variable, 'T'),
            [],
            ' A protected generic field of type {@code T}.\n'
        ),
        field(
            publicGenericList,
            [
                modifier(public)
            ],
            declared_type(
                'java.util.List',
                [
                    type(type_variable, 'T')
                ]
            ),
            [],
            ' A public generic list of type {@code T}.\n'
        ),
        method(
            m7,
            [
                modifier(protected),
                modifier(abstract)
            ],
            [],
            type(no_type, void),
            [],
            [],
            [],
            ' A protected abstract method that must be implemented by concrete subclasses.\n'
        ),
        method(
            m8,
            [
                modifier(public)
            ],
            [
                type_parameter(
                    'L',
                    [
                        declared_type('java.lang.Object', [])
                    ],
                    []
                )
            ],
            type(no_type, void),
            [
                parameter(
                    list,
                    declared_type(
                        'java.util.List',
                        [
                            type(
                                wildcard_super,
                                declared_type('io.github.grimch.doclet.sample_module.types.basic.C1', [])
                            )
                        ]
                    ),
                    [],
                    []
                )
            ],
            [],
            [],
            ' A generic method demonstrating a wildcard type parameter with a ''super'' bound.\n\n @param <L> A generic type parameter for the method.\n @param list A list whose elements are supertypes of {@link C1}.\n'
        ),
        method(
            hiddenStaticMethod,
            [
                modifier(public),
                modifier(static)
            ],
            [],
            type(no_type, void),
            [],
            [],
            [],
            ' A static method used for testing method hiding.\n This method prints a message indicating it''s from C2.\n'
        )
    ],
    [],
    ' Represents a sealed abstract generic class with complex type parameter bounds.\n This class demonstrates the use of generics, abstract methods, and method hiding.\n It is part of a sealed hierarchy, permitting only {@link C3} as a direct subclass.\n\n @param <T> The type parameter, which must extend both {@link C1} and {@link Serializable}.\n @see C1\n @see C3\n @see Serializable\n'
).
