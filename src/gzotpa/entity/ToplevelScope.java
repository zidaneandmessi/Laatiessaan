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

        ParamTypes substringtypes = new ParamTypes(new ArrayList<Type>(), false);
        Params substringparams = new Params(new ArrayList<Parameter>());
        substringtypes.add(new IntegerType(32, "left"));
        substringparams.addParameter(new Parameter(new TypeNode(new IntegerTypeRef("left")), "left"));
        substringtypes.add(new IntegerType(32, "right"));
        substringparams.addParameter(new Parameter(new TypeNode(new IntegerTypeRef("right")), "right"));
        substringtypes.add(new StringType("", "str"));
        substringparams.addParameter(new Parameter(new TypeNode(new StringTypeRef("str")), "str"));
        entities.put("substring", new DefinedFunction(new TypeNode(new FunctionType(new IntegerType(32, "substring"),
                                                                                substringtypes)),
                                                "substring",
                                                substringparams,
                                                new BlockNode(new ArrayList<DefinedVariable>(),
                                                                new ArrayList<StmtNode>())));

        ParamTypes parseInttypes = new ParamTypes(new ArrayList<Type>(), false);
        Params parseIntparams = new Params(new ArrayList<Parameter>());
        parseInttypes.add(new StringType("", "str"));
        parseIntparams.addParameter(new Parameter(new TypeNode(new StringTypeRef("str")), "str"));
        entities.put("parseInt", new DefinedFunction(new TypeNode(new FunctionType(new IntegerType(32, "parseInt"),
                                                                                parseInttypes)),
                                                "parseInt",
                                                parseIntparams,
                                                new BlockNode(new ArrayList<DefinedVariable>(),
                                                                new ArrayList<StmtNode>())));

        ParamTypes ordtypes = new ParamTypes(new ArrayList<Type>(), false);
        Params ordparams = new Params(new ArrayList<Parameter>());
        ordtypes.add(new IntegerType(32, "pos"));
        ordparams.addParameter(new Parameter(new TypeNode(new IntegerTypeRef("pos")), "pos"));
        ordtypes.add(new StringType("", "str"));
        ordparams.addParameter(new Parameter(new TypeNode(new StringTypeRef("str")), "str"));
        entities.put("ord", new DefinedFunction(new TypeNode(new FunctionType(new IntegerType(32, "ord"),
                                                                                ordtypes)),
                                                "ord",
                                                ordparams,
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