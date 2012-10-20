package lexer;

import java.io.IOException;
import java.util.Hashtable;

/**
 * Contains a scanning function that converts character streams into token objects
 * @author ktraff
 *
 */
public class Lexer {
    private int line = 1;
    private char peek = ' ';
    private Hashtable<String, Token> words = new Hashtable<String, Token>();
    
    public Lexer() {
        this.reserve(new Word(Tag.TRUE, "true"));
        this.reserve(new Word(Tag.FALSE, "false"));
    }
    
    public void reserve(Word t) {
        words.put(t.lexeme, t);
    }
    
    public Token scan() throws IOException {
        // skip whitespace
        for ( ; ; this.peek = (char)System.in.read()) {
            if (this.peek == ' ' || this.peek == '\t') continue;
            else if (this.peek == '\n') this.line++;
            else break;
        }
        // parse digits
        if (Character.isDigit(this.peek)) {
            int value = 0;
            do {
                value = 10 * value + Character.digit(this.peek, 10);
                this.peek = (char)System.in.read();
            } while (Character.isDigit(this.peek));
            return new Num(value);
        }
        // parse reserved words and identifiers
        if (Character.isLetter(this.peek)) {
            StringBuilder lexeme = new StringBuilder();
            do {
                lexeme.append(this.peek);
                this.peek = (char)System.in.read();
            } while (Character.isLetterOrDigit(this.peek));
            String wordStr = lexeme.toString();
            Word savedWord = (Word)this.words.get(wordStr);
            if (savedWord != null) return savedWord;
            // the word has not been saved, create a new one
            Word newWord = new Word(Tag.ID, wordStr);
            this.words.put(wordStr, newWord);
            return newWord;
        }
        // return the current character as a token and reset peek to whitespace
        // to be stripped during the next scan
        Token t = new Token(this.peek);
        this.peek = ' ';
        return t;
    }
}
