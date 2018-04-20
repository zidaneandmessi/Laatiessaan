package gzotpa.ast;
import gzotpa.entity.ConstantEntry;
import gzotpa.type.TypeRef;

public class StringLiteralNode extends LiteralNode {
    protected String value;
    protected ConstantEntry entry;

    public StringLiteralNode(Location loc, TypeRef ref, String value) {
        super(loc, ref);
        this.value = value;
    }

    public String value() {
        return value;
    }

    public ConstantEntry entry() {
        return entry;
    }
}
