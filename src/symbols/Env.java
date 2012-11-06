package symbols;
import java.util.*;

/**
 * Represents a symbol table for a particular scope (or block) of execution
 * of a source program.
 * 
 * @author ktraff
 */
public class Env {
    private Hashtable<String, Symbol> table;
    // reference to the closest "parent" symbol table
    protected Env prev;
    
    public Env(Env prev) {
        this.prev = prev;
        this.table = new Hashtable<String, Symbol>();
    }
    
    public void put(String str, Symbol sym) {
        this.table.put(str, sym);
    }
    
    /**
     * Search each symbol tree, recursing up to the highest-level parent,
     * searching for the nearest applicable symbol
     * @param str
     * @return
     */
    public Symbol get(String str) {
        Env curEnv = this;
        Symbol symbol = curEnv.table.get(str);
        for (curEnv = this; curEnv != null && symbol == null; curEnv = curEnv.prev) {
            Symbol found = curEnv.table.get(str);
            if (found != null) return found;
        }
        return null;
    }
}
