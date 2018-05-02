package gzotpa.entity;
import gzotpa.ast.TypeNode;

abstract public class Variable extends Entity {
    public Variable(TypeNode type, String name) {
        super(type, name);
    }
}
