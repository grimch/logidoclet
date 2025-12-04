# Guide for AI: Navigating LogiDoclet Prolog Output

This document establishes the protocol for efficiently understanding this project using Prolog-based Javadoc facts.

**Important Definition**: `[current_dir]` means the directory where this document is located.

## 1. The Token Conservation Strategy
**Goal**: Minimize the reading of raw source code.
**Rule**: Use the Javadoc (Prolog) to determine class structures, method signatures, and dependencies.

When tasked with generating new code (e.g., a Controller calling a Persistence service) or refactoring, follow this strict Hierarchy of Information Retrieval:

1.  **Level 1: Minimal Facts** (Structure, Relations, Inheritance)
2.  **Level 2: Full Facts** (Javadoc Text, Natural Language Intent)
3.  **Level 3: Source Code** (Implementation Details - **Last Resort**)

### Visual Strategy: Decision Flow
```mermaid
flowchart TD
Start([User Request]) --> Step1["Load Minimal Prolog<br/>[current_dir]/minimal/"]
Step1 --> Check1{Is Structure/Signature<br/>Clear?}

    Check1 -- Yes --> Generate([Generate/Answer])
    Check1 -- No --> Step2["Load Full Prolog<br/>[current_dir]/full/"]
    
    Step2 --> Check2{Is NLP Context<br/>Sufficient?}
    Check2 -- Yes --> Generate
    Check2 -- No --> Step3[<b>STOP & READ</b><br/>Raw .java Source Code]
    Step3 --> Generate
    
    style Step1 fill:#d4f1f4,stroke:#00778a,stroke-width:2px
    style Step2 fill:#ffe8d6,stroke:#ff7b00,stroke-width:2px
    style Step3 fill:#ffcccc,stroke:#cc0000,stroke-width:4px
```

## 2. Schema and Navigation Requirements

To execute **Level 1** and **Level 2** above, you must navigate the file system correctly.

### A. Initialization
* **Schema**: You **MUST** read **`[current_dir]/java_metastructure.pl`** first to understand the predicates (names and arguments) used in the `.pl` files.
* **Index**: You **MUST** determine the top-level structure (Module or Package) by checking for the module index file first. **Do not guess paths.**

### B. Navigation Logic (Sequential Top-Down Search Process)
The doclet output is structured **EITHER** by modules **OR** by packages, but not both. You must check for the highest-level structure first:

1.  **Check for Module Structure**: Attempt to read `minimal/module_index.pl`.
2.  **If Module Index Exists**: The project is Module-Structured. Use the Module Index to find modules, and then read the respective `module.pl` files to find the packages exported by that module. (Path: Module Index $\rightarrow$ Module $\rightarrow$ Package $\rightarrow$ Type)
3.  **If Module Index is Absent**: The project is Package-Structured. Use the fallback `minimal/package_index.pl` file to find packages directly. (Path: Package Index $\rightarrow$ Package $\rightarrow$ Type)
4.  **Resolve Path**: Translate Java names (e.g., `com.example`) to file paths (`com/example`).
5.  **Modes**: Use `minimal/` for structure and `full/` when Javadoc comments are needed.

### Visual Navigation: File System Sequence
```mermaid
sequenceDiagram
participant AI
participant FileSystem

    Note over AI, FileSystem: Phase 1: Bootstrap & Determine Top-Level Structure
    AI->>FileSystem: Read `java_metastructure.pl` (Learn Schema)
    AI->>FileSystem: **CHECK:** Does `minimal/module_index.pl` exist?

    alt Module Index Exists (Module-Structured Project)
        FileSystem-->>AI: Returns `module_index(Modules)`
        AI->>AI: Select Module (e.g., io.github.grimch.doclet.sample_module)
        AI->>FileSystem: Read `minimal/[path]/module.pl`
        FileSystem-->>AI: Returns `module(..., all_packages: [Pkg1, ...])`
        Note right of AI: Module provides package list
    else Module Index Absent (Package-Structured Project)
        AI->>FileSystem: **FALLBACK:** Read `minimal/package_index.pl`
        FileSystem-->>AI: Returns `package_index(Packages)`
        Note right of AI: Index provides package list directly
    end

    Note over AI, FileSystem: Phase 2: Drill Down to Package
    AI->>AI: Select Package (e.g., io.github.grimch.doclet.sample_module.types.basic)
    AI->>FileSystem: Read `minimal/[path]/package.pl`
    FileSystem-->>AI: Returns `package_declaration(..., declared_types: [T1, ...])`

    Note over AI, FileSystem: Phase 3: Retrieve Type Facts
    AI->>AI: Select Type (e.g., C1)
    alt Level 1: Structure Needed (Minimal)
        AI->>FileSystem: Read `minimal/[path]/C1.pl`
    else Level 2: Context Needed (Full)
        AI->>FileSystem: Read `full/[path]/C1.pl`
    end
    FileSystem-->>AI: Returns Prolog Facts
```