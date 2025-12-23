package io.github.grimch.doclet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.grimch.doclet.element.PackageListDoc;
import io.github.grimch.doclet.element.PackageDoc;
import io.github.grimch.doclet.element.TypeDoc;

import javax.tools.DocumentationTool;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

public class JsonDocWriter implements DocWriter {
    public final static String DEFAULT_PACKAGE_SUMMARY_FILE_NAME = "package";
    public final static String DEFAULT_PACKAGE_INDEX_DOC_FILE_NAME = "package_index";
    public final static String DEFAULT_FILE_SUFFIX = ".json";

    private final JavaFileManager fileManager;
    private final String packageSummaryFileName;
    private final String packageIndexFileName;
    private final String fileSuffix;
    private final boolean prettyPrint;
    private final Gson gson;

    // Private constructor used by the Builder
    private JsonDocWriter(Builder builder) {
        this.fileManager = builder.fileManager;
        this.packageSummaryFileName = builder.packageSummaryFileName;
        this.packageIndexFileName = builder.packageIndexFileName;
        this.fileSuffix = builder.fileSuffix;
        this.prettyPrint = builder.prettyPrint;

        // Initialize Gson based on builder settings
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (this.prettyPrint) {
            gsonBuilder.setPrettyPrinting();
        }
        this.gson = gsonBuilder.create();
    }

    public static class Builder {
        private final JavaFileManager fileManager; // Mandatory
        private String packageSummaryFileName = DEFAULT_PACKAGE_SUMMARY_FILE_NAME;
        private String packageIndexFileName = DEFAULT_PACKAGE_INDEX_DOC_FILE_NAME;
        private String fileSuffix = DEFAULT_FILE_SUFFIX;
        private boolean prettyPrint = false;

        public Builder(JavaFileManager fileManager) {
            this.fileManager = Objects.requireNonNull(fileManager, "FileManager cannot be null");
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
        try {
            FileObject fileObject = fileManager.getFileForOutput(
                DocumentationTool.Location.DOCUMENTATION_OUTPUT,
                packageName,
                String.format("%s.%s", fileName, fileSuffix),
                null
            );
            try (Writer out = fileObject.openWriter()) {
                gson.toJson(doc, out);
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
    public void writeTypeDoc(TypeDoc typeDoc) {
        writeDoc(typeDoc, typeDoc.name(), typeDoc.packageName());
    }
}
