package gzotpa.type;
import gzotpa.ast.*;
import gzotpa.exception.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

public class TypeTable {

    private static int pointerSize = 32;
    private static int charSize = 8;
    private static int intSize = 32;
    private Map<TypeRef, Type> table;

    public TypeTable() {
        this.table = new HashMap<TypeRef, Type>();
    }

    public void addKnownedTypes() {
        put(new VoidTypeRef(), new VoidType());
        table.put(IntegerTypeRef.charRef(),
                  new IntegerType(charSize, "char"));
        put(IntegerTypeRef.intRef(),
                new IntegerType(intSize, "int"));
        put(IntegerTypeRef.intRef(true),
                new IntegerType(intSize, "int", true));
        put(new StringTypeRef("string"),
                new StringType("", "string"));
    }

    public boolean isDefined(TypeRef ref) {
        return table.containsKey(ref);
    }

    public void put(TypeRef ref, Type t) {
        if (table.containsKey(ref)) {
            throw new Error("Gzotpa! Multiple type definition: " + ref + "!");
        }
        table.put(ref, t);
    }

    public Type get(TypeRef ref) {
        Type type = table.get(ref);
        if (type == null) {
            if (ref instanceof ArrayTypeRef) {
                ArrayTypeRef aref = (ArrayTypeRef)ref;
                Type t = new ArrayType(get(aref.baseType()),
                                       aref.length(),
                                       pointerSize);
                table.put(aref, t);
                return t;
            }
            else if (ref instanceof PointerTypeRef) {
                PointerTypeRef pref = (PointerTypeRef)ref;
                Type t = new PointerType(pointerSize, get(pref.baseType()));
                table.put(pref, t);
                return t;
            }
            else if (ref instanceof FunctionTypeRef) {
                FunctionTypeRef fref = (FunctionTypeRef)ref;
                Type t = new FunctionType(get(fref.returnType()),
                                          fref.params().internTypes(this));
                table.put(fref, t);
                return t;
            }
            throw new Error("Gzotpa! Unregistered type: " + ref.toString() + "!");
        }
        return type;
    }

    public Type getParamType(TypeRef ref) {
        Type t = get(ref);
        return t.isArray() ? pointerTo(t.baseType()) : t;
    }

    public int intSize() {
        return this.intSize;
    }

    public int pointerSize() {
        return this.pointerSize;
    }

    public int maxIntSize() {
        return this.pointerSize;
    }

    public Type ptrDiffType() {
        return get(ptrDiffTypeRef());
    }

    public TypeRef ptrDiffTypeRef() {
        return new IntegerTypeRef(ptrDiffTypeName());
    }

    public Collection<Type> types() {
        return table.values();
    }

    protected String ptrDiffTypeName() {
        if (signedInt().size == pointerSize) return "int";
        throw new Error("Gzotpa! Integer.size != pointer.size!");
    }

    public VoidType voidType() {
        return (VoidType)table.get(new VoidTypeRef());
    }

    public IntegerType signedChar() {
        return (IntegerType)table.get(IntegerTypeRef.charRef());
    }

    public IntegerType signedInt() {
        return (IntegerType)table.get(IntegerTypeRef.intRef());
    }

    public PointerType pointerTo(Type baseType) {
        return new PointerType(pointerSize, baseType);
    }

    public void semanticCheck() {
        for (Type t : types()) {
            /*if (t instanceof CompositeType) {
                checkVoidMembers((CompositeType)t, h);
                checkDuplicatedMembers((CompositeType)t, h);
            }
            else */
            if (t instanceof ArrayType) {
                checkVoidMembers((ArrayType)t);
            }
            //checkRecursiveDefinition(t);
        }
    }

    protected void checkVoidMembers(ArrayType t) {
        if (t.baseType().isVoid()) {
            throw new Error("Gzotpa! Array cannot contain void!");
        }
    }

    /*protected void checkVoidMembers(CompositeType t, ErrorHandler h) {
        for (Slot s : t.members()) {
            if (s.type().isVoid()) {
                h.error(t.location(), "struct/union cannot contain void");
            }
        }
    }

    protected void checkDuplicatedMembers(CompositeType t, ErrorHandler h) {
        Map<String, Slot> seen = new HashMap<String, Slot>();
        for (Slot s : t.members()) {
            if (seen.containsKey(s.name())) {
                h.error(t.location(),
                        t.toString() + " has duplicated member: " + s.name());
            }
            seen.put(s.name(), s);
        }
    }*/

    /*protected void checkRecursiveDefinition(Type t) {
        _checkRecursiveDefinition(t, new HashMap<Type, Object>());
    }

    static final protected Object checking = new Object();
    static final protected Object checked = new Object();

    protected void _checkRecursiveDefinition(Type t, Map<Type, Object> marks) {
        if (marks.get(t) == checking) {
            throw new error("Gzotpa! Recursive type definition: " + t);
        }
        else if (marks.get(t) == checked) {
            return;
        }
        else {
            marks.put(t, checking);
            if (t instanceof CompositeType) {
                CompositeType ct = (CompositeType)t;
                for (Slot s : ct.members()) {
                    _checkRecursiveDefinition(s.type(), marks, h);
                }
            }
            else 
            if (t instanceof ArrayType) {
                ArrayType at = (ArrayType)t;
                _checkRecursiveDefinition(at.baseType(), marks);
            }
            else if (t instanceof UserType) {
                UserType ut = (UserType)t;
                _checkRecursiveDefinition(ut.realType(), marks);
            }
            marks.put(t, checked);
        }
    }*/
}
