class(
    'C4',
    'io.github.grimch.doclet.sample_module.types.advanced',
    [
        modifier(public),
        modifier('non-sealed')
    ],
    [],
    'null',
    [
        implements(
            declared,
            declared_type('io.github.grimch.doclet.sample_module.types.advanced.I2', [])
        ),
        implements(
            declared,
            declared_type(
                'io.github.grimch.doclet.sample_module.types.basic.I1',
                [
                    declared_type('java.lang.Double', []),
                    declared_type('java.lang.Boolean', [])
                ]
            )
        )
    ],
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
        ),
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
            'PACKAGE_PRIVATE_STATIC_INT',
            [
                modifier(static)
            ],
            type(primitive, int),
            [],
            ' A package-private static integer field.\n'
        ),
        field(
            protectedExternalReference,
            [
                modifier(protected)
            ],
            declared_type('io.github.grimch.doclet.sample_module.types.basic.C1', []),
            [],
            ' A protected field referencing an external class {@link C1}.\n'
        ),
        method(
            m12,
            [
                modifier(protected)
            ],
            [],
            type(no_type, void),
            [],
            [
                throws(
                    declared_type('java.lang.IllegalArgumentException', [])
                ),
                throws(
                    declared_type('java.sql.SQLException', [])
                )
            ],
            [],
            ' A protected method that declares multiple checked exceptions.\n @throws IllegalArgumentException if an illegal argument is provided.\n @throws java.sql.SQLException if a SQL error occurs.\n'
        ),
        method(
            m13,
            [],
            [],
            type(no_type, void),
            [],
            [],
            [],
            ' A package-private method.\n'
        ),
        method(
            m14,
            [
                modifier(public),
                modifier(native)
            ],
            [],
            type(no_type, void),
            [],
            [],
            [],
            ' A public native method.\n'
        ),
        method(
            m1,
            [
                modifier(public)
            ],
            [],
            type(no_type, void),
            [
                parameter(
                    arg1,
                    declared_type('java.lang.Double', []),
                    [],
                    []
                )
            ],
            [],
            [
                annotation('java.lang.Override', [])
            ],
            ' Implements the {@code m1} method from the {@link I1} interface.\n @param arg1 A Double argument as specified by the interface.\n'
        ),
        method(
            m2,
            [
                modifier(public)
            ],
            [],
            declared_type('java.lang.Boolean', []),
            [
                parameter(
                    arg2,
                    declared_type('java.lang.Boolean', []),
                    [],
                    []
                )
            ],
            [],
            [
                annotation('java.lang.Override', [])
            ],
            ' Implements the {@code m2} method from the {@link I1} interface.\n @param arg2 A Boolean argument as specified by the interface.\n @return The input argument {@code arg2}.\n'
        ),
        method(
            m10,
            [
                modifier(public)
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
            ' A generic method with complex bounds and a wildcard type.\n Implements the {@code m10} method from the {@link I2} interface.\n @param <K> The type parameter, which must extend {@link Comparable} of itself.\n @param list A list of numbers, where elements extend {@link Number}.\n @return A value of type {@code K}.\n @throws FileNotFoundException if a file is not found.\n'
        )
    ],
    [],
    ' Default constructor.\n'
).
