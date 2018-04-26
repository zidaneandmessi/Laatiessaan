package gzotpa.type;
import gzotpa.ast.TypeNode;
import gzotpa.ast.Location;

public class UserType extends NamedType {
    protected TypeNode real;

    public UserType(String name, TypeNode real, Location loc) {
        super(name, loc);
        this.real = real;
    }
    
    public boolean isPointer() { return realType().isPointer(); }
    public boolean isCallable() { return realType().isCallable(); }
    
    public Type realType() {
        return real.type();
    }

    public String toString() {
        return name;
    }
}
