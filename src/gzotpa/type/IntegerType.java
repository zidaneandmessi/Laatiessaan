package gzotpa.type;

public class IntegerType extends Type {
    protected long size;
    protected String name;

    public IntegerType(long size, String name) {
        super();
        this.size = size;
        this.name = name;
    }

    public boolean isInteger() {
        return true;
    }
    
    public String toString() {
        return name;
    }
}
