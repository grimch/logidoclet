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
import java.util.Locale;
import java.util.Set;

/**
 * A custom Javadoc Doclet that generates a machine-readable Prolog representation of a Java codebase.
 * <p>
 * This doclet traverses the Abstract Syntax Tree (AST) of the Java source code provided by the javadoc tool
 * and generates a set of Prolog facts that describe the structure of the code, including modules, packages,
 * types (classes, interfaces, enums, records), methods, and fields.
 * <p>
 * It supports two modes of operation:
 * <ul>
 *     <li><b>minimal</b>: Generates facts about the code's structure without any Javadoc comments. This provides a
 *     high-level architectural overview.</li>
 *     <li><b>full</b>: Includes Javadoc comments in the generated facts, providing a richer, more detailed
 *     semantic representation.</li>
 * </ul>
 * The output is organized into a directory structure that mirrors the project's package structure, with an
 * index file for easy navigation.
 *
 * @see PrologVisitor
 * @see DocletPrologWriter
 */
public class LogiDoclet implements Doclet {

    private Reporter reporter;
    private Path outputDirectory;
    private boolean outputCommentary = false;

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
     * and a custom {@code -outputCommentary} flag to control whether Javadoc comments are included.
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
                new Option() { // New Option for outputCommentary
                    @Override
                    public int getArgumentCount() {
                        return 0; // No argument needed for a boolean flag
                    }

                    @Override
                    public String getDescription() {
                        return "Include Javadoc comments in the Prolog output.";
                    }

                    @Override
                    public Option.Kind getKind() {
                        return Option.Kind.STANDARD;
                    }

                    @Override
                    public java.util.List<String> getNames() {
                        return java.util.List.of("-outputCommentary");
                    }

                    @Override
                    public String getParameters() {
                        return ""; // No parameters
                    }

                    @Override
                    public boolean process(String option, java.util.List<String> arguments) {
                        if (option.equals("-outputCommentary")) {
                            outputCommentary = true;
                            return true;
                        }
                        return false;
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

        // Copy gemini.md and java_metastructure.pl from resources to the root output directory
        try (
                java.io.InputStream geminiMdStream = LogiDoclet.class.getClassLoader().getResourceAsStream("gemini.md");
                java.io.InputStream masterGeminiTemplateMdStream = LogiDoclet.class.getClassLoader().getResourceAsStream("master_gemini_template.md");
                java.io.InputStream javaMetastructureStream = LogiDoclet.class.getClassLoader().getResourceAsStream("java_metastructure.pl")
        ) {

            if (geminiMdStream == null) {
                reporter.print(Diagnostic.Kind.ERROR, "Resource 'gemini.md' not found in classpath.");
                return false;
            }

            if (masterGeminiTemplateMdStream == null) {
                reporter.print(Diagnostic.Kind.ERROR, "Resource 'master_gemini_template.md' not found in classpath.");
                return false;
            }

            if (javaMetastructureStream == null) {
                reporter.print(Diagnostic.Kind.ERROR, "Resource 'java_metastructure.pl' not found in classpath.");
                return false;
            }

            Files.copy(geminiMdStream, outputDirectory.resolve("gemini.md"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(javaMetastructureStream, outputDirectory.resolve("java_metastructure.pl"), StandardCopyOption.REPLACE_EXISTING);
            reporter.print(Diagnostic.Kind.NOTE, "Copied gemini.md and java_metastructure.pl to " + outputDirectory.toAbsolutePath());
            Path templateDir = outputDirectory.resolve("templates");
            Files.createDirectories(templateDir);
            Files.copy(masterGeminiTemplateMdStream, templateDir.resolve("master_gemini.md.template"), StandardCopyOption.REPLACE_EXISTING);
            reporter.print(Diagnostic.Kind.NOTE, "Copied master_gemini_template.md as master_gemini.md.template to " + templateDir.toAbsolutePath());
        } catch (IOException e) {
            reporter.print(Diagnostic.Kind.ERROR, "Error copying resource files: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        Path actualOutputDirectory;
        if (outputCommentary) {
            actualOutputDirectory = outputDirectory.resolve("full");
        } else {
            actualOutputDirectory = outputDirectory.resolve("minimal");
        }

        reporter.print(Diagnostic.Kind.NOTE, "Generating Prolog facts to: " + actualOutputDirectory.toAbsolutePath());

        DocletPrologWriter writer = new DocletPrologWriter(actualOutputDirectory);
        PrologVisitor visitor = new PrologVisitor(writer, environment, reporter, outputCommentary);

        try {
            for (Element element : environment.getIncludedElements()) {
                element.accept(visitor, null);
            }
            writer.writeIndexFile(visitor.getIndex());
            reporter.print(Diagnostic.Kind.NOTE, "Prolog fact generation completed successfully.");
            return true;
        } catch (IOException e) {
            reporter.print(Diagnostic.Kind.ERROR, "Error writing Prolog facts: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
