package gzotpa.entity;
import gzotpa.ir.*;
import gzotpa.ast.TypeNode;
import gzotpa.ast.ExprNode;
import gzotpa.type.Type;

public class DefinedVariable extends Variable {
    protected ExprNode initializer;
    protected Expr ir;

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

    public Expr ir() {
        return ir;
    }
    public void setIR(Expr expr) {
        this.ir = expr;
    }

    static private long tmpCnt = 0;

    static public DefinedVariable tmpVariable(Type t) {
        return new DefinedVariable(new TypeNode(t), "(tmp) " + (tmpCnt++), null);
    }
    
    public <T> T accept(EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }
}