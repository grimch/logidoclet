/**
 * Defines the sample module for the LogiDoclet project.
 */
module io.github.grimch.doclet.sample_module {
    exports  io.github.grimch.doclet.sample_module.types.basic;
    exports  io.github.grimch.doclet.sample_module.types.advanced;
    exports  io.github.grimch.doclet.sample_module.types.utility;
    requires java.sql;
}