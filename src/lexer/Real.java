package lexer;

/**
 * Used to represent floating point numbers
 * @author ktraff
 *
 */
public class Real extends Token {
    public final float value;
    
    public Real(float value) {
        super(Tag.REAL);
        this.value = value;
    }
    
    public String toString() {
        return "" + this.value;
    }
}
