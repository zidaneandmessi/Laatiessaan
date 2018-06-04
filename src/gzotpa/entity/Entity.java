package gzotpa.entity;
import gzotpa.ast.TypeNode;
import gzotpa.type.Type;
import gzotpa.ast.Location;
import gzotpa.asm.*;

abstract public class Entity {
    protected String name;
    protected TypeNode typeNode;
    protected MemoryReference memref;
    protected long cntRefered;
    protected Operand address;
    protected boolean loopCntVar;

    public Entity(TypeNode type, String name) {
        this.name = name;
        this.typeNode = type;
        this.cntRefered = 0;
        this.loopCntVar = false;
    }

    public String name() {
        return name;
    }
    
    abstract public boolean isDefined();

    public boolean isParameter() { return false; }

    public TypeNode typeNode() {
        return typeNode;
    }

    public Type type() {
        return typeNode.type();
    }

    public long allocSize() {
        return type().allocSize();
    }

    public void refered() {
        cntRefered++;
    }

    public long cntRefered() {
        return cntRefered;
    }

    public boolean isRefered() {
        return (cntRefered > 0);
    }

    public void setLoopCntVar() {
        loopCntVar = true;
    }

    public boolean isLoopCntVar() {
        return loopCntVar;
    }

    public Location location() {
        return typeNode.location();
    }

    public MemoryReference memref() {
        if (memref == null && address == null) {
            throw new Error("Gzotpa! Address did not resolved: " + name);
        }
        return memref;
    }

    public Operand address() {
        return address;
    }

    public void setMemref(MemoryReference mem) {
        this.memref = mem;
    }

    public void setAddress(MemoryReference mem) {
        this.address = mem;
    }

    public void setAddress(ImmediateValue imm) {
        this.address = imm;
    }
    
    abstract public <T> T accept(EntityVisitor<T> visitor);
}