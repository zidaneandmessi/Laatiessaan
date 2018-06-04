package gzotpa.ast;
import gzotpa.type.Type;
import gzotpa.exception.SemanticError;

abstract public class ExprNode extends Node {
    public ExprNode() {
        super();
    }

    abstract public Type type();
    
    protected Type origType() { return type(); }
    
    public boolean isParameter() { return false; }
    public boolean isConstant() { return false; }
    public boolean isAssignable() { return false; }
    public boolean isLoadable() { return false; }
    
    public boolean isCallable() {
        try {
            return type().isCallable();
        }
        catch (SemanticError err) {
            return false;
        }
    }
    
    public boolean isPointer() {
        try {
            return type().isPointer();
        }
        catch (SemanticError err) {
            return false;
        }
    }
    
    abstract public <S,E> E accept(ASTVisitor<S,E> visitor);

    abstract public ExprNode clone();
}
