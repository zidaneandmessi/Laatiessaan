package gzotpa.ir;
import java.util.LinkedList;
import gzotpa.entity.Entity;
import gzotpa.type.Type;

public class New extends Expr {
    protected LinkedList<Expr> lenStack;
    protected long length;
    protected boolean sizeKnown;
    protected Type type;

    public New(LinkedList<Expr> lenStack) {
        this.lenStack = lenStack;
        this.length = 0;
        this.sizeKnown = false;
    }

    public New(LinkedList<Expr> lenStack, Type type) {
        this.lenStack = lenStack;
        this.length = 0;
        this.sizeKnown = false;
        this.type = type;
    }

    public New(long length) {
        this.lenStack = null;
        this.length = length;
        this.sizeKnown = true;
    }

    public New(long length, Type type) {
        this.lenStack = null;
        this.length = length;
        this.sizeKnown = true;
        this.type = type;
    }

    public LinkedList<Expr> lenStack() {
        return lenStack;
    }

    public Expr exprLen() {
        return lenStack.getLast();
    }

    public long length() {
        return length;
    }

    public boolean sizeKnown() {
        return sizeKnown;
    }

    public Type type() {
        return type;
    }

    public Entity entity() {
        throw new Error("Gzotpa! IR node has no entity!");
    }

    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    protected void _dump(Dumper d) {
        if (sizeKnown) d.printMember("length", length);
        else d.printMember("exprLen", exprLen());
    }
}