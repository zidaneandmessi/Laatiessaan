package gzotpa.core;
import gzotpa.ast.*;
import gzotpa.entity.*;
import gzotpa.type.*;
import gzotpa.exception.*;
import java.util.*;

public class TypeResolver extends Visitor implements EntityVisitor<Void>, ClassVisitor<Void> {
    private final TypeTable typeTable;

    public TypeResolver(TypeTable typeTable) {
        this.typeTable = typeTable;
    }

    public void resolve(AST ast) {
        defineTypes(ast.types());
        for (TypeDefinitionNode t : ast.types()) {
            t.accept(this);
        }
        for (Entity e : ast.entities()) {
            e.accept(this);
        }
    }

    private void defineTypes(List<TypeDefinitionNode> deftypes) {
        for (TypeDefinitionNode def : deftypes) {
            if (typeTable.isDefined(def.typeRef())) {
                throw new Error("Gzotpa! Multiple type definition " + def.typeRef() + "!");
            }
            typeTable.put(def.typeRef(), def.definingType());
        }
    }

    private void bindType(TypeNode n) {
        if (n == null) return;
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
    
    public Void visit(DefinedFunction func) {
        bindType(func.typeNode());
        for (Parameter param : func.parameters()) {
            Type t = typeTable.getParamType(param.typeNode().typeRef());
            param.typeNode().setType(t);
        }
        visitStmt(func.body());
        return null;
    }

    public Void visit(BlockNode node) {
        Iterator<DefinedVariable> vars = node.variables().iterator();
        Iterator<StmtNode> stmts = node.stmts().iterator();
        for (Boolean b : node.order()) {
            if (b == true) {
                DefinedVariable var = vars.next();
                var.accept(this);
            }
            else if(b == false) {
                StmtNode stmt = stmts.next();
                if (stmt != null)
                    stmt.accept(this);
            }
        }
        return null;
    }

    public Void visit(ClassNode cl) {
        ClassType type = (ClassType)typeTable.get(cl.typeRef());
        for (DefinedVariable v : cl.decls().defvars()) {
            bindType(v.typeNode());
        }
        for (DefinedFunction f : cl.decls().defuns()) {
            bindType(f.typeNode());
            for (Parameter param : f.parameters()) {
                Type t = typeTable.getParamType(param.typeNode().typeRef());
                if (!param.typeNode().isResolved())
                    param.typeNode().setType(t);
            }
            visitStmt(f.body());
        }
        return null;
    }

    public Void visit(IntegerLiteralNode node) {
        bindType(node.typeNode());
        return null;
    }

    public Void visit(NewTypeNode node) {
        if (node.typeRef() instanceof ArrayTypeRef) {
            ((ArrayTypeRef)node.typeRef()).exprLen().accept(this);
        }
        bindType(node.typeNode());
        if (node.type() instanceof ArrayType) {
            ((ArrayType)node.type()).setExprLen(((ArrayTypeRef)node.typeRef()).exprLen());
        }
        return null;
    }

    public Void visit(StringLiteralNode node) {
        bindType(node.typeNode());
        return null;
    }
}