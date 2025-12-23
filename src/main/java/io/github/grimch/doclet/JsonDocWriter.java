package io.github.grimch.doclet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.grimch.doclet.element.PackageListDoc;
import io.github.grimch.doclet.element.PackageDoc;
import io.github.grimch.doclet.element.TypeDoc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public class JsonDocWriter implements DocWriter {
    public final static String DEFAULT_PACKAGE_SUMMARY_FILE_NAME = "package";
    public final static String DEFAULT_PACKAGE_INDEX_DOC_FILE_NAME = "package_index";
    public final static String DEFAULT_FILE_SUFFIX = "json";

    private final Path outputDirectory;
    private final String packageSummaryFileName;
    private final String packageIndexFileName;
    private final String fileSuffix;
    private final boolean prettyPrint;
    private final Gson gson;

    // Private constructor used by the Builder
    private JsonDocWriter(Builder builder) {
        this.outputDirectory = builder.outputDirectory;
        this.packageSummaryFileName = builder.packageSummaryFileName;
        this.packageIndexFileName = builder.packageIndexFileName;
        this.fileSuffix = builder.fileSuffix;
        this.prettyPrint = builder.prettyPrint;

        // Initialize Gson based on builder settings
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (this.prettyPrint) {
            gsonBuilder.setPrettyPrinting();
        }
        this.gson = gsonBuilder
            .disableHtmlEscaping()
            .create();
    }

    public static class Builder {
        private String packageSummaryFileName = DEFAULT_PACKAGE_SUMMARY_FILE_NAME;
        private String packageIndexFileName = DEFAULT_PACKAGE_INDEX_DOC_FILE_NAME;
        private String fileSuffix = DEFAULT_FILE_SUFFIX;
        private boolean prettyPrint = false;
        private final Path outputDirectory;

        public Builder(Path outputDirectory) {
            this.outputDirectory = Objects.requireNonNull(outputDirectory, "Output directory cannot be null");
        }

        public Builder packageSummaryFileName(String name) {
            this.packageSummaryFileName = name;
            return this;
        }

        public Builder packageIndexFileName(String name) {
            this.packageIndexFileName = name;
            return this;
        }

        public Builder fileSuffix(String suffix) {
            this.fileSuffix = suffix;
            return this;
        }

        public Builder prettyPrint(boolean prettyPrint) {
            this.prettyPrint = prettyPrint;
            return this;
        }

        public JsonDocWriter build() {
            return new JsonDocWriter(this);
        }
    }

    private void writeDoc(Object doc, String packageName, String fileName) {
        String[] parts = packageName.split("\\.");
        Path packageDir = outputDirectory.resolve(Path.of(parts[0], Arrays.copyOfRange(parts, 1, parts.length)));
        writeDoc(doc, packageDir, fileName);
    }

    private void writeDoc(Object doc, Path packageDir, String fileName) {
        try {
            Files.createDirectories(packageDir);
            Path filePath = packageDir.resolve(String.format("%s.%s", fileName, fileSuffix));
            try (Writer out = new PrintWriter(Files.newBufferedWriter(filePath))) {
                gson.toJson(doc, out);
                System.out.println();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writePackageListDoc(PackageListDoc packageListDoc) {
        writeDoc(packageListDoc, "", packageIndexFileName);
    }
    public void writePackageDoc(PackageDoc packageDoc) {
        writeDoc(packageDoc, packageDoc.name(), packageSummaryFileName);

    }
    public void writeTypeDoc(String packageName, TypeDoc typeDoc) {
        writeDoc(typeDoc, packageName, typeDoc.header().name());
    }
}
