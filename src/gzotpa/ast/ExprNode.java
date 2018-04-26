package gzotpa.ast;
import gzotpa.type.Type;

abstract public class ExprNode extends Node {
    public ExprNode() {
        super();
    }

    abstract public Type type();
    
    protected Type origType() { return type(); }
    
    public boolean isParameter() { return false; }
    
    abstract public <S,E> E accept(ASTVisitor<S,E> visitor);
}
