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
package io.github.grimch.doclet.prolog;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Manages the writing of Prolog facts to the file system.
 * <p>
 * This class is responsible for creating the necessary directory structure that mirrors the Java package
 * hierarchy and writing the generated {@link Fact} objects into {@code .pl} files. It ensures that
 * different kinds of declarations (module, package, type) are written to their appropriate locations.
 *
 * @see Fact
 * @see PrologVisitor
 */
public class DocletPrologWriter {

    private final Path outputDirectory;
    private final boolean prettyPrint;
    private final PrettyPrinter prettyPrinter = new PrettyPrinter();

    /**
     * Constructs a new writer that will output files to the specified base directory.
     *
     * @param outputDirectory The root directory where the Prolog files and their
     *                        directory structure will be created.
     * @param prettyPrint     PrettyPrint flag which enables formatted, indented output.
     */
    public DocletPrologWriter(Path outputDirectory, boolean prettyPrint) {
        this.outputDirectory = outputDirectory;
        this.prettyPrint = prettyPrint;
    }

    /**
     * Writes the main index file for the entire documentation run.
     * This file typically contains a fact that lists all the modules or packages processed.
     *
     * @param indexFact The top-level fact for the index.
     * @throws IOException If an I/O error occurs while writing the file.
     */
    public void writeIndexFile(Fact indexFact) throws IOException {
        writeFactToFile("", "index", indexFact);
    }

    /**
     * Writes a summary file for a Java module.
     * The file will be named {@code module.pl} and placed in a directory corresponding
     * to the module's name.
     *
     * @param moduleName The name of the module (e.g., "java.base").
     * @param moduleFact The fact representing the module's declaration and contents.
     */
    public void writeModuleSummaryFile(String moduleName, Fact moduleFact)  {
        writeFactToFile(moduleName, "module", moduleFact);
    }

    /**
     * Writes a summary file for a Java package.
     * The file will be named {@code package.pl} and placed in a directory structure
     * that mirrors the package name.
     *
     * @param packageName The fully qualified name of the package (e.g., "java.util").
     * @param packageFact The fact representing the package's declaration.
     */
    public void writePackageSummaryFile(String packageName, Fact packageFact)  {
        writeFactToFile(packageName, "package", packageFact);
    }

    /**
     * Writes a file for a specific Java type (class, interface, etc.).
     * The file will be named after the type (e.g., {@code String.pl}) and placed in a
     * directory structure that mirrors its package.
     *
     * @param packageName The fully qualified name of the package containing the type.
     * @param typeName    The simple name of the type (e.g., "String").
     * @param typeDeclarationFact The fact representing the type's declaration and members.
     */
    public void writeTypeFile(String packageName, String typeName, Fact typeDeclarationFact) {
        writeFactToFile(packageName, typeName, typeDeclarationFact);
    }

    /**
     * Private helper to write a fact to a file within a specified hierarchical directory.
     *
     * @param hierarchy The dot-separated path (like a package name) for the directory structure.
     * @param fileName  The base name of the file to write (without the .pl extension).
     * @param fact      The fact to write to the file.
     */
    private void writeFactToFile(String hierarchy, String fileName,  Fact fact) {
        Path fileDir = outputDirectory.resolve(hierarchy.replace('.', '/'));
        writeFactToFile(fileDir, fileName, fact);
    }

    /**
     * Core file-writing method. It creates the necessary directories and writes the
     * fact to the specified file.
     *
     * @param fileDir  The directory where the file should be saved.
     * @param fileName The base name of the file (without extension).
     * @param fact     The fact to be written.
     * @throws RuntimeException if an {@link IOException} occurs during file operations.
     */
    private void writeFactToFile(Path fileDir, String fileName,  Fact fact) {
        try {
            Files.createDirectories(fileDir);
            Path factFilePath = fileDir.resolve(fileName + ".pl");

            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(factFilePath))) {
                // Write the main type declaration fact, terminated by a period.
                writer.println(prettyPrint ? prettyPrinter.prettyPrint(fact) : fact.toString() + ".");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

