/*
 * This file is part of LogiDoclet.
 *
 * Copyright (c) 2025 The LogiDoclet Authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.grimch.doclet;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;

import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic;
import javax.tools.DocumentationTool;

import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;


public class LogiDoclet implements Doclet {
    private Reporter reporter;
    private boolean prettyPrint = false;
    private Path outputDirectory;

    /**
     * Initializes the doclet with the given locale and reporter.
     * This method is called by the javadoc tool to set up the doclet.
     *
     * @param locale   The locale to use for any output.
     * @param reporter The reporter to use for printing messages and errors.
     */
    @Override
    public void init(Locale locale, Reporter reporter) {
        this.reporter = reporter;
        reporter.print(Diagnostic.Kind.NOTE, "LogiDoclet initialized.");
    }

    /**
     * Returns the name of this doclet.
     *
     * @return The string "LogiDoclet".
     */
    @Override
    public String getName() {
        return "LogiDoclet";
    }

    /**
     * Returns the set of supported options for this doclet.
     * This doclet supports the standard {@code -d} option for specifying the output directory
     * and a custom {@code -outputMode} flag to control the output mode (minimal/full/both.
     * Additionally you can provide {@code -prettyPrint} flag to get output well formatted instead of single line.
     *
     * @return A set of supported {@link Doclet.Option}s.
     */
    @Override
    public Set<? extends Doclet.Option> getSupportedOptions() {
        return Set.of(
            new Option() {
                @Override
                public int getArgumentCount() {
                    return 1;
                }

                @Override
                public String getDescription() {
                    return "Output directory for Prolog facts.";
                }

                @Override
                public Option.Kind getKind() {
                    return Option.Kind.STANDARD;
                }

                @Override
                public java.util.List<String> getNames() {
                    return java.util.List.of("-d");
                }

                @Override
                public String getParameters() {
                    return "<directory>";
                }

                @Override
                public boolean process(String option, java.util.List<String> arguments) {
                    if (arguments != null && arguments.size() == 1) {
                        outputDirectory = Paths.get(arguments.get(0));
                        return true;
                    }
                    reporter.print(Diagnostic.Kind.ERROR, "Option -d requires a directory argument.");
                    return false;
                }
            },

            new Option() { // New Option for prettyPrint
                @Override
                public int getArgumentCount() { return 1; }

                @Override
                public String getDescription() {
                    return "Format Prolog output.";
                }

                @Override
                public Option.Kind getKind() {
                    return Option.Kind.STANDARD;
                }

                @Override
                public java.util.List<String> getNames() {
                    return java.util.List.of("-prettyPrint");
                }

                @Override
                public String getParameters() {
                    return "<boolean>"; // No parameters
                }

                @Override
                public boolean process(String option, java.util.List<String> arguments) {
                    if (arguments != null && arguments.size() == 1) {
                        prettyPrint = Boolean.valueOf(arguments.get(0));
                        return true;
                    }
                    reporter.print(Diagnostic.Kind.ERROR, "Option -prettyPrint requires a boolean argument.");
                    return false;
                }
            },

            new Option() { // -no-fonts option to please Gradle
                @Override
                public int getArgumentCount() { return 0; }

                @Override
                public String getDescription() { return "-no-fonts dummy for Gradle"; }

                @Override
                public Option.Kind getKind() { return Doclet.Option.Kind.STANDARD; }

                @Override
                public java.util.List<String> getNames() { return java.util.List.of("-no-fonts"); }

                @Override
                public String getParameters() { return ""; }

                @Override
                public boolean process(String option, java.util.List<String> arguments) {
                    reporter.print(Diagnostic.Kind.WARNING, "ignoring option: " + option);
                    return true;
                }
            }
        );
    }

    /**
     * Returns the supported source version.
     *
     * @return The latest supported {@link SourceVersion}.
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_17;
    }

    @Override
    public boolean run(DocletEnvironment environment) {
        DocWriter docWriter =
            new JsonDocWriter.Builder(outputDirectory)
                .prettyPrint(prettyPrint)
                .build();

        reporter.print(Diagnostic.Kind.NOTE, "Generating javadoc to: " + DocumentationTool.Location.DOCUMENTATION_OUTPUT);
        new ElementVisitor(environment, reporter, docWriter).startVisit();
        return true;
    }
}

