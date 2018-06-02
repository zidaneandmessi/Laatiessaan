package gzotpa.ir;
import gzotpa.asm.Label;
import gzotpa.ast.Location;
import gzotpa.entity.Entity;

public class ConditionJump extends Stmt {
    protected Expr cond;
    protected Label thenLabel, elseLabel;

    public ConditionJump(Location loc, Expr cond, Label thenLabel, Label elseLabel) {
        super(loc);
        this.cond = cond;
        this.thenLabel = thenLabel;
        this.elseLabel = elseLabel;
    }

    public ConditionJump(Expr cond, Label thenLabel, Label elseLabel) {
        super(null);
        this.cond = cond;
        this.thenLabel = thenLabel;
        this.elseLabel = elseLabel;
    }

    public Expr cond() {
        return cond;
    }

    public Label thenLabel() {
        return thenLabel;
    }

    public Label elseLabel() {
        return elseLabel;
    }

    public <S,E> S accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    protected void _dump(Dumper d) {
        d.printMember("cond", cond);
        d.printMember("thenLabel", thenLabel);
        d.printMember("elseLabel", elseLabel);
    }
}