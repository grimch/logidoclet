package io.github.grimch.doclet.prolog;

import jdk.javadoc.doclet.DocletEnvironment;

import javax.lang.model.element.*;
import javax.lang.model.type.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Manages the accumulation and writing of Prolog facts to the file system.
 * This class handles the creation of directory structure and the final output
 * of Prolog fact files, ensuring they conform to the specified metadata structure.
 */
public class DocletPrologWriter {

    private final Path outputDirectory;

    // public Record

    // Accumulation maps
    private final Map<String, List<Fact>> moduleFacts = new HashMap<>();       // Module name to list of facts

    public DocletPrologWriter(Path outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void writeIndexFile(Fact indexFact) throws IOException {
        writeFactToFile("", "index", indexFact);
    }

    public void writeModuleSummaryFile(String moduleName, Fact moduleFact)  {
        writeFactToFile(moduleName, "module", moduleFact);
    }

    public void writePackageSummaryFile(String packageName, Fact packageFact)  {
        writeFactToFile(packageName, "package", packageFact);
    }

    public void writeTypeFile(String packageName, String typeName, Fact typeDeclarationFact) {
        writeFactToFile(packageName, typeName, typeDeclarationFact);
    }

    private void writeFactToFile(String hierarchy, String fileName,  Fact fact) {
        Path fileDir = outputDirectory.resolve(hierarchy.replace('.', '/'));
        writeFactToFile(fileDir, fileName, fact);
    }

    private void writeFactToFile(Path fileDir, String fileName,  Fact fact) {
        try {
            Files.createDirectories(fileDir);
            Path typeFilePath = fileDir.resolve(fileName + ".pl");

            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(typeFilePath))) {
                // Write the main type declaration fact
                writer.println(fact.toString() + ".");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
