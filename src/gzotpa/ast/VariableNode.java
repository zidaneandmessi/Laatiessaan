package gzotpa.ast;
import gzotpa.type.Type;
import gzotpa.entity.Entity;
import gzotpa.entity.*;

public class VariableNode extends LHSNode {
    private Location location;
    private String name;
    private Entity entity;
    private ExprNode memFuncBase;
    private MemberNode memVarBase;
    private boolean implicitThis;

    public VariableNode(Location loc, String name) {
        this.location = loc;
        this.name = name;
        this.memFuncBase = null;
        this.memVarBase = null;
        this.implicitThis = false;
    }

    public VariableNode(String name) {
        this.location = null;
        this.name = name;
        this.memFuncBase = null;
        this.memVarBase = null;
        this.implicitThis = false;
    }

    public VariableNode(Location loc, Entity ent) {
        this.location = loc;
        this.entity = ent;
        this.name = ent.name();
        this.memFuncBase = null;
        this.memVarBase = null;
        this.implicitThis = false;
    }

    public VariableNode(Entity ent) {
        this.entity = ent;
        this.name = ent.name();
        this.memFuncBase = null;
        this.memVarBase = null;
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

    public void setMemVarBase(MemberNode node) {
        this.memVarBase = node;
    }

    public MemberNode memVarBase() {
        return memVarBase;
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

    public Type origType() {
        if (entity == null) {
            throw new Error("Gzotpa! Null type from empty entity! " + name);
        }
        return entity.type();
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

    public VariableNode clone() {
        VariableNode newNode = new VariableNode(null, name);
        ExprNode newMemFuncBase = null;
        if (memFuncBase != null) newMemFuncBase = memFuncBase.clone();
        newNode.setMemFuncBase(newMemFuncBase);
        MemberNode newMemVarBase = null;
        if (memVarBase != null) newMemVarBase = memVarBase.clone();
        newNode.setMemVarBase(newMemVarBase);
        newNode.setImplicitThis(implicitThis);
        newNode.setType(type());
        if (entity instanceof DefinedVariable && entity.isGlobal())
            newNode.setEntity(entity);
        if (entity instanceof DefinedFunction && entity.isGlobal())
            newNode.setEntity(entity);
        return newNode;
    }
}
