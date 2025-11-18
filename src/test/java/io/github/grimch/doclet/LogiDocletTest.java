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

import org.junit.jupiter.api.BeforeAll;
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
    private static Path outputDir;

    /**
     *
     * Sets up a clean output directory.</li>
     *
     * @throws IOException if an error occurs during file I/O operations (e.g., reading or deleting files).
     */
    @BeforeAll
    public static void cleanOutputDir() throws IOException {
        outputDir = Paths.get("target/test-output");
        if (Files.exists(outputDir)) {
            try (Stream<Path> files = Files.walk(outputDir)) {
                files.sorted(java.util.Comparator.reverseOrder()).map(Path::toFile).forEach(java.io.File::delete);
            }
        }

    }

    /**
     * Executes the {@link LogiDoclet} on a sample project and verifies its output.
     * <p>
     * The test performs the following steps:
     * <ol>
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
    public void testMinimalOutput() throws IOException {
        String[] args = {
                "-verbose",
                "-doclet", LogiDoclet.class.getName(),
                "-d", outputDir.toString(),
                "--source-path", "src/test/resources/sample_module",
                "-subpackages",  "io.github.grimch.doclet.sample_module"
        };
        testDoclet(args, "minimal");
    }

    /**
     * Executes the {@link LogiDoclet} on a sample java class to get formatted output and verifies the same.
     * <p>
     * The test performs the following steps:
     * <ol>
     *     <li>Invokes the system's {@link DocumentationTool} (javadoc) with the {@code LogiDoclet}.</li>
     *     <li>Specifies the source path to the sample class to process.</li>
     *     <li>Asserts that the javadoc tool execution completes successfully (exit code 0).</li>
     *     <li>Compares the expected output  file line-by-line with its corresponding actual generated file.</li>
     *     <li>Asserts that the contents of the actual and expected file are identical.</li>
     * </ol>
     *
     * @throws IOException if an error occurs during file I/O operations (e.g., reading or deleting files).
     */
    @Test
    public void testFormattedOutput() throws IOException {
        String[] args = {
                "-verbose",
                "-doclet", LogiDoclet.class.getName(),
                "-d", outputDir.toString(),
                "-outputCommentary",
                "-prettyPrint",
                "--source-path", "src/test/resources/sample_module",
                "-subpackages",  "io.github.grimch.doclet.sample_module"
        };
        testDoclet(args, "full");
    }

    /**
     * Executes the {@link LogiDoclet} on a sample project and verifies its output .
     * <p>
     * The test performs the following steps:
     * <ol>
     *     <li>Invokes the system's {@link DocumentationTool} (javadoc) with the {@code LogiDoclet}.</li>
     *     <li>Specifies the source path to a sample module and the packages to process.</li>
     *     <li>Asserts that the javadoc tool execution completes successfully (exit code 0).</li>
     *     <li>Recursively walks the directory of expected Prolog files and compares each file
     *         line-by-line with its corresponding actual generated file.</li>
     *     <li>Asserts that the contents of the actual and expected files are identical.</li>
     * </ol>
     *
     * @param args  The arguments to pass to the javadoc.
     * @param mode The javadoc execution mode:
     * <ol>
     *     <li>minimal: Output as single line without comments.</li>
     *     <li>full: Formatted output including  comments.</li>
     * </ol>
     * @throws IOException if an error occurs during file I/O operations (e.g., reading or deleting files).
     */
    private void testDoclet(String[] args, String mode) throws IOException {
        DocumentationTool tool = ToolProvider.getSystemDocumentationTool();
        int result = tool.run(null, null, null, args);
        assertEquals(0, result, "Javadoc tool execution failed");

        Path expectedDir = Paths.get("src/test/resources/expected_output/" + mode);
        Path actualDir = outputDir.resolve(mode);

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

