package gzotpa.entity;
import gzotpa.ir.*;
import gzotpa.asm.Register;
import gzotpa.ast.TypeNode;
import gzotpa.ast.ExprNode;
import gzotpa.type.Type;
import java.util.HashSet;

public class DefinedVariable extends Variable {
    protected ExprNode initializer;
    protected Expr ir;
    protected boolean waitingForInit;
    protected boolean isTmp;
    protected Register reg;
    protected HashSet<DefinedVariable> interferers = new HashSet<DefinedVariable>();

    public DefinedVariable(TypeNode type, String name) {
        super(type, name);
        initializer = null;
        waitingForInit = false;
        isTmp = false;
        reg = null;
    }

    public DefinedVariable(TypeNode type, String name, ExprNode init) {
        super(type, name);
        initializer = init;
        waitingForInit = false;
        isTmp = false;
        reg = null;
        interferers = new HashSet<DefinedVariable>();
    }

    public DefinedVariable(TypeNode type, String name, ExprNode init, boolean isTmp) {
        super(type, name);
        initializer = init;
        waitingForInit = false;
        this.isTmp = isTmp;
        reg = null;
        interferers = new HashSet<DefinedVariable>();
    }

    public boolean isDefined() {
        return true;
    }

    public boolean isTmp() {
        return isTmp;
    }

    public boolean hasInitializer() {
        return (initializer != null);
    }

    public ExprNode initializer() {
        return initializer;
    }
    
    public void setInitializer(ExprNode expr) {
        this.initializer = expr;
    }

    public void deletInitializer() {
        this.initializer = null;
    }

    public void setWaiting(boolean waitingForInit) {
        this.waitingForInit = waitingForInit;
    }

    public boolean waitingForInit() {
        return waitingForInit;
    }

    public Expr ir() {
        return ir;
    }

    public void setIR(Expr expr) {
        this.ir = expr;
    }

    public Register reg() {
        return reg;
    }

    public void setReg(Register reg) {
        this.reg = reg;
    }

    public HashSet<DefinedVariable> interferers()
    {
        return interferers;
    }

    public void removeInterferer(DefinedVariable var) {
        interferers.remove(var);
    }

    public void withDraw() {
        for (DefinedVariable var : interferers)
            var.removeInterferer(this);
    }

    static private long tmpCnt = 0;

    static public DefinedVariable tmpVariable(Type t) {
        return new DefinedVariable(new TypeNode(t), "(_tmp_" + (tmpCnt++) + ")", null);
    }
    
    public <T> T accept(EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }
}