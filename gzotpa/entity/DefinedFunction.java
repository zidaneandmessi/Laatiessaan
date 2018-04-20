package gzotpa.entity;
import gzotpa.type.Type;
import gzotpa.ast.*;

public class DefinedFunction extends Function {
    protected Params params;
    protected BlockNode body;
    protected LocalScope scope;

    public DefinedFunction(boolean isPrivate, TypeNode type,
            String name, Params params, BlockNode body) {
        super(isPrivate, type, name);
        this.params = params;
        this.body = body;
    }

    public DefinedFunction(TypeNode type,
            String name, Params params, BlockNode body) {
        super(false, type, name);
        this.params = params;
        this.body = body;
    }

    public List<Parameter> parameters() {
        return params.parameters();
    }

    public BlockNode body() {
        return body;
    }
}
