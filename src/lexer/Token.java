package lexer;

/**
 * Represents a tuple (tag, attributes) used when performing lexical analysis.
 * The tag represents a terminal symbol that is used for making parsing decisions, 
 * and attributes are included to provide necessary information about the tag, e.g.
 * a token (num, 31) tells us that the token is a number with a value = 31.
 * @author ktraff
 *
 */
public class Token {
    public final int tag;
    public Token(int t) {
        this.tag = t;
    }
}
