package gzotpa.type;

public class PointerTypeRef extends TypeRef {
    protected TypeRef baseType;

    public PointerTypeRef(TypeRef baseType) {
        super(baseType.location());
        this.baseType = baseType;
    }
    public TypeRef baseType() {
        return baseType;
    }
}
