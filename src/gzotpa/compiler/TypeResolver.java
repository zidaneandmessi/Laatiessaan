package gzotpa.compiler;
import gzotpa.ast.*;
import gzotpa.entity.*;
import gzotpa.type.*;
import gzotpa.exception.*;
import java.util.*;

public class TypeResolver extends Visitor implements EntityVisitor<Void>{
    private final TypeTable typeTable;

    public TypeResolver(TypeTable typeTable) {
        this.typeTable = typeTable;
    }

    public void resolve(AST ast) {
        //defineTypes(ast.types());
        //for (TypeDefinition t : ast.types()) {
        //    t.accept(this);
        //}
        for (Entity e : ast.entities()) {
            e.accept(this);
        }
    }

    /*private void defineTypes(List<TypeDefinition> deftypes) {
        for (TypeDefinition def : deftypes) {
            if (typeTable.isDefined(def.typeRef())) {
                throw new Error("Gzotpa! Multiple type definition " + def.typeRef() + "!")
            }
            typeTable.put(def.typeRef(), def.definingType());
        }
    }*/

    private void bindType(TypeNode n) {
        if (n.isResolved()) return;
        n.setType(typeTable.get(n.typeRef()));
    }

    public Void visit(DefinedVariable var) {
        bindType(var.typeNode());
        if (var.hasInitializer()) {
            visitExpr(var.initializer());
        }
        return null;
    }
    
    public Void visit(UndefinedVariable var) {
        bindType(var.typeNode());
        return null;
    }
    public Void visit(DefinedFunction func) {
        bindType(func.typeNode());
        for (Parameter param : func.parameters()) {
            Type t = typeTable.getParamType(param.typeNode().typeRef());
            param.typeNode().setType(t);
        }
        visitStmt(func.body());
        return null;
    }

    public Void visit(UndefinedFunction func) {
        bindType(func.typeNode());
        for (Parameter param : func.parameters()) {
            Type t = typeTable.getParamType(param.typeNode().typeRef());
            param.typeNode().setType(t);
        }
        return null;
    }

    public Void visit(BlockNode node) {
        for (DefinedVariable var : node.variables()) {
            var.accept(this);
        }
        visitStmts(node.stmts());
        return null;
    }

    public Void visit(IntegerLiteralNode node) {
        bindType(node.typeNode());
        return null;
    }

    public Void visit(StringLiteralNode node) {
        bindType(node.typeNode());
        return null;
    }

}