package io.github.grimch.doclet;

import org.junit.jupiter.api.Test;

import javax.tools.DocumentationTool;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration test for the {@link LogiDoclet}.
 * <p>
 * This test runs the Javadoc tool with the {@code LogiDoclet} enabled on a sample
 * Java module located in the test resources. It then compares the generated Prolog
 * output against a set of expected files to ensure the doclet is producing the
 * correct, structured representation of the source code.
 */
public class LogiDocletTest {

    /**
     * Executes the {@link LogiDoclet} on a sample project and verifies its output.
     * <p>
     * The test performs the following steps:
     * <ol>
     *     <li>Sets up a clean output directory.</li>
     *     <li>Invokes the system's {@link DocumentationTool} (javadoc) with the {@code LogiDoclet}.</li>
     *     <li>Specifies the source path to a sample module and the packages to process.</li>
     *     <li>Asserts that the javadoc tool execution completes successfully (exit code 0).</li>
     *     <li>Recursively walks the directory of expected Prolog files and compares each file
     *         line-by-line with its corresponding actual generated file.</li>
     *     <li>Asserts that the contents of the actual and expected files are identical.</li>
     * </ol>
     *
     * @throws IOException if an error occurs during file I/O operations (e.g., reading or deleting files).
     */
    @Test
    public void testDoclet() throws IOException {
        Path outputDir = Paths.get("target/test-output");
        if (Files.exists(outputDir)) {
            try (Stream<Path> files = Files.walk(outputDir)) {
                files.sorted(java.util.Comparator.reverseOrder()).map(Path::toFile).forEach(java.io.File::delete);
            }
        }
        
        DocumentationTool tool = ToolProvider.getSystemDocumentationTool();
        String[] args = {
                "-verbose",
                "-doclet", LogiDoclet.class.getName(),
                "-d", outputDir.toString(),
//                "-outputCommentary",
                "--source-path", "src/test/resources/sample_module",
                "-subpackages",  "io.github.grimch.doclet.sample_module"
        };
        int result = tool.run(null, null, null, args);
        assertEquals(0, result, "Javadoc tool execution failed");

        Path expectedDir = Paths.get("src/test/resources/expected_output");
        Path actualDir = outputDir.resolve("minimal");

        try (Stream<Path> expectedFiles = Files.walk(expectedDir)) {
            expectedFiles
                    .filter(Files::isRegularFile)
                    .forEach(expectedFile -> {
                        try {
                            Path relativePath = expectedDir.relativize(expectedFile);
                            Path actualFile = actualDir.resolve(relativePath);
                            List<String> expectedLines = Files.readAllLines(expectedFile).stream()
                                    .map(String::trim)
                                    .filter(s -> !s.isEmpty())
                                    .collect(Collectors.toList());
                            List<String> actualLines = Files.readAllLines(actualFile).stream()
                                    .map(String::trim)
                                    .filter(s -> !s.isEmpty())
                                    .collect(Collectors.toList());
                            assertEquals(expectedLines, actualLines, "File content mismatch: " + relativePath);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}
