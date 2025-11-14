package io.github.grimch.doclet.sample_module.types.basic;

// *** Generic Record ***
public record R2<X, Y>(X fieldX, Y fieldY) {
    private static final String DEFAULT_TYPE = "GENERIC";
}