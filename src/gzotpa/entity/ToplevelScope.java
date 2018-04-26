package gzotpa.entity;
import gzotpa.exception.*;
import java.util.LinkedHashMap;

public class ToplevelScope extends Scope {

    protected LinkedHashMap<String, Entity> entities;

    public ToplevelScope() {
        super();
        entities = new LinkedHashMap<String, Entity>();
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