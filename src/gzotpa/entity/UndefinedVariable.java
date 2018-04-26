package gzotpa.entity;
import gzotpa.ast.TypeNode;

public class UndefinedVariable extends Variable {
    public UndefinedVariable(TypeNode t, String name) {
        super(false, t, name);
    }

    public boolean isDefined() {
        return false;
    }
    
    public <T> T accept(EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
