package gzotpa.ast;
import gzotpa.type.Type;
import gzotpa.entity.Entity;
import gzotpa.entity.DefinedVariable;

public class VariableNode extends LHSNode {
    private Location location;
    private String name;
    private Entity entity;
    private ExprNode memFuncBase;
    private boolean implicitThis;

    public VariableNode(Location loc, String name) {
        this.location = loc;
        this.name = name;
        this.memFuncBase = null;
        this.implicitThis = false;
    }

    public VariableNode(String name) {
        this.location = null;
        this.name = name;
        this.memFuncBase = null;
        this.implicitThis = false;
    }

    public VariableNode(DefinedVariable var) {
        this.entity = var;
        this.name = var.name();
        this.memFuncBase = null;
        this.implicitThis = false;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMemFuncBase(ExprNode expr) {
        this.memFuncBase = expr;
    }

    public ExprNode memFuncBase() {
        return memFuncBase;
    }

    public void setImplicitThis(boolean b) {
        this.implicitThis = b;
    }

    public boolean implicitThis() {
        return implicitThis;
    }

    public boolean isResolved() {
        return (entity != null);
    }

    public Entity entity() {
        if (entity == null) {
            throw new Error("Gzotpa! VariableNode.entity == null! " + name);
        }
        return entity;
    }

    public void setEntity(Entity ent) {
        entity = ent;
    }

    public TypeNode typeNode() {
        return entity().typeNode();
    }

    public boolean isParameter() {
        return entity().isParameter();
    }

    protected Type origType() {
        return entity().type();
    }

    public Location location() {
        return location;
    }
    
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return "Variable " + name;
    }
}
