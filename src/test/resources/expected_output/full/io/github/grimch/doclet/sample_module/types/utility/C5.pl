class(
    'C5',
    'io.github.grimch.doclet.sample_module.types.utility',
    [],
    [],
    'null',
    [],
    [],
    [
        constructor('<init>', [], [], [], [], [], ''),
        field(
            'PACKAGE_PRIVATE_FIELD',
            [
                modifier(static)
            ],
            type(primitive, int),
            [],
            ' A static package-private integer field.\n'
        ),
        method(
            m15,
            [
                modifier(public),
                modifier(synchronized)
            ],
            [],
            type(no_type, void),
            [],
            [],
            [],
            ' A public synchronized method.\n This method ensures thread-safe execution.\n'
        )
    ],
    [
        annotation('java.lang.Deprecated', [])
    ],
    ' Represents a package-private utility class that is {@link Deprecated}.\n This class demonstrates a static package-private field and a synchronized method.\n It is intended for internal use within its package.\n @deprecated This class is deprecated and should not be used.\n'
).
