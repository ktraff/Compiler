package lexer;

/**
 * A token that represents a number (denoted by it's value)
 * @author ktraff
 *
 */
public class Num extends Token {
    public final int value;
    public Num(int v) {
        super(Tag.NUM);
        value = v;
    }
    
    public String toString() {
        return "Num: " + this.value + "(" + this.tag + ")";
    }
}
