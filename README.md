# Laatiessaan
From Mx* language to Linux x86-64 Assembly Compiler

# Osat

|Osa|Selity|
|-|-|
|asm|kokoonpano objekti|
|ast|abstrakti syntaksipuu|
|core|ydin|
|entity|toiminto & muuttuja|
|ir|keskitason edustusto|
|parser|jäsennin|
|sys|assembler & linker|
|type|tyyppi|
|utility|hyödyllisyys|

# Parser

Parser Generator: JavaCC (LL)

# AST

- Root

- ExprNode

    - AbstractAssignNode

        - AssignNode (=)

        - OpAssignNode (+=, -=, ...)

    - BinaryOpNode (+, -, ...)

        - LogicalAndNode (&&)

        - LogicalOrNode (||)