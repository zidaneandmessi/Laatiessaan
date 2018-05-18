package gzotpa.ir;
import gzotpa.asm.Label;
import gzotpa.ast.Location;

public class Jump extends Stmt {
    protected Label targetLabel;

    public Jump(Location loc, Label targetLabel) {
        super(loc);
        this.targetLabel = targetLabel;
    }

    public Label targetLabel() {
        return targetLabel;
    }
    
    public <S,E> S accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
}