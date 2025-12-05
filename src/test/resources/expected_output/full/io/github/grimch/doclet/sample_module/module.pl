module(
    'io.github.grimch.doclet.sample_module',
    [],
    [
        requires([], 'java.sql', [])
    ],
    [
        exports('io.github.grimch.doclet.sample_module.types.basic', [], []),
        exports('io.github.grimch.doclet.sample_module.types.advanced', [], []),
        exports(
            'io.github.grimch.doclet.sample_module.types.utility',
            [another_module],
            []
        )
    ],
    [],
    [],
    ['io.github.grimch.doclet.sample_module.types.basic', 'io.github.grimch.doclet.sample_module.types.advanced', 'io.github.grimch.doclet.sample_module.types.utility']
).
