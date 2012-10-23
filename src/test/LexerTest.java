package test;

import static org.junit.Assert.*;

import java.util.Hashtable;

import lexer.Lexer;
import lexer.Num;
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
        lexer = new Lexer("input/input.txt", "console");
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
        lexer = new Lexer("input/lexer-test-1", "console");
        
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
        fail("Not yet implemented");
    }

    @Test
    public void testParseNum() {
        fail("Not yet implemented");
    }

    @Test
    public void testParseWord() {
        fail("Not yet implemented");
    }

}
