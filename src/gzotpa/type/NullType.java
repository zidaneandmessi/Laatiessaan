package gzotpa.type;

public class NullType extends Type {

    public NullType() {
        super();
    }

    public String toString() {
        return "null";
    }

    public boolean isType(Type type) {
        return true;
    }
    public boolean isEqualType(Type type) {
        return isType(type);
    }
    
    public long size() {
    	return 0;
    }

    public String typeName() {
        return "null";
    }
}
