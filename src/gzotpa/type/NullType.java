package gzotpa.type;

public class NullType extends Type {

    public NullType() {
        super();
    }

    public String toString() {
        return "null";
    }

    public boolean isType(Type type) {
        return type instanceof NullType;
    }
}
