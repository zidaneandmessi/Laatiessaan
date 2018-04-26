package gzotpa.entity;
import gzotpa.ast.*;
import gzotpa.exception.*;
import gzotpa.type.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ToplevelScope extends Scope {

    protected LinkedHashMap<String, Entity> entities;

    public ToplevelScope() {
        super();
        entities = new LinkedHashMap<String, Entity>();

        ParamTypes printtypes = new ParamTypes(new ArrayList<Type>(), false);
        Params printparams = new Params(new ArrayList<Parameter>());
        printtypes.add(new StringType("", "str"));
        printparams.addParameter(new Parameter(new TypeNode(new StringTypeRef("str")), "str"));
        entities.put("print", new DefinedFunction(new TypeNode(new FunctionType(new VoidType(),
                                                                                printtypes)),
                                                "print",
                                                printparams,
                                                new BlockNode(new ArrayList<DefinedVariable>(),
                                                                new ArrayList<StmtNode>())));

        ParamTypes printlntypes = new ParamTypes(new ArrayList<Type>(), false);
        Params printlnparams = new Params(new ArrayList<Parameter>());
        printlntypes.add(new StringType("", "str"));
        printlnparams.addParameter(new Parameter(new TypeNode(new StringTypeRef("str")), "str"));
        entities.put("println", new DefinedFunction(new TypeNode(new FunctionType(new VoidType(),
                                                                                    printlntypes)),
                                                "println",
                                                printlnparams,
                                                new BlockNode(new ArrayList<DefinedVariable>(),
                                                                new ArrayList<StmtNode>())));

        entities.put("getString", new DefinedFunction(new TypeNode(new FunctionType(new StringType("", "getString"),
                                                                                    new ParamTypes(new ArrayList<Type>(), false))),
                                                "getString",
                                                new Params(new ArrayList<Parameter>()),
                                                new BlockNode(new ArrayList<DefinedVariable>(),
                                                                new ArrayList<StmtNode>())));

        entities.put("getInt", new DefinedFunction(new TypeNode(new FunctionType(new IntegerType(32, "getInt"),
                                                                                    new ParamTypes(new ArrayList<Type>(), false))),
                                                "getInt",
                                                new Params(new ArrayList<Parameter>()),
                                                new BlockNode(new ArrayList<DefinedVariable>(),
                                                                new ArrayList<StmtNode>())));

        ParamTypes toStringtypes = new ParamTypes(new ArrayList<Type>(), false);
        Params toStringparams = new Params(new ArrayList<Parameter>());
        toStringtypes.add(new IntegerType(32, "i"));
        toStringparams.addParameter(new Parameter(new TypeNode(new IntegerTypeRef("i")), "i"));
        entities.put("toString", new DefinedFunction(new TypeNode(new FunctionType(new StringType("", "toString"),
                                                                                    toStringtypes)),
                                                "getString",
                                                toStringparams,
                                                new BlockNode(new ArrayList<DefinedVariable>(),
                                                                new ArrayList<StmtNode>())));

        ParamTypes lengthtypes = new ParamTypes(new ArrayList<Type>(), false);
        Params lengthparams = new Params(new ArrayList<Parameter>());
        lengthtypes.add(new StringType("", "str"));
        lengthparams.addParameter(new Parameter(new TypeNode(new StringTypeRef("str")), "str"));
        entities.put("length", new DefinedFunction(new TypeNode(new FunctionType(new IntegerType(32, "length"),
                                                                                lengthtypes)),
                                                "length",
                                                lengthparams,
                                                new BlockNode(new ArrayList<DefinedVariable>(),
                                                                new ArrayList<StmtNode>())));
    }

    public boolean isToplevel() {
        return true;
    }

    public boolean inLoop() {
        return false;
    }

    public ToplevelScope toplevel() {
        return this;
    }

    public Scope parent() {
        return null;
    }

    public void declareEntity(Entity entity) throws SemanticException {
        Entity e = entities.get(entity.name());
        if (e != null) {
            throw new SemanticException("Gzotpa! Duplicated declaration: " +
                    entity.name() + ": " +
                    e.location() + " and " + entity.location());
        }
        entities.put(entity.name(), entity);
    }

    public void defineEntity(Entity entity) throws SemanticException {
        Entity e = entities.get(entity.name());
        if (e != null && e.isDefined()) {
            throw new SemanticException("Gzotpa! Duplicated definition: " +
                    entity.name() + ": " +
                    e.location() + " and " + entity.location());
        }
        entities.put(entity.name(), entity);
    }

    public Entity get(String name) throws SemanticException {
        Entity ent = entities.get(name);
        if (ent == null) {
            throw new SemanticException("Gzotpa! Unresolved reference: " + name);
        }
        return ent;
    }
}