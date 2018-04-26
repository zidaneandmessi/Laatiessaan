package gzotpa.type;

public class BooleanType extends Type {
    protected boolean image;
    protected String name;

    public BooleanType(boolean image, String name) {
        super();
        this.image = image;
        this.name = name;
    }
    
    public boolean isScalar() { return true; }
}
