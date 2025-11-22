# Guide for AI: Navigating LogiDoclet Prolog Output

This document provides instructions for an AI to efficiently understand and interact with the Prolog-based Javadoc output.

**Important Definition**: `[current_dir]` means the directory where the current document is located.

## 1. Schema and Retrieval Process

* ** You must always follow this process to identify any packages or types in the Javadoc!**

* Javadoc output is located in `[current_dir]/minimal`
* packages and module names need to be tranlated into file paths, e.g., 'com.test' -> 'com/test'. 
* The master schema, bootstrap process, and type system are all documented directly within the header of **`[current_dir]/java_metastructure.pl`**.
* To understand this codebase, you MUST read and follow the instructions in that file's header first. It is the single source of truth.
* 
* **If you MUST NOT try to read a module package or type file which is not contained in the repective index. If there is no entry in the index there will be no file either.**
* **If something for you is unclear or ambiguous about this flow inform the user and propose a moidification of this file.**

### Visual Sequence Description of the above Retrieval Process

```mermaid
sequenceDiagram
    participant AI
    participant FileSystem

    Note over AI, FileSystem: Step 1: Consult Metadata
    AI->>FileSystem: Read `[current_dir]/java_metastructure.pl`
    FileSystem-->>AI: Load predicate definitions

    Note over AI, FileSystem: Step 2: Determine Entry Point
    AI->>FileSystem: Check existence of `[current_dir]/minimal/module_index.pl`

    alt `module_index.pl` exists
        FileSystem-->>AI: Returns `module_index(Modules)`
        AI->>AI: Convert Module Name to Path
        Note right of AI: e.g., 'com.test' -> 'com/test'
        Note over AI, FileSystem: AI->>AI: Parse module paths
        AI->>FileSystem: Read `.../module/module.pl`
        FileSystem-->>AI: `module(..., all_packages: [Pkg1, ...])`
        Note right of AI: Extract `all_packages`
    else `module_index.pl` missing
        AI->>FileSystem: Read `package_index.pl`
        FileSystem-->>AI: Returns `package_index(Packages)`
        Note right of AI: Extract packages directly
    end

    Note over AI, FileSystem: Step 3: Navigate to Package
    AI->>AI: Convert Package Name to Path
    Note right of AI: e.g., 'com.test' -> 'com/test'

    AI->>FileSystem: Read `[path]/package.pl`
    FileSystem-->>AI: `package_declaration(..., declared_types: [T1, ...])`

    Note over AI, FileSystem: Step 4: Analyze Type
    AI->>FileSystem: Read `[path]/T1.pl`
    FileSystem-->>AI: `class('T1', ...)`
```

## 3. Minimal vs. Full Output Modes

There is a potential second version of the Prolog output located in `[current_dir]/full/`. This version includes all Javadoc comments extracted from the source code.

**Strategy for AI:**
*   Always start with the **Minimal Version** (as described in `java_metastructure.pl`) to map the project structure.
*   Switch to the **Full Version** for specific classes, methods, or fields when detailed documentation is needed for a particular task (e.g., explaining a method, generating new code, or refactoring).