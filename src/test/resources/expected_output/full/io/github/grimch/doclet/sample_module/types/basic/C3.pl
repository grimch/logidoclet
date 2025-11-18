class(
    'C3',
    'io.github.grimch.doclet.sample_module.types.basic',
    [
        modifier(public),
        modifier(final)
    ],
    [],
    extends(
        declared,
        declared_type(
            'io.github.grimch.doclet.sample_module.types.basic.C2',
            [
                declared_type('io.github.grimch.doclet.sample_module.types.basic.C1', [])
            ]
        )
    ),
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
            ' Private constructor for C3.\n This prevents direct instantiation from outside the class.\n'
        ),
        method(
            m7,
            [
                modifier(protected)
            ],
            [],
            type(no_type, void),
            [],
            [],
            [
                annotation('java.lang.Override', [])
            ],
            ' Implements the abstract method {@code m7()} from {@link C2}.\n'
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
            ' A static method that hides the {@code hiddenStaticMethod()} in {@link C2}.\n This method prints a message indicating it''s from C3.\n'
        ),
        method(
            m9,
            [
                modifier(public)
            ],
            [],
            type(no_type, void),
            [],
            [
                throws(
                    declared_type('java.lang.NullPointerException', [])
                )
            ],
            [],
            ' A public method that throws an unchecked {@link NullPointerException}.\n\n @throws NullPointerException always, as it''s explicitly thrown.\n'
        )
    ],
    [],
    ' Represents a final class that extends {@link C2} with {@link C1} as its type argument.\n As a final class, it cannot be subclassed further.\n This class demonstrates implementing an abstract method from its superclass and method hiding.\n\n @see C2\n @see C1\n'
).
