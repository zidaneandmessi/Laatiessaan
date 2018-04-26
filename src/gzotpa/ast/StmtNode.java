package gzotpa.ast;

abstract public class StmtNode extends Node {
    protected Location location;
    protected boolean inLoop;

    public StmtNode(Location loc) {
        this.location = loc;
        this.inLoop = false;
    }

    public StmtNode(Location loc, boolean inLoop) {
        this.location = loc;
        this.inLoop = inLoop;
    }

    public void setLoop(boolean inLoop) {
        this.inLoop = inLoop;
    }

    public boolean inLoop() {
        return inLoop;
    }

    public Location location() {
        return location;
    }
    
    abstract public <S,E> S accept(ASTVisitor<S,E> visitor);
}
