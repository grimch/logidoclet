# Guide for AI: Navigating LogiDoclet Prolog Output

This document provides instructions for an AI to efficiently understand and interact with the Prolog-based Javadoc output.

## 1. Schema and Retrieval Process

The master schema, bootstrap process, and type system are all documented directly within the header of **`[current_dir]/java_metastructure.pl`**.

To understand this codebase, you MUST read and follow the instructions in that file's header first. It is the single source of truth.

## 2. Minimal vs. Full Output Modes

There is a potential second version of the Prolog output located in `[current_dir]/full/`. This version includes all Javadoc comments extracted from the source code.

**Strategy for AI:**
*   Always start with the **Minimal Version** (as described in `java_metastructure.pl`) to map the project structure.
*   Switch to the **Full Version** for specific classes, methods, or fields when detailed documentation is needed for a particular task (e.g., explaining a method, generating new code, or refactoring).