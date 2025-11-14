# Documentation Corpus Instruction for Gemini (Prolog Dual-Layer)

This directory contains Java Javadoc data serialized as **Prolog Facts** in two formats.

**Hint:** All Prolog predicate names (e.g., `METHOD`, `INTERFACE`, `USES`) strictly correspond to the **Doclet API Element Names** (e.g., `ElementKind`, `DirectiveKind`).

## File Priority and Usage

1.  **PRIORITY 1: Minimal Files (`*.min.pl`)**
    *   **Purpose:** For structural lookups, validation, and navigation (names, signatures, modifiers). Descriptions are empty.
    *   **Instruction:** ALWAYS start by consulting the minimal files (`index.min.pl` and `Type.min.pl`).

2.  **PRIORITY 2: Detailed Files (`*.pl`)**
    *   **Purpose:** For synthesis and description. Contains full Javadoc strings.
    *   **Instruction:** **ONLY** consult the corresponding detailed file (`Type.pl` or `index.pl`) if the query requires the Javadoc narrative, summary, or any descriptive text.

## Search and Synthesis Flow

1.  **Navigation:** Use the `MODULE/3`, `PACKAGE/3`, and `DIRECTIVE/3` facts in the minimal index files to locate the required element's Qualified Name.
2.  **Local Lookup Signal:** If a type name in a fact begins with **`L:`**, remove the prefix (`L:`) and treat the remainder as a Fully Qualified Name (FQN) pointing to a local file in the corpus hierarchy (e.g., `L:com.foo.Bar` $ightarrow$ `com.foo/Bar.pl`).
3.  **Synthesis:** Combine the structural facts from the minimal file with the documentation strings from the detailed file to answer the query.

## Response Format
Render the final output in standard Markdown. Do **not** output raw Prolog facts to the user.
