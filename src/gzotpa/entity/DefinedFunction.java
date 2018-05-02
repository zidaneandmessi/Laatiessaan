package gzotpa.entity;
import gzotpa.type.Type;
import gzotpa.ast.*;
import java.util.List;

public class DefinedFunction extends Function {
    protected Params params;
    protected BlockNode body;
    protected LocalScope scope;
    protected Boolean isConstruct;

    public DefinedFunction(TypeNode type, String name, Params params, BlockNode body) {
        super(type, name);
        this.params = params;
        this.body = body;
        this.isConstruct = false;
    }

    public DefinedFunction(TypeNode type, String name, Params params, BlockNode body, Boolean isConstruct) {
        super(type, name);
        this.params = params;
        this.body = body;
        this.isConstruct = isConstruct;
    }

    public boolean isDefined() {
        return true;
    }

    public boolean isConstruct() {
        return isConstruct;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Parameter> parameters() {
        return params.parameters();
    }

    public BlockNode body() {
        return body;
    }
    
    public void setScope(LocalScope scope) {
        this.scope = scope;
    }

    public LocalScope scope() {
        return scope;
    }
    
    public <T> T accept(EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
