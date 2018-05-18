package gzotpa.ir;

public interface IRVisitor<S,E> {
    public S visit(Assign x);
    public S visit(ConditionJump x);
    public S visit(ExprStmt x);
    public S visit(Jump x);
    public S visit(LabelStmt x);
    public S visit(Return x);

    public E visit(Addr x);
    public E visit(Bin x);
    public E visit(Call x);
    public E visit(Int x);
    public E visit(Mem x);
    public E visit(Str x);
    public E visit(Var x);
    public E visit(Uni x);
}
