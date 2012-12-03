package symbols;
import java.util.*; import lexer.*; import inter.*;

/**
 * Represents a symbol table for a particular scope (or block) of execution
 * of a source program.
 * 
 * @author ktraff
 */
public class Env {
    private Hashtable<Token, Id> table;
    // reference to the closest "parent" symbol table
    protected Env prev;
    
    public Env(Env prev) {
        this.prev = prev;
        this.table = new Hashtable<Token, Id>();
    }
    
    public void put(Token w, Id i) { table.put(w, i); }
    
    /**
     * Search each symbol tree, recursing up to the highest-level parent,
     * searching for the nearest applicable symbol
     * @return
     */
    public Id get(Token w) {
        for( Env e = this; e != null; e = e.prev ) {
            Id found = (Id)(e.table.get(w));
            if( found != null ) return found;
        }
        return null;
    }
}
