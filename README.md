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

    - NewTypeNode

    - NullNode

    - UnaryOpNode

        - UnaryArithmeticOpNode

            - PrefixOpNode (++i)

            - SuffixOpNode (i++)

# Entity

- Entity

    - Variable

        - DefinedVariable

            - Parameter

    - Function

        - DefinedFunction

- ParamSlots<Type>

    - Params

- StmtNode

    - BlockNode

    - BreakNode

    - ContinueNode

    - ExprStmtNode

    - ForNode

    - IfNode

    - ReturnNode

    - WhileNode

- TypeDefinitionNode

    - ClassNode

- TypeNode

# Semantic Checker

- Visitor

    - TypeResolver

    - LocalResolver

    - DereferenceChekcer

    - TypeChecker

- Scope

    - TopLevelScope

    - LocalScope