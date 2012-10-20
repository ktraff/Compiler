package lexer;

/**
 * Represents a token for reserved words and identifiers
 * @author ktraff
 *
 */
public class Word extends Token {
    public final String lexeme;
    public Word(int t, String s) {
        super(t);
        lexeme = s;
    }
}
