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
 * LogiDoclet is a custom Doclet that traverses Java source elements
 * and outputs Prolog facts representing the code structure.
 */
public class LogiDoclet implements Doclet {

    private Reporter reporter;
    private Path outputDirectory;
    private boolean outputCommentary = false; // New field, default to false

    @Override
    public void init(Locale locale, Reporter reporter) {
        this.reporter = reporter;
        reporter.print(Diagnostic.Kind.NOTE, "LogiDoclet initialized.");
    }

    @Override
    public String getName() {
        return "LogiDoclet";
    }

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

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8; // Using ElementScanner8
    }

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
        try (java.io.InputStream geminiMdStream = LogiDoclet.class.getClassLoader().getResourceAsStream("gemini.md");
             java.io.InputStream javaMetastructureStream = LogiDoclet.class.getClassLoader().getResourceAsStream("java_metastructure.pl")) {

            if (geminiMdStream == null) {
                reporter.print(Diagnostic.Kind.ERROR, "Resource 'gemini.md' not found in classpath.");
                return false;
            }
            if (javaMetastructureStream == null) {
                reporter.print(Diagnostic.Kind.ERROR, "Resource 'java_metastructure.pl' not found in classpath.");
                return false;
            }

            Files.copy(geminiMdStream, outputDirectory.resolve("gemini.md"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(javaMetastructureStream, outputDirectory.resolve("java_metastructure.pl"), StandardCopyOption.REPLACE_EXISTING);
            reporter.print(Diagnostic.Kind.NOTE, "Copied gemini.md and java_metastructure.pl to " + outputDirectory.toAbsolutePath());
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
