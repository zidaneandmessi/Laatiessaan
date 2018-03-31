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

    - FuncallNode

    - LHSNode

        - ArefNode (a[i])

        - MemberNode (a.memb)

        - VariableNode

    - LiteralNode

        - IntegerLiteralNode

        - StringLiteralNode

    - UnaryOpNode

        - UnaryArithmeticOpNode

            - PrefixOpNode (++i)

            - SuffixOpNode (i++)

- StmtNode

    - BlockNode

    - BreakNode

    - ContinueNode

    - ExprStmtNode

    - ForNode

    - IfNode

    - ReturnNode

    - WhileNode

- TypeDefinition

- TypeNode