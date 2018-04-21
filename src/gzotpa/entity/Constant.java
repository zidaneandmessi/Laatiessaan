package gzotpa.entity;
import gzotpa.ast.TypeNode;
import gzotpa.ast.ExprNode;

public class Constant extends Entity {
    private TypeNode type;
    private String name;
    private ExprNode value;

    public Constant(TypeNode type, String name, ExprNode value) {
        super(true, type, name);
        this.value = value;
    }
}
