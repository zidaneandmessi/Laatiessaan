package gzotpa.ir;
import gzotpa.entity.*;

abstract public class Expr {

    Expr() {}

    public boolean isVar() { return false; }
    
    public Expr addressNode() {
        throw new Error("Gzotpa! Address for expr callded!");
    }

    abstract public <S,E> E accept(IRVisitor<S,E> visitor);
}