package gzotpa.ast;
import gzotpa.type.*;

public class TypeNode extends Node {
    TypeRef typeRef;
    Type type;

    public TypeNode(TypeRef ref) {
        super();
        this.typeRef = ref;
    }

    public TypeNode(Type type) {
        super();
        this.type = type;
    }

    public boolean isResolved() {
        return (type != null);
    }
    
    public Location location() {
        return typeRef == null ? null : typeRef.location();
    }

    public TypeRef typeRef() {
        return typeRef;
    }

    public void setType(Type t) {
        if (type != null) {
            throw new Error("Gzotpa! Type already exists!");
        }
        this.type = t;
    }

    public Type type() {
        if (type == null) {
            throw new Error("Gzotpa! Null type! TypeRef = "+typeRef);
        }
        return type;
    }
}
