package gzotpa.ast;

public interface ASTVisitor<S, E> {
    public S visit(BlockNode node);
    public S visit(BreakNode node);
    public S visit(ContinueNode node);
    public S visit(ExprStmtNode node);
    public S visit(ForNode node);
    public S visit(IfNode node);
    public S visit(ReturnNode node);
    public S visit(WhileNode node);

    public E visit(AssignNode node);
    public E visit(ArefNode node);
    public E visit(BinaryOpNode node);
    public E visit(FuncallNode node);
    public E visit(IntegerLiteralNode node);
    public E visit(LogicalAndNode node);
    public E visit(LogicalOrNode node);
    public E visit(MemberNode node);
    public E visit(MemberFuncNode node);
    public E visit(NewTypeNode node);
    public E visit(NullNode node);
    public E visit(OpAssignNode node);
    public E visit(PrefixOpNode node);
    public E visit(StringLiteralNode node);
    public E visit(SuffixOpNode node);
    public E visit(UnaryOpNode node);
    public E visit(VariableNode node);
}
