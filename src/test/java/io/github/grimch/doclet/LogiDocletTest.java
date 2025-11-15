package io.github.grimch.doclet;

import org.junit.jupiter.api.Test;

import javax.tools.DocumentationTool;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogiDocletTest {

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
                                    .collect(java.util.stream.Collectors.toList());
                            List<String> actualLines = Files.readAllLines(actualFile).stream()
                                    .map(String::trim)
                                    .filter(s -> !s.isEmpty())
                                    .collect(java.util.stream.Collectors.toList());
                            assertEquals(expectedLines, actualLines, "File content mismatch: " + relativePath);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}
