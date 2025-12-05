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

import io.github.grimch.doclet.prolog.DocletPrologWriter;
import io.github.grimch.doclet.prolog.PrologVisitor;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * A custom Javadoc Doclet that generates a machine-readable Prolog representation of a Java codebase.
 * <p>
 * This doclet traverses the Abstract Syntax Tree (AST) of the Java source code provided by the javadoc tool
 * and generates a set of Prolog facts that describe the structure of the code, including modules, packages,
 * types (classes, interfaces, enums, records), methods, and fields.
 * <p>
 * It supports three modes of operation:
 * <ul>
 *     <li><b>minimal</b>: Generates facts about the code's structure without any Javadoc comments. This provides a
 *     high-level architectural overview.</li>
 *     <li><b>full</b>: Includes Javadoc comments in the generated facts, providing a richer, more detailed
 *     semantic representation.</li>
 *     <li><b>both</b>: This will output both variants outlined above</li>
 * </ul>
 * The output is organized into a directory structure that mirrors the project's package structure, with an
 * index file for easy navigation.
 *
 * Per default output is written to a file in a single line to make tokenization as efficient as possible.
 * If you want to see formatted output set addtional parameter <b>prettyPrint</b>
 *
 * @see PrologVisitor
 * @see DocletPrologWriter
 */
public class LogiDoclet implements Doclet {
    record DocletProcessor(PrologVisitor visitor, DocletPrologWriter writer) {};

    private Reporter reporter;
    private Path outputDirectory;
    private String outputMode = "both";
    private boolean prettyPrint = false;

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
                new Option() { // New Option for outputMode
                    @Override
                    public int getArgumentCount() { return 1; }

                    @Override
                    public String getDescription() {
                        return "Defines the mode of the Prolog output.";
                    }

                    @Override
                    public Option.Kind getKind() {
                        return Option.Kind.STANDARD;
                    }

                    @Override
                    public java.util.List<String> getNames() {
                        return java.util.List.of("-outputMode");
                    }

                    @Override
                    public String getParameters() {
                        return "<minimal>|<full>"; // No parameters
                    }

                    @Override
                    public boolean process(String option, java.util.List<String> arguments) {
                        if (arguments != null && arguments.size() == 1) {
                            outputMode = arguments.get(0);
                            return true;
                        }
                        reporter.print(Diagnostic.Kind.ERROR, "Option -outputMode requires a mode argument.");
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

    /**
     * The main entry point of the doclet, called by the javadoc tool to execute the documentation generation.
     * <p>
     * This method orchestrates the entire process:
     * <ol>
     *     <li>Validates that an output directory has been specified.</li>
     *     <li>Creates the output directory structure.</li>
     *     <li>Copies necessary static resources (e.g., Prolog metastructure files) to the output directory.</li>
     *     <li>Initializes the {@link PrologVisitor} and {@link DocletPrologWriter}.</li>
     *     <li>Iterates over the elements included in the javadoc run (modules, packages, types).</li>
     *     <li>Delegates the processing of each element to the {@link PrologVisitor}.</li>
     *     <li>Writes the final index file containing a summary of all generated Prolog files.</li>
     * </ol>
     *
     * @param environment The environment provided by the javadoc tool, containing all the information
     *                    about the source code being documented.
     * @return {@code true} if the generation was successful, {@code false} otherwise.
     */
    @Override
    public boolean run(DocletEnvironment environment) {
        if (outputDirectory == null) {
            reporter.print(Diagnostic.Kind.ERROR, "Output directory not specified. Use -d option.");
            return false;
        }

        // Ensure the base output directory exists
        try {
            Files.createDirectories(outputDirectory);
        } catch (IOException e) {
            reporter.print(Diagnostic.Kind.ERROR, "Error creating output directory: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        // Copy LLM-context.md and java_metastructure.pl from resources to the root output directory
        try (
                java.io.InputStream llmContextMdStream = LogiDoclet.class.getClassLoader().getResourceAsStream("LLM_context.md");
                java.io.InputStream masterLlmContextTemplateMdStream = LogiDoclet.class.getClassLoader().getResourceAsStream("master_LLM_context_template.md");
                java.io.InputStream javaMetastructureStream = LogiDoclet.class.getClassLoader().getResourceAsStream("java_metastructure.pl")
        ) {

            if (llmContextMdStream == null) {
                reporter.print(Diagnostic.Kind.ERROR, "Resource 'LLM_context.md' not found in classpath.");
                return false;
            }

            if (masterLlmContextTemplateMdStream == null) {
                reporter.print(Diagnostic.Kind.ERROR, "Resource 'master-LLM_context_template.md' not found in classpath.");
                return false;
            }

            if (javaMetastructureStream == null) {
                reporter.print(Diagnostic.Kind.ERROR, "Resource 'java_metastructure.pl' not found in classpath.");
                return false;
            }

            Files.copy(llmContextMdStream, outputDirectory.resolve("LLM_context.md"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(javaMetastructureStream, outputDirectory.resolve("java_metastructure.pl"), StandardCopyOption.REPLACE_EXISTING);
            reporter.print(Diagnostic.Kind.NOTE, "Copied LLM_context.md and java_metastructure.pl to " + outputDirectory.toAbsolutePath());
            Path templateDir = outputDirectory.resolve("templates");
            Files.createDirectories(templateDir);
            Files.copy(masterLlmContextTemplateMdStream, templateDir.resolve("master_LLM_context.md.template"), StandardCopyOption.REPLACE_EXISTING);
            reporter.print(Diagnostic.Kind.NOTE, "Copied master_LLM_context_template.md as master_LLM_context.md.template to " + templateDir.toAbsolutePath());
        } catch (IOException e) {
            reporter.print(Diagnostic.Kind.ERROR, "Error copying resource files: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        List<DocletProcessor> docletProcessors = new ArrayList<>();

        if (outputMode.equals("full") || outputMode.equals("both")) {
            DocletPrologWriter writer = new DocletPrologWriter(outputDirectory.resolve("full"), prettyPrint);
            PrologVisitor visitor = new PrologVisitor(writer, environment, reporter, true);
            docletProcessors.add(new DocletProcessor(visitor, writer));
        }
        if (outputMode.equals("minimal") || outputMode.equals("both")) {
            DocletPrologWriter writer = new DocletPrologWriter(outputDirectory.resolve("minimal"), prettyPrint);
            PrologVisitor visitor = new PrologVisitor(writer, environment, reporter, false);
            docletProcessors.add(new DocletProcessor(visitor, writer));
        }

        reporter.print(Diagnostic.Kind.NOTE, "Generating Prolog facts to: " + outputDirectory.toAbsolutePath());

        try {
            for (Element element : environment.getIncludedElements()) {
                docletProcessors.stream().forEach(docletProcessor -> element.accept(docletProcessor.visitor(), null));
            }
            for (DocletProcessor docletProcessor : docletProcessors) {
                if (docletProcessor.visitor().hasModulesDefined()) {
                    docletProcessor.writer().writeIndexFile(docletProcessor.visitor().getModuleIndex(), "module_index");
                }
                docletProcessor.writer().writeIndexFile(docletProcessor.visitor().getPackageIndex(), "package_index");
            }
            reporter.print(Diagnostic.Kind.NOTE, "Prolog fact generation completed successfully.");
            return true;
        } catch (IOException e) {
            reporter.print(Diagnostic.Kind.ERROR, "Error writing Prolog facts: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

