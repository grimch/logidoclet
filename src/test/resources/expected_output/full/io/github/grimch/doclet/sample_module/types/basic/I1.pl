interface(
    'I1',
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
                declared_type('java.lang.Number', [])
            ],
            []
        ),
        type_parameter(
            'U',
            [
                declared_type('java.lang.Object', [])
            ],
            []
        )
    ],
    [],
    [
        method(
            m1,
            [
                modifier(public),
                modifier(abstract)
            ],
            [],
            type(no_type, void),
            [
                parameter(
                    arg1,
                    type(type_variable, 'T'),
                    [],
                    []
                )
            ],
            [],
            [],
            ' An abstract method that takes an argument of type {@code T}.\n @param arg1 The argument of type {@code T}.\n'
        ),
        method(
            m2,
            [
                modifier(public),
                modifier(default)
            ],
            [],
            type(type_variable, 'U'),
            [
                parameter(
                    arg2,
                    type(type_variable, 'U'),
                    [],
                    []
                )
            ],
            [],
            [],
            ' A default method that returns its input argument.\n @param arg2 The argument of type {@code U}.\n @return The input argument {@code arg2}.\n'
        ),
        method(
            m3,
            [
                modifier(public),
                modifier(static)
            ],
            [
                type_parameter(
                    'V',
                    [
                        declared_type('java.lang.Object', [])
                    ],
                    []
                )
            ],
            type(no_type, void),
            [
                parameter(
                    arg3,
                    type(type_variable, 'V'),
                    [],
                    []
                )
            ],
            [],
            [],
            ' A static generic method that prints its argument to the console.\n @param <V> The type parameter for this method.\n @param arg3 The argument of type {@code V}.\n'
        )
    ],
    [],
    [
        declared_type('io.github.grimch.doclet.sample_module.types.basic.C1', []),
        declared_type('io.github.grimch.doclet.sample_module.types.advanced.C4', [])
    ],
    ' Represents a sealed interface demonstrating generics, wildcards, default, and static methods.\n This interface is part of a sealed hierarchy, permitting {@link C1} and {@link io.github.grimch.doclet.sample_module.types.advanced.C4}\n as direct implementations.\n\n @param <T> The first type parameter, which must extend {@link Number}.\n @param <U> The second type parameter.\n @see C1\n @see io.github.grimch.doclet.sample_module.types.advanced.C4\n'
).
