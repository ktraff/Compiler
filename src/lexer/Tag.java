package lexer;

/**
 * Contains integer constants to map terminals to an integer value.
 * @author ktraff
 *
 */
public class Tag {
    public final static int
        NUM = 256, ID = 257, TRUE = 258, FALSE = 259, COMMENT = 260,
        // relational operators
        LESS_THAN = 261, LTE = 262, EQUAL_TO = 263, NOT_EQUAL_TO = 264, GTE = 265, GREATER_THAN = 266;
}
