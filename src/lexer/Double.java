package lexer;

/**
 * A token that represents a floating point number (denoted by it's value)
 * @author ktraff
 *
 */
public class Double extends Num {
    public final double value;
    public Double(double v) {
        super(Tag.NUM);
        value = v;
    }
    
    public String toString() {
        return "Double: " + this.value + "(" + this.tag + ")";
    }
}
