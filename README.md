# LogiDoclet

![Javadoc](https://img.shields.io/badge/Javadoc-orange?style=for-the-badge&logo=java&logoColor=white)
![RAG](https://img.shields.io/badge/RAG-blue?style=for-the-badge)
![Token-Efficient](https://img.shields.io/badge/Token--Efficient-brightgreen?style=for-the-badge)
[![Java 17+](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/grimch/logidoclet)](https://github.com/grimch/logidoclet/releases)
[![GitHub stars](https://img.shields.io/github/stars/grimch/logidoclet)](https://github.com/grimch/logidoclet/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/grimch/logidoclet)](https://github.com/grimch/logidoclet/network)
[![GitHub last commit](https://img.shields.io/github/last-commit/grimch/logidoclet)](https://github.com/grimch/logidoclet/commits/main)
[![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/grimch/logidoclet)](https://github.com/grimch/logidoclet)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

# LogiDoclet: AI-Friendly API Documentation 💡

LogiDoclet is a **Javadoc Doclet** that generates a structured, LLM-friendly representation of your Java codebase. Its purpose is to make software projects instantly accessible and highly token-efficient for AI analysis.

---
# Table of Contents
- [Motivation: Why Traditional Javadoc Fails AI](#motivation-why-traditional-javadoc-fails-ai)
- [Key Usage Scenarios](#key-usage-scenarios)
- [User Guide](#user-guide)
  - [Direct `javadoc` Execution](#direct-javadoc-execution)
  - [Usage with Maven](#usage-with-maven)
  - [Usage with Gradle](#usage-with-gradle)
- [Formatted Prolog Output Example](#formatted-prolog-output-example)
- [Standalone Example Project](#standalone-example-project)
- [Developer Guide](#developer-guide)
  - [Architecture Overview](#architecture-overview)
  - [Project Structure](#project-structure)
  - [Building and Testing](#building-and-testing)
- [License](#license)

___

<!-- TOC --><a name="motivation-why-traditional-javadoc-fails-ai"></a>
## Motivation: Why Traditional Javadoc Fails AI

While human developers rely on traditional Javadoc for API descriptions, this presentation-oriented format is highly inefficient for Large Language Models (LLMs):

* **High Token Cost:** Information is buried within complex HTML, CSS, and scripts, forcing the LLMs to spend vast tokens and processing time merely to extract the code's structure.
* **Low Clarity:** The output is ambiguous and lacks the strict, logical structure AIs prefer.

LogiDoclet addresses these issues by providing a **compact, unambiguous set of Prolog facts** that represent the codebase's API. This makes the documentation **instantly machine-readable**.

---

<!-- TOC --><a name="key-usage-scenarios"></a>
## Key Usage Scenarios

LogiDoclet provides crucial context that standard code assistance tools often miss:

1.  **Closing the Library Context Gap (External Dependencies):**
    * Integrated Development Environments (IDEs) build a robust internal Abstract Syntax Tree (AST) for the code you are currently writing.
    * However, the AST often lacks the deep design context of **external libraries and frameworks** (e.g., Spring, Apache) that you haven't fully used or imported yet—a common scenario during major design or refactoring.
    * **Solution:** We encourage **industry-leading software foundations and platform providers** (such as Google, Spring, and Apache) to publish this AI-friendly documentation alongside their binaries to enable deeper LLM context for any library on your classpath.

2.  **Empowering Command Line AI Tools:**
    * Command Line Interface (CLI) tools, like **Gemini CLI**, operate outside the IDE's environment and cannot access the project's internal AST. They rely on expensive, ad-hoc analysis of raw source files.
    * **Solution:** By providing the structured API facts via LogiDoclet, CLI tools gain immediate access to the entire project's API structure, substantially reducing the need for costly direct source code analysis.
___

<!-- TOC --><a name="user-guide"></a>
## User Guide

There are two primary ways to use LogiDoclet:
* Directly via the `javadoc` command-line tool
* As a plugin in a Gradle / Maven build.

<!-- TOC --><a name="direct-javadoc-execution"></a>
### Direct `javadoc` Execution

To use LogiDoclet, you invoke the standard `javadoc` tool and specify `LogiDoclet` as the doclet. This method is useful for quick analysis or for use in non-Maven projects.

#### Options
*   `-d <directory>`: **(Required)** Specifies the output directory. **It is highly recommended to use a directory outside of `target/`** (e.g., `build/prolog-docs`) to prevent generated files from being deleted by `mvn clean`.
*   `-docletpath <path_to_jar>`: **(Required)** Specifies the path to the LogiDoclet JAR file. You must build the project first (`mvn clean install`) to create this JAR in the `target/` directory.
*   `-outputMode`: (Optional) Defines if the "full" version, that includes Javadoc comments in the Prolog output,  a "minimal" version without comments, or "both" (default) is generated.
*   All other standard `javadoc` options like `--source-path` and `-subpackages` are supported.

#### Example Commands

**Generating docs for a project:**
```bash
# First, build LogiDoclet to create the JAR
# mvn clean install

# Then, run javadoc on your own project
javadoc -doclet io.github.grimch.doclet.LogiDoclet \
        -docletpath 'path/to/logidoclet/target/logidoclet-1.0.0.jar' \
        -d build/prolog-docs \
        --source-path path/to/your/project/src/main/java \
        -subpackages com.your.project.packages
```

**Generating docs for the included sample:**
This command generates Prolog facts for the sample code included in this repository's test resources.
```bash
# Assumes you have built the project with 'mvn clean install, for a Gradle build, the library is located in build/libs directory
javadoc -doclet io.github.grimch.doclet.LogiDoclet \
        -docletpath 'target/logidoclet-1.0.0.jar' \
        -d build/prolog-sample-docs \
        --source-path src/test/resources/sample_module \
        -subpackages io.github.grimch.doclet.sample_module
```

<!-- TOC --><a name="usage-with-maven"></a>
### Usage with Maven

You can integrate LogiDoclet directly into your project's `pom.xml` using the `maven-javadoc-plugin`.

#### Setup
1.  **Install LogiDoclet Locally:** Since it is not yet available on Maven Central. You must build and install it locally first:
    ```bash
    # In the logidoclet project directory
    mvn clean install
    ```
2.  **Configure Your `pom.xml`:** Add the `maven-javadoc-plugin` to your project's `pom.xml` and configure it to use LogiDoclet.

An example `pom.xml` is provided in the [`examples/logidoclet-usage/`](./examples/logidoclet-usage/) directory. You can adapt the `<plugin>` section from this file into your own project.

Key configuration snippet from the example which shows how generate standard javadoc and LogiDoc output in parallel:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <version>${maven.javadoc.plugin.version}</version>
    <configuration>
        <!-- uncomment next two lines for troubleshooting -->
        <debug>true</debug>
        <verbose>true</verbose>
    </configuration>

    <executions>
        <execution>
            <id>default-javadoc</id>
            <phase>compile</phase>
            <goals>
                <goal>javadoc</goal>
            </goals>
            <configuration>
                <classifier>default-javadoc</classifier>
                <outputDirectory>${project.build.directory}/javadoc</outputDirectory>
                <show>private</show>
            </configuration>
        </execution>

        <execution>
            <id>logidoclet-javadoc</id>
            <phase>compile</phase>
            <goals>
                <goal>javadoc</goal>
            </goals>
            <configuration>
                <!-- The next is necessary because otherwise LogiDoclet will respective parameter -->
                <disableNoFonts>true</disableNoFonts>
                <doclet>io.github.grimch.doclet.LogiDoclet</doclet>
                <docletArtifact>
                    <groupId>io.github.grimch</groupId>
                    <artifactId>logidoclet</artifactId>
                    <version>${logidoclet.version}</version>
                </docletArtifact>
                <useStandardDocletOptions>false</useStandardDocletOptions>
                <additionalOptions>
                    <!-- Pass the output directory to your custom doclet -->
                    <additionalOption>-d ${logidoc.output.directory}</additionalOption>
                    <!-- Pass the prettyPrint flag -->
                    <!-- Comment out the line below to enable full output -->
                    <!-- Note: tokenization is more efficient wit this disabled -->
                    <additionalOption>-prettyPrint</additionalOption>
                </additionalOptions>
            </configuration>
        </execution>
    </executions>
</plugin>

```

<!-- TOC --><a name="usage-with-gradle"></a>
### Usage with Gradle

You can integrate LogiDoclet directly into your project's `build.gradle` by registering  `logidocletJavadoc` task.

#### Setup
1.  **Install LogiDoclet Locally:** Since it is not yet available on Maven Central. You must build and install it locally first:
    ```bash
    # In the logidoclet project directory
    gradle clean publishToMavenLocal
    ```
2.  **Configure Your `pom.xml`:** Add the `maven-javadoc-plugin` to your project's `pom.xml` and configure it to use LogiDoclet.

An example `build.gradle` is provided in the [`examples/logidoclet-usage/`](./examples/logidoclet-usage/) directory. You can adapt the `logidoclet` configuration, dependency and task registration from this file into your own project.

Key configuration snippet from the example which shows how generate standard javadoc and LogiDoc output in parallel:

```groovy
configurations {
    logidoclet
}

dependencies {
    logidoclet "io.github.grimch:logidoclet:1.0.0"
}

tasks.register('defaultJavadoc', Javadoc) {
    source = sourceSets.main.allJava
    classpath = sourceSets.main.compileClasspath
    destinationDir = file("${buildDir}/javadoc")
    options.showAll()
}

tasks.register('logidocletJavadoc', Javadoc) {
    source = sourceSets.main.allJava
    classpath = sourceSets.main.compileClasspath
    destinationDir = logidocOutputDirectory.get().asFile
    title = null

    options.author = false
    options.version = false
    options.noTimestamp = false
    options.windowTitle = null
    options.docTitle = null
    options.footer = null

    options.addBooleanOption('no-fonts', true)
    options.doclet = 'io.github.grimch.doclet.LogiDoclet'
    options.docletpath = configurations.logidoclet.files.asType(List)

    // Pass the prettyPrint flag
    // Comment out the line below to enable full output
    // Note: tokenization is more efficient with this disabled
    options.addStringOption('prettyPrint', 'true')
}
```
___

<!-- TOC --><a name="formatted-prolog-output-example"></a>
## Formatted Prolog Output Example

To illustrate the output of LogiDoclet, you can examine the Java classes used for unit testing and their corresponding expected formatted Prolog representation. The `-prettyPrint` option was used to generate this output.

*   **Sample Java Source Files:** [`src/test/resources/sample_module`](./src/test/resources/sample_module)
*   **Expected Formatted Prolog Output Files:** [`src/test/resources/expected_output/full`](./src/test/resources/expected_output/full)
___

<!-- TOC --><a name="standalone-example-project"></a>
## Standalone Example Project
To make it easy to experiment, a complete, runnable Maven example is provided in the [`examples/logidoclet-usage/`](./examples/logidoclet-usage/) directory.

**To run the example:**
1.  **Navigate to the example directory:**
    ```bash
    cd examples/logidoclet-usage
    ```
2.  **Run the setup script:** This will copy the sample source from *src/test/resources/sample_module* into the example project.
    *   On Linux or macOS:
        ```bash
        bash setup_example.sh
        ```
    *   On Windows:
        ```cmd
        setup_example.bat
        ```
3. **Generate the Javadoc** 
   * **Using Maven (Note that you need to explitly run Maven `compile` phase and not `javadoc:javadoc` goal for maven to run both executions!):**
     ```bash
     mvn clean compile
     ```
     ****

   * **Using Gradle:**
     ```bash
      gradle clean defaultJavadoc logidocletJavadoc
     ```  
In both cases the Prolog documentation will be generated in the `examples/logidoclet-usage/llm-context/apidocs` directory. Output will be both minimal and full output (inlcuding comments) and formatted.

Also for both cases standard Javadoc output will be generated as well is respective default output folders for comparison purposes.

**Additional Output Files for AI Tool Integration**

LogiDoclet generates three files in the specified output directory (`-d` option), which are crucial for AI agent integration:

*   **[`LLM_context.md`](src/main/resources/LLM_context.md)**: Algorithmic guide defining the steps an LLM must follow to parse the LogiDoclet Prolog Javadoc output.
*   **[`java_metastructure.pl`](src/main/resources/java_metastructure.pl)**: This file defines the Prolog schema (predicates and their arities) used to represent the Java codebase. It's essential for any Prolog-based AI agent to correctly interpret the generated facts.
*   **[`templates/master_LLM_context.md.template`](src/main/resources/master_LLM_context_template.md)**: This file serves as a structured template for initializing AI tools like Claude Code and Gemini CLI. It is designed to be copied directly into your project's root directory (or a designated context directory for your AI tool) under a suitable name (e.g., `gemini.md` or `claude.md`). The AI tool is then expected to interpret this file, which contains references to `LLM_context.md` and the generated Prolog facts, to establish its initial context about the codebase.

---

<!-- TOC --><a name="developer-guide"></a>
## Developer Guide

This guide provides information for developers who want to contribute to LogiDoclet itself.

<!-- TOC --><a name="architecture-overview"></a>
### Architecture Overview

LogiDoclet hooks into the `javadoc` toolchain. The tool parses the Java source and provides an Abstract Syntax Tree (AST) to our doclet, which then transforms the AST nodes into Prolog facts.

```mermaid
graph TD
    subgraph Javadoc Tool
        A[Java Source Code] --> B{javadoc};
    end

    subgraph LogiDoclet Core
        B -- Doclet API provides AST --> C["LogiDoclet.run()"];
        C -- instantiates and runs --> D[PrologVisitor];
        D -- traverses AST --> D;
        D -- creates --> E["Prolog Data Model<br>(Fact, Atom, PrologList)"];
        E -- are written by --> F[DocletPrologWriter];
    end

    subgraph Output
        F --> G["Prolog Files (*.pl)"];
    end
```
<!-- TOC --><a name="project-structure"></a>
### Project Structure
```
.
├── pom.xml                 # Maven build configuration
└── src
    ├── main
    │   ├── java
    │   │   └── io/github/grimch/doclet
    │   │       ├── LogiDoclet.java         # Main doclet entry point
    │   │       └── prolog
    │   │           ├── PrologVisitor.java       # Traverses the Java AST
    │   │           ├── DocletPrologWriter.java  # Writes facts to files
    │   │           ├── PrettyPrinter.java       # Formats output for better readability (note that this will make tokenizazion less efficient).
    │   │           └── *.java                   # Prolog data model (Term, Fact, etc.)
    │   └── resources
    │       └── java_metastructure.pl            # Algorithmic guide defining the steps an LLM must follow to parse the LogiDoclet Prolog Javadoc output.
    │       └── jLLM_context.md                  # Information for the LLM how to use/traverse the javadoc information.
    │       └── master_LLM_context_template.md   # A template for the initial context file at the project root folder (see above).
    └── test
        ├── java                        # Unit and integration tests
        └── resources
            ├── sample_module           # A sample Java project for testing
            └── expected_output         # The expected Prolog output for the sample
```
* **`LogiDoclet`**: The main class implementing `jdk.javadoc.doclet.Doclet`. It handles options and orchestrates the process.
* **`PrologVisitor`**: A `SimpleElementVisitor9` that does the core work. It traverses the AST elements (modules, packages, types, methods) provided by the Doclet API.
* **Prolog Data Model (`Term`, `Fact`, `Atom`, `PrologList`)**: A set of classes that represent Prolog constructs. The `PrologVisitor` builds a tree of these objects, which can then be serialized into valid Prolog syntax via their `toString()` methods.
* **`DocletPrologWriter`**: Manages the creation of the output directory structure and writes the generated Prolog facts into `.pl` files.

<!-- TOC --><a name="building-and-testing"></a>
### Building and Testing

**Prerequisites**
*   Java Development Kit (JDK) 17 or later.
*   Apache Maven 3.6.0 or later.

LogiDoclet can be built with either Maven or Gradle. To build the project, run the tests, and install it in your local repository, 
execute either :
```bash
mvn clean install
```
or
```bash
gradle clean publishToMavenLocal
```
This build will also run the integration test in `LogiDocletTest`, which generates Prolog facts for a sample project and compares them against an expected output.


---
<!-- TOC --><a name="license"></a>
## License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
