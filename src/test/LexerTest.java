package test;

import static org.junit.Assert.*;

import java.util.Hashtable;

import lexer.Lexer;
import lexer.Num;
import lexer.Double;
import lexer.Tag;
import lexer.Token;
import lexer.Word;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LexerTest {
    Lexer lexer;
    
    @Before
    public void setup() {
        lexer = new Lexer("input/lexer/default", "console");
    }
    
    @After
    public void teardown() {
        lexer = null;
    }
    
    @Test
    public void testLexer() {
        Hashtable<String, Token> words = this.lexer.getWords();
        assertEquals(2, words.size());
        // ensure true and false Tags are reserved
        assertEquals(Tag.TRUE, words.get("true").tag);
        assertEquals(Tag.FALSE, words.get("false").tag);
    }
    
    @Test
    public void testReserve() {
        Hashtable<String, Token> words = this.lexer.getWords();
        lexer.reserve(new Word(Tag.ID, "reserved"));
        assertEquals(Tag.ID, words.get("reserved").tag);
    }

    @Test
    public void testScan() {
        lexer = new Lexer("input/lexer/default", "console");
        
        Word trueWord = (Word)lexer.scan();
        assertEquals(Tag.TRUE, trueWord.tag);
        assertEquals("true", trueWord.lexeme);
        
        Num numTag = (Num)lexer.scan();
        assertEquals(Tag.NUM, numTag.tag);
        assertEquals(1, numTag.value);
        
        Word falseWord = (Word)lexer.scan();
        assertEquals(Tag.FALSE, falseWord.tag);
        assertEquals("false", falseWord.lexeme);
        
        numTag = (Num)lexer.scan();
        assertEquals(Tag.NUM, numTag.tag);
        assertEquals(3, numTag.value);
        
        Word anIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anIdentifier.tag);
        assertEquals("anIdentifier", anIdentifier.lexeme);
    }

    @Test
    public void testParseWhitespace() {
        lexer = new Lexer("input/lexer/whitespace", "console");
        
        Word anIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anIdentifier.tag);
        assertEquals("aToken", anIdentifier.lexeme);
        
        lexer.parseWhitespace();
        assertEquals(11, lexer.getLine());
        Word trueWord = (Word)lexer.scan();
        assertEquals(Tag.TRUE, trueWord.tag);
        assertEquals("true", trueWord.lexeme);
        assertEquals(11, lexer.getLine());
        Word falseWord = (Word)lexer.scan();
        assertEquals(Tag.FALSE, falseWord.tag);
        assertEquals("false", falseWord.lexeme);
        assertEquals(11, lexer.getLine());
        lexer.parseWhitespace();
        assertEquals(11, lexer.getLine());
        Word anotherIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anotherIdentifier.tag);
        assertEquals("anotherToken", anotherIdentifier.lexeme);
    }
    
    @Test
    public void testParseSingleLineComments() {
        lexer = new Lexer("input/lexer/single-line-comment", "console");
        
        // assert the lexer skips over the single-line comments        
        Num numTag = (Num)lexer.scan();
        assertEquals(Tag.NUM, numTag.tag);
        assertEquals(1, numTag.value);
        
        Word anIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anIdentifier.tag);
        assertEquals("anIdentifier", anIdentifier.lexeme);
    }
    
    @Test
    public void testParseMultiLineComments() {
        lexer = new Lexer("input/lexer/multi-line-comment", "console");
        
        // assert the lexer skips over the multi-line comments        
        Num numTag = (Num)lexer.scan();
        assertEquals(Tag.NUM, numTag.tag);
        assertEquals(2, numTag.value);
        
        Word anIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anIdentifier.tag);
        assertEquals("anIdentifier", anIdentifier.lexeme);
        
        Word falseWord = (Word)lexer.scan();
        assertEquals(Tag.FALSE, falseWord.tag);
        assertEquals("false", falseWord.lexeme);
    }
    
    @Test
    /**
     * assert the lexer skips over single and multi-line comments
     */
    public void testComments() {
        lexer = new Lexer("input/lexer/comment", "console");
        
        Num numTag = (Num)lexer.scan();
        assertEquals(Tag.NUM, numTag.tag);
        assertEquals(2, numTag.value);
        
        Word anIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anIdentifier.tag);
        assertEquals("anIdentifier", anIdentifier.lexeme);
        
        Word falseWord = (Word)lexer.scan();
        assertEquals(Tag.FALSE, falseWord.tag);
        assertEquals("false", falseWord.lexeme);
    }

    @Test
    public void testParseOperators() {
        lexer = new Lexer("input/lexer/operators", "console");
        
        Word anIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anIdentifier.tag);
        assertEquals("anIdentifier", anIdentifier.lexeme);
        
        Word operator = (Word)lexer.scan();
        assertEquals(Tag.LESS_THAN, operator.tag);
        assertEquals("<", operator.lexeme);
        
        Word anotherIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anotherIdentifier.tag);
        assertEquals("anotherIdentifier", anotherIdentifier.lexeme);
        
        anIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anIdentifier.tag);
        assertEquals("anIdentifier", anIdentifier.lexeme);
        
        operator = (Word)lexer.scan();
        assertEquals(Tag.LTE, operator.tag);
        assertEquals("<=", operator.lexeme);
        
        anotherIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anotherIdentifier.tag);
        assertEquals("anotherIdentifier", anotherIdentifier.lexeme);
        
        anIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anIdentifier.tag);
        assertEquals("anIdentifier", anIdentifier.lexeme);
        
        operator = (Word)lexer.scan();
        assertEquals(Tag.EQUAL_TO, operator.tag);
        assertEquals("==", operator.lexeme);
        
        anotherIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anotherIdentifier.tag);
        assertEquals("anotherIdentifier", anotherIdentifier.lexeme);
        
        anIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anIdentifier.tag);
        assertEquals("anIdentifier", anIdentifier.lexeme);
        
        operator = (Word)lexer.scan();
        assertEquals(Tag.NOT_EQUAL_TO, operator.tag);
        assertEquals("!=", operator.lexeme);
        
        anotherIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anotherIdentifier.tag);
        assertEquals("anotherIdentifier", anotherIdentifier.lexeme);
        
        anIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anIdentifier.tag);
        assertEquals("anIdentifier", anIdentifier.lexeme);
        
        operator = (Word)lexer.scan();
        assertEquals(Tag.GTE, operator.tag);
        assertEquals(">=", operator.lexeme);
        
        anotherIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anotherIdentifier.tag);
        assertEquals("anotherIdentifier", anotherIdentifier.lexeme);
        
        anIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anIdentifier.tag);
        assertEquals("anIdentifier", anIdentifier.lexeme);
        
        operator = (Word)lexer.scan();
        assertEquals(Tag.GREATER_THAN, operator.tag);
        assertEquals(">", operator.lexeme);
        
        anotherIdentifier = (Word)lexer.scan();
        assertEquals(Tag.ID, anotherIdentifier.tag);
        assertEquals("anotherIdentifier", anotherIdentifier.lexeme);
    }

    /**
     * Tests the ability of the lexer to parse floating point numbers
     */
    @Test
    public void testParseDouble() {
        lexer = new Lexer("input/lexer/double", "console");
        
        Double numTag = (Double)lexer.scan();
        assertEquals(Tag.NUM, numTag.tag);
        assertEquals(15.23, numTag.value, 0.1);
        
        numTag = (Double)lexer.scan();
        assertEquals(Tag.NUM, numTag.tag);
        assertEquals(2., numTag.value, 0.1);
        
        numTag = (Double)lexer.scan();
        assertEquals(Tag.NUM, numTag.tag);
        assertEquals(.35, numTag.value, 0.1);
    }
    
    @Test
    public void testParseWord() {
        fail("Not yet implemented");
    }

}
