class(
    'C1',
    'io.github.grimch.doclet.sample_module.types.basic',
    [
        modifier(public),
        modifier('non-sealed')
    ],
    [],
    'null',
    [
        implements(
            declared,
            declared_type(
                'io.github.grimch.doclet.sample_module.types.basic.I1',
                [
                    declared_type('java.lang.Integer', []),
                    declared_type('java.lang.String', [])
                ]
            )
        ),
        implements(
            declared,
            declared_type('java.io.Serializable', [])
        )
    ],
    [],
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
        method(
            innerMethod,
            [
                modifier(public)
            ],
            [],
            type(no_type, void),
            [],
            [],
            [],
            ' A method within the inner class that accesses the outer class''s field.\n'
        ),
        field(
            'PUBLIC_STATIC_FINAL_STRING',
            [
                modifier(public),
                modifier(static),
                modifier(final)
            ],
            declared_type('java.lang.String', []),
            [],
            ' A public static final string constant.\n'
        ),
        field(
            publicInt,
            [
                modifier(public)
            ],
            type(primitive, int),
            [],
            ' A public integer field.\n'
        ),
        field(
            protectedVolatileDouble,
            [
                modifier(protected),
                modifier(volatile)
            ],
            type(primitive, double),
            [],
            ' A protected volatile double field.\n'
        ),
        field(
            privateFinalBoolean,
            [
                modifier(private),
                modifier(final)
            ],
            type(primitive, boolean),
            [],
            ' A private final boolean field, initialized to true.\n'
        ),
        field(
            transientShort,
            [
                modifier(transient)
            ],
            type(primitive, short),
            [],
            ' A transient short field.\n'
        ),
        field(
            staticChar,
            [
                modifier(static)
            ],
            type(primitive, char),
            [],
            ' A static character field, initialized to ''Z''.\n'
        ),
        field(
            publicStringArray,
            [
                modifier(public)
            ],
            type(
                array,
                declared_type('java.lang.String', [])
            ),
            [],
            ' A public array of strings.\n'
        ),
        field(
            publicGenericList,
            [
                modifier(public)
            ],
            declared_type(
                'java.util.List',
                [
                    declared_type('java.lang.Integer', [])
                ]
            ),
            [
                annotation('io.github.grimch.doclet.sample_module.types.basic.TestAnno', [])
            ],
            ' A public generic list of Integers, annotated with {@link TestAnno}.\n'
        ),
        field(
            privateSelfReference,
            [
                modifier(private),
                modifier(final)
            ],
            declared_type('io.github.grimch.doclet.sample_module.types.basic.C1', []),
            [],
            ' A private final self-referencing instance of C1.\n'
        ),
        field(
            protectedWildcardMap,
            [
                modifier(protected)
            ],
            declared_type(
                'java.util.Map',
                [
                    type(
                        wildcard_extends,
                        declared_type('java.lang.Number', [])
                    ),
                    type(
                        wildcard_super,
                        declared_type('java.lang.String', [])
                    )
                ]
            ),
            [],
            ' A protected map with wildcard types for keys and values.\n Keys extend {@link Number} and values are super of {@link String}.\n'
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
            ' Public canonical constructor for C1.\n Initializes default values for fields.\n'
        ),
        constructor(
            '<init>',
            [
                modifier(protected)
            ],
            [],
            [
                parameter(
                    id,
                    type(primitive, int),
                    [],
                    []
                )
            ],
            [],
            [],
            ' Overloaded protected constructor for C1.\n @param id An integer identifier to initialize {@link #publicInt}.\n'
        ),
        constructor(
            '<init>',
            [],
            [],
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
            ' Package-private constructor for C1.\n @param name A string name (usage not specified in implementation).\n'
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
                    declared_type('java.lang.Integer', []),
                    [],
                    []
                )
            ],
            [],
            [
                annotation('java.lang.Override', [])
            ],
            ' Implements the {@code m1} method from the {@link I1} interface.\n @param arg1 An Integer argument as specified by the interface.\n'
        ),
        method(
            m4,
            [
                modifier(public)
            ],
            [],
            type(no_type, void),
            [],
            [
                throws(
                    declared_type('java.io.IOException', [])
                )
            ],
            [
                annotation('java.lang.Deprecated', [])
            ],
            ' A public method demonstrating a checked exception.\n This method is {@link Deprecated}.\n @throws java.io.IOException if an I/O error occurs.\n @deprecated This method is deprecated and should not be used.\n'
        ),
        method(
            getList,
            [
                modifier(public)
            ],
            [],
            declared_type(
                'java.util.List',
                [
                    declared_type('java.lang.String', [])
                ]
            ),
            [
                parameter(
                    input,
                    declared_type(
                        'java.util.List',
                        [
                            declared_type('java.lang.String', [])
                        ]
                    ),
                    [],
                    []
                )
            ],
            [],
            [
                annotation('io.github.grimch.doclet.sample_module.types.basic.TestAnno', [])
            ],
            ' Demonstrates a method with a receiver parameter annotation and a local record declaration.\n\n @param input A list of strings to be processed.\n @return The input list of strings.\n'
        ),
        method(
            m5,
            [
                modifier(public),
                modifier(static),
                modifier(synchronized)
            ],
            [],
            type(no_type, void),
            [],
            [],
            [],
            ' A static synchronized method.\n This method performs a synchronized operation.\n'
        )
    ],
    [],
    ' Represents a basic public non-sealed class demonstrating various field types,\n constructors, methods, and nested types. It implements the {@link I1} interface\n with specific type arguments and {@link java.io.Serializable}.\n <p>\n This class is part of a sealed hierarchy, permitting specific subclasses.\n </p>\n @see I1\n @see java.io.Serializable\n @see C2\n'
).
