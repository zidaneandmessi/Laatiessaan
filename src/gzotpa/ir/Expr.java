package gzotpa.ir;
import gzotpa.entity.*;

abstract public class Expr implements Dumpable {

    Expr() {}

    public Expr addressNode() {
        throw new Error("Gzotpa! Address for expr callded!");
    }

    abstract public <S,E> E accept(IRVisitor<S,E> visitor);
    
    public void dump(Dumper d) {
        d.printClass(this);
        _dump(d);
    }

    abstract protected void _dump(Dumper d);
}