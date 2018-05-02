package gzotpa.entity;
import gzotpa.ast.TypeNode;
import gzotpa.ast.ExprNode;

public class DefinedVariable extends Variable {
    protected ExprNode initializer;

    public DefinedVariable(TypeNode type, String name) {
        super(type, name);
        initializer = null;
    }

    public DefinedVariable(TypeNode type, String name, ExprNode init) {
        super(type, name);
        initializer = init;
    }

    public boolean isDefined() {
        return true;
    }

    public boolean hasInitializer() {
        return (initializer != null);
    }

    public ExprNode initializer() {
        return initializer;
    }
    
    public void setInitializer(ExprNode expr) {
        this.initializer = expr;
    }
    
    public <T> T accept(EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }
}