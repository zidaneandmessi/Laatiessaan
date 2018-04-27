package gzotpa.ast;

public interface ClassVisitor<T> {
    public T visit(ClassNode cl);
}
