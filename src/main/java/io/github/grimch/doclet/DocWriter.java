package io.github.grimch.doclet;

import io.github.grimch.doclet.element.PackageDoc;
import io.github.grimch.doclet.element.PackageListDoc;
import io.github.grimch.doclet.element.TypeDoc;

public interface DocWriter {
    public void writePackageListDoc(PackageListDoc packageListDoc);
    public void writePackageDoc(PackageDoc packageDoc);
    public void writeTypeDoc(String packageName, TypeDoc typeDoc);
}
