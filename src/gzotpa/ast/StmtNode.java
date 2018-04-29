package gzotpa.ast;

abstract public class StmtNode extends Node {
    protected Location location;
    protected boolean inLoop;

    public StmtNode(Location loc) {
        this.location = loc;
    }

    public StmtNode(Location loc, boolean inLoop) {
        this.location = loc;
    }

    public Location location() {
        return location;
    }
    
    abstract public <S,E> S accept(ASTVisitor<S,E> visitor);
}
