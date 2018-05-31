package gzotpa.ast;
import gzotpa.type.*;
import gzotpa.entity.*;
import java.util.*;

public class ClassNode extends TypeDefinitionNode {
    Declarations decls;

    public ClassNode(Location loc, TypeRef ref, String name, Declarations decls) {
        super(loc, ref, name);
        for (DefinedFunction func : decls.defuns()) {
            func.setName(name + "." + func.name());
            func.parameters().add(new Parameter(this.typeNode(), "this"));
            ((FunctionTypeRef)(func.typeNode().typeRef())).addParam(ref);
        }
        this.decls = decls;
    }

    public Declarations decls() {
        return decls;
    }
    
    public boolean isClass() { return true; }

    public Type definingType() {
        return new ClassType(name(), decls(), location());
    }

    public <T> T accept(ClassVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
