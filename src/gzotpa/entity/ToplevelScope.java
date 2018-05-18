package gzotpa.entity;
import gzotpa.ast.*;
import gzotpa.exception.*;
import gzotpa.type.*;
import java.util.List;
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
                                                                new ArrayList<StmtNode>(),
                                                                new ArrayList<Boolean>())));

        ParamTypes printlntypes = new ParamTypes(new ArrayList<Type>(), false);
        Params printlnparams = new Params(new ArrayList<Parameter>());
        printlntypes.add(new StringType("", "str"));
        printlnparams.addParameter(new Parameter(new TypeNode(new StringTypeRef("str")), "str"));
        entities.put("println", new DefinedFunction(new TypeNode(new FunctionType(new VoidType(),
                                                                                    printlntypes)),
                                                "println",
                                                printlnparams,
                                                new BlockNode(new ArrayList<DefinedVariable>(),
                                                                new ArrayList<StmtNode>(),
                                                                new ArrayList<Boolean>())));

        entities.put("getString", new DefinedFunction(new TypeNode(new FunctionType(new StringType("", "getString"),
                                                                                    new ParamTypes(new ArrayList<Type>(), false))),
                                                "getString",
                                                new Params(new ArrayList<Parameter>()),
                                                new BlockNode(new ArrayList<DefinedVariable>(),
                                                                new ArrayList<StmtNode>(),
                                                                new ArrayList<Boolean>())));

        entities.put("getInt", new DefinedFunction(new TypeNode(new FunctionType(new IntegerType(32, "getInt"),
                                                                                    new ParamTypes(new ArrayList<Type>(), false))),
                                                "getInt",
                                                new Params(new ArrayList<Parameter>()),
                                                new BlockNode(new ArrayList<DefinedVariable>(),
                                                                new ArrayList<StmtNode>(),
                                                                new ArrayList<Boolean>())));

        ParamTypes toStringtypes = new ParamTypes(new ArrayList<Type>(), false);
        Params toStringparams = new Params(new ArrayList<Parameter>());
        toStringtypes.add(new IntegerType(32, "i"));
        toStringparams.addParameter(new Parameter(new TypeNode(new IntegerTypeRef("i")), "i"));
        entities.put("toString", new DefinedFunction(new TypeNode(new FunctionType(new StringType("", "toString"),
                                                                                    toStringtypes)),
                                                "toString",
                                                toStringparams,
                                                new BlockNode(new ArrayList<DefinedVariable>(),
                                                                new ArrayList<StmtNode>(),
                                                                new ArrayList<Boolean>())));

        ParamTypes lengthtypes = new ParamTypes(new ArrayList<Type>(), false);
        Params lengthparams = new Params(new ArrayList<Parameter>());
        lengthtypes.add(new StringType("", "str"));
        lengthparams.addParameter(new Parameter(new TypeNode(new StringTypeRef("str")), "str"));
        entities.put("string.length", new DefinedFunction(new TypeNode(new FunctionType(new IntegerType(32, "string.length"),
                                                                                lengthtypes)),
                                                "string.length",
                                                lengthparams,
                                                new BlockNode(new ArrayList<DefinedVariable>(),
                                                                new ArrayList<StmtNode>(),
                                                                new ArrayList<Boolean>())));

        ParamTypes substringtypes = new ParamTypes(new ArrayList<Type>(), false);
        Params substringparams = new Params(new ArrayList<Parameter>());
        substringtypes.add(new IntegerType(32, "left"));
        substringparams.addParameter(new Parameter(new TypeNode(new IntegerTypeRef("left")), "left"));
        substringtypes.add(new IntegerType(32, "right"));
        substringparams.addParameter(new Parameter(new TypeNode(new IntegerTypeRef("right")), "right"));
        substringtypes.add(new StringType("", "str"));
        substringparams.addParameter(new Parameter(new TypeNode(new StringTypeRef("str")), "str"));
        entities.put("string.substring", new DefinedFunction(new TypeNode(new FunctionType(new StringType("", "string.substring"),
                                                                                substringtypes)),
                                                "string.substring",
                                                substringparams,
                                                new BlockNode(new ArrayList<DefinedVariable>(),
                                                                new ArrayList<StmtNode>(),
                                                                new ArrayList<Boolean>())));

        ParamTypes parseInttypes = new ParamTypes(new ArrayList<Type>(), false);
        Params parseIntparams = new Params(new ArrayList<Parameter>());
        parseInttypes.add(new StringType("", "str"));
        parseIntparams.addParameter(new Parameter(new TypeNode(new StringTypeRef("str")), "str"));
        entities.put("string.parseInt", new DefinedFunction(new TypeNode(new FunctionType(new IntegerType(32, "parseInt"),
                                                                                parseInttypes)),
                                                "string.parseInt",
                                                parseIntparams,
                                                new BlockNode(new ArrayList<DefinedVariable>(),
                                                                new ArrayList<StmtNode>(),
                                                                new ArrayList<Boolean>())));

        ParamTypes ordtypes = new ParamTypes(new ArrayList<Type>(), false);
        Params ordparams = new Params(new ArrayList<Parameter>());
        ordtypes.add(new IntegerType(32, "pos"));
        ordparams.addParameter(new Parameter(new TypeNode(new IntegerTypeRef("pos")), "pos"));
        ordtypes.add(new StringType("", "str"));
        ordparams.addParameter(new Parameter(new TypeNode(new StringTypeRef("str")), "str"));
        entities.put("string.ord", new DefinedFunction(new TypeNode(new FunctionType(new IntegerType(32, "string.ord"),
                                                                                ordtypes)),
                                                "string.ord",
                                                ordparams,
                                                new BlockNode(new ArrayList<DefinedVariable>(),
                                                                new ArrayList<StmtNode>(),
                                                                new ArrayList<Boolean>())));


        ParamTypes sizetypes = new ParamTypes(new ArrayList<Type>(), false);
        Params sizeparams = new Params(new ArrayList<Parameter>());
        sizetypes.add(new ArrayType(new IntegerType(32, "size"), 32));
        sizeparams.addParameter(new Parameter(new TypeNode(new ArrayTypeRef(new IntegerTypeRef("arr"))), "arr"));
        entities.put("array.size", new DefinedFunction(new TypeNode(new FunctionType(new IntegerType(32, "array.size"),
                                                                                sizetypes)),
                                                "array.size",
                                                sizeparams,
                                                new BlockNode(new ArrayList<DefinedVariable>(),
                                                                new ArrayList<StmtNode>(),
                                                                new ArrayList<Boolean>())));
    }

    public boolean isToplevel() {
        return true;
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
        if (entity instanceof DefinedFunction && ((DefinedFunction)entity).isConstruct())
            return;
        entities.put(entity.name(), entity);
    }

    public List<DefinedVariable> definedGlobalScopeVariables() {
        List<DefinedVariable> vars = new ArrayList<DefinedVariable>();
        for (Entity ent : entities.values()) {
            if (ent instanceof DefinedVariable) {
                vars.add((DefinedVariable)ent);
            }
        }
        return vars;
    }

    public boolean has(String name) {
        Entity ent = entities.get(name);
        return ent != null;
    }

    public Entity get(String name) throws SemanticException {
        Entity ent = entities.get(name);
        if (ent == null) {
            throw new SemanticException("Gzotpa! Unresolved reference: " + name);
        }
        return ent;
    }
}