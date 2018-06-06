package gzotpa.ir;
import gzotpa.ast.LHSNode;
import gzotpa.entity.*;

abstract public class Expr implements Dumpable {

    Expr() {}

    public Expr addressNode() {
        throw new Error("Gzotpa! Address for expr called!");
    }

    public LHSNode lhsBase() {
        throw new Error("Gzotpa! LHSBase for expr called!");
    }

    abstract public Entity entity();

    abstract public boolean isIntConstant();
    abstract public boolean isStrConstant();

    abstract public <S,E> E accept(IRVisitor<S,E> visitor);
    
    public void dump(Dumper d) {
        d.printClass(this);
        _dump(d);
    }

    abstract protected void _dump(Dumper d);
}