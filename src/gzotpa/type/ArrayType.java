package gzotpa.type;
import gzotpa.ast.*;
import java.util.LinkedList;

public class ArrayType extends Type {
    protected Type baseType;
    protected long length;
    protected long pointerSize;
    private ExprNode exprLen;
    static final protected long undefined = -1;

    public ArrayType(Type baseType, long length, long pointerSize) {
        this.baseType = baseType;
        this.length = length;
        this.pointerSize = pointerSize;
        this.exprLen = null;
    }

    public ArrayType(Type baseType, long pointerSize) {
        this.baseType = baseType;
        this.length = undefined;
        this.pointerSize = pointerSize;
        this.exprLen = null;
    }

    public Type baseType() {
        return baseType;
    }

    public long length() {
        return length;
    }

    public ExprNode exprLen() {
        return exprLen;
    }

    public void setExprLen(ExprNode exprLen) {
        this.exprLen = exprLen;
    }

    public boolean isPointer() { return true; }
    public boolean isAllocatedArray() {
        return length != undefined &&
            (!baseType.isArray() || baseType.isAllocatedArray());
    }
    public boolean isIncompleteArray() {
        if (!baseType.isArray()) return false;
        return !baseType.isAllocatedArray();
    }

    public boolean isType(Type type) { // is array
        if (type instanceof NullType) return true;
        return type instanceof ArrayType;
    }

    public boolean isEqualType(Type type) { // is array with same dimension
        if (type instanceof NullType) return true;
        if (!(type instanceof ArrayType)) return false;
        return baseType.isEqualType(((ArrayType)type).baseType());
    }
    
    public String toString() {
        if (length < 0) {
            return baseType.toString() + "[]";
        }
        else {
            return baseType.toString() + "[" + length + "]";
        }
    }

    public String typeName() {
        return "_array";
    }

    public long size() {
        return pointerSize;
    }

    public long allocSize() {
        if (length == undefined) return pointerSize;
        else return 64 * length;
    }

    public ExprNode exprAllocSize() {
        if (exprLen == null) {
            return new IntegerLiteralNode(new IntegerType(64, "int"), IntegerTypeRef.intRef(), pointerSize);
        }
        else {
            if (baseType instanceof ArrayType) {
                return new BinaryOpNode(((ArrayType)baseType).exprAllocSize(),
                                         "*",
                                         exprLen);
            }
            else {
                return new BinaryOpNode(new IntegerType(64, "int"),
                                        new IntegerLiteralNode(new IntegerType(64, "int"), IntegerTypeRef.intRef(), 64),
                                        "*",
                                        exprLen);
            }
        }
    }

    public LinkedList<ExprNode> lenStack() {
        LinkedList<ExprNode> stack = new LinkedList<ExprNode>();
        if (exprLen == null) return stack;
        stack.addLast(exprLen);
        if (baseType instanceof ArrayType) {
            stack.addAll(((ArrayType)baseType).lenStack());
        }
        return stack;
    }

    public boolean equals(Object other) {
        if (!(other instanceof ArrayType)) return false;
        ArrayType type = (ArrayType)other;
        if (exprLen != null && !exprLen.equals(type.exprLen))
            return false;
        return (baseType.equals(type.baseType) && length == type.length);
    }
}
