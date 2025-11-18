interface(
    'I2',
    'io.github.grimch.doclet.sample_module.types.advanced',
    [
        modifier(public),
        modifier(abstract)
    ],
    [],
    [],
    [
        method(
            m10,
            [
                modifier(public),
                modifier(abstract)
            ],
            [
                type_parameter(
                    'K',
                    [
                        declared_type(
                            'java.lang.Comparable',
                            [
                                type(type_variable, 'K')
                            ]
                        )
                    ],
                    []
                )
            ],
            type(type_variable, 'K'),
            [
                parameter(
                    list,
                    declared_type(
                        'java.util.List',
                        [
                            type(
                                wildcard_extends,
                                declared_type('java.lang.Number', [])
                            )
                        ]
                    ),
                    [],
                    []
                )
            ],
            [
                throws(
                    declared_type('java.io.FileNotFoundException', [])
                )
            ],
            [],
            ' A generic method with bounds and a wildcard type.\n\n @param <K> The type parameter, which must extend {@link Comparable} of itself.\n @param list A list of numbers, where elements extend {@link Number}.\n @return A value of type {@code K}.\n @throws FileNotFoundException if a file is not found.\n'
        ),
        method(
            m11,
            [
                modifier(private)
            ],
            [],
            type(no_type, void),
            [],
            [],
            [],
            ' A private method within the interface (Java 9+ feature).\n This method is intended for internal use by default or static methods within this interface.\n'
        )
    ],
    [],
    [],
    ' Represents a non-sealed interface demonstrating a generic method with complex bounds\n and a private interface method (Java 9+ feature).\n'
).
