package gzotpa.ast;

abstract public class StmtNode extends Node {
    protected Location location;

    public StmtNode(Location loc) {
        this.location = loc;
    }

    public Location location() {
        return location;
    }
}
