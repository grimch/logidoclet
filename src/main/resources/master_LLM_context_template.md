# Project Knowledge Base & Navigation

> **Note to Developer:** Append this content to your project's root-level AI instructions. Ensure the path `llm-context/apidocs/` matches your generation output.

## ⚡ Core Protocol: Javadoc-First Development

To ensure high-quality code generation and minimize token consumption, you must adhere to the **LogiDoclet Protocol**.

**Mandatory Initialization:**
Before undertaking **ANY** code-altering, code-designing, or dependency-mapping task — including, but not limited to, **designing new components** (e.g., a Controller tier), **overhauling existing architecture** (refactoring) — you **MUST** read and apply the instructions found in:
👉 **`llm-context/apidocs/LLM_context.md`**

**The Golden Rule:**
Treat the Prolog-based Javadoc defined in that context file as the **Primary Source of Truth and Design Specification**. You are strictly forbidden from analyzing raw `.java` source files unless the Prolog documentation explicitly fails to provide the necessary method signatures or structural relationships required for the intended design or implementation.

---

### 🗺️ LLM Execution Mandate Flowchart

This diagram represents the **non-negotiable order** of operations for processing any code request.

```mermaid
flowchart TD
A[User Request: Design, Refactor, or Code Task] --> B{Task Involves Project Code?}

    B -- No --> End[Answer Request Directly]
    
    B -- Yes --> C[MANDATORY: Load `LLM_context.md` Protocol]
    C --> D(Execute Retrieval Strategy: Minimal Facts)
    
    D --> E{Prolog Facts Sufficient<br/> for Design/Signatures?}
    
    E -- Yes --> F[Generate Code / Design Solution]
    E -- No --> G(Execute Retrieval Strategy: Full Facts/Javadoc)
    
    G --> H{Javadoc Context Sufficient<br/> for Implementation?}
    
    H -- Yes --> F
    
    H -- No --> I[LAST RESORT: Read Raw .java Source Files]
    I --> F
    
    F --> End
    
    style A fill:#a9c2f6,stroke:#0d47a1,stroke-width:2px
    style C fill:#fff2cc,stroke:#ff6f00,stroke-width:3px
    style D fill:#d9ead3,stroke:#38761d
    style G fill:#d9ead3,stroke:#38761d
    style I fill:#f4cccc,stroke:#cc0000,stroke-width:3px
    style F fill:#b6d7a8,stroke:#6aa84f
    style End fill:#c9daf8
```