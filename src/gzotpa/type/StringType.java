package gzotpa.type;

public class StringType extends Type {
    protected String name, image;

    public StringType(String image, String name) {
        super();
        this.image = image;
        this.name = name;
    }

    public boolean isConstant() {
        return true;
    }

    public boolean isType(Type type) {
        return type instanceof StringType;
    }
    public boolean isEqualType(Type type) {
        return isType(type);
    }
    
    public long size() {
    	return image.length() * 8;
    }

    public String toString() {
        return name;
    }

    public String typeName() {
        return "string";
    }
}