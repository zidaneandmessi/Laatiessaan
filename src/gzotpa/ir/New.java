package gzotpa.ir;

public class New extends Expr {
    protected Expr exprLen;
    protected long length;
    protected boolean sizeKnown;

    public New(Expr exprLen) {
        this.exprLen = exprLen;
        this.length = 0;
        this.sizeKnown = false;
    }

    public New(long length) {
        this.exprLen = null;
        this.length = length;
        this.sizeKnown = true;
    }

    public Expr exprLen() {
        return exprLen;
    }

    public long length() {
        return length;
    }

    public boolean sizeKnown() {
        return sizeKnown;
    }

    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    protected void _dump(Dumper d) {
        if (sizeKnown) d.printMember("length", length);
        else d.printMember("exprLen", exprLen);
    }
}