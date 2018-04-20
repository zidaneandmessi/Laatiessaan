package gzotpa.entity;
import gzotpa.ast.TypeNode;

public class Parameter extends DefinedVariable {
    public Parameter(TypeNode type, String name) {
        super(type, name);
    }

    public boolean isParameter() {
        return true;
    }
}
