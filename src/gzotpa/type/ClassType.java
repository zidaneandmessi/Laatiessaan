package gzotpa.type;
import gzotpa.ast.*;
import gzotpa.entity.*;
import gzotpa.exception.*;

public class ClassType extends NamedType {
    Declarations decls;

    public ClassType(String name, Declarations decls, Location loc) {
        super(name, loc);
        this.decls = decls;
    }

    public ClassType(String name, Declarations decls) {
        super(name, null);
        this.decls = decls;
    }

    public ClassType(String name) {
        super(name, null);
        this.decls = null;
    }

    public String name() {
        return name;
    }

    public Declarations decls() {
        return decls;
    }

    public long size() {
        long size = 0;
        for (DefinedVariable var : decls.defvars()) {
            if (var.type() instanceof IntegerType) {
                size += 64;
            }
            else if (var.type() instanceof StringType) {
                size += var.type().size();
            }
            else if (var.type() instanceof ArrayType) {
                size += var.type().allocSize();
            }
            else if (var.type() instanceof ClassType) {
                size += 64;
            }
        }
        return size;
    }

    public boolean isClass() { return true; }

    public boolean isType(Type type) {
        if (type instanceof NullType) return true;
        return type instanceof ClassType && name.equals(((ClassType)type).name());
    }
    public boolean isEqualType(Type type) {
        return isType(type);
    }

    public DefinedVariable getMemberVariable(String name) {
        for (DefinedVariable var : decls.defvars()) {
            if (name.equals(var.name()))
                return var;
        }
        throw new SemanticError("Gzotpa! Class has no member variable " + name + "!");
    }

    public DefinedFunction getMemberFunction(String name) {
        for (DefinedFunction func : decls.defuns()) {
            if (name.equals(func.name()))
                return func;
        }
        throw new SemanticError("Gzotpa! Class no member function " + name + "!");
    }

    public Type memberVariableType(String name) {
        return getMemberVariable(name).type();
    }

    public Type memberFunctionReturnType(String name) {
        return getMemberFunction(name).returnType();
    }

    public boolean hasMemberVariable(String name) {
        for (DefinedVariable var : decls.defvars()) {
            if (name.equals(var.name()))
                return true;
        }
        return false;
    }

    public boolean hasMemberFunction(String name) {
        for (DefinedFunction func : decls.defuns()) {
            if (name.equals(func.name()))
                return true;
        }
        return false;
    }

    public long memberOffset(String name) {
        boolean isVariable = hasMemberVariable(name);
        boolean isFunction = hasMemberFunction(name);
        if (!isVariable && !isFunction) {
            throw new Error("Gzotpa! Class has no such member!");
        }
        else if (isVariable) {
            DefinedVariable var = getMemberVariable(name);
            return 233;
        }
        else {
            DefinedFunction func = getMemberFunction(name);
            return 666;
        }
        /**/
    }

    public String toString() {
        return "class " + name;
    }

    public String typeName() {
        return name;
    }
}
