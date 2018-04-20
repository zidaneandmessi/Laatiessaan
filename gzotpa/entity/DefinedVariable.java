package gzotpa.entity;
import gzotpa.ast.TypeNode;
import gzotpa.ast.ExprNode;

public class DefinedVariable extends Variable {
    protected ExprNode initializer;
    protected long sequence;

    public DefinedVariable(TypeNode type, String name) {
        super(false, type, name);
        initializer = null;
        sequence = -1;
    }

    public DefinedVariable(boolean isPrivate, TypeNode type,
                           String name, ExprNode init) {
        super(isPrivate, type, name);
        initializer = init;
        sequence = -1;
    }

    public DefinedVariable(TypeNode type,
                           String name, ExprNode init) {
        super(false, type, name);
        initializer = init;
        sequence = -1;
    }
}