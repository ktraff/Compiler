package lexer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import util.Writer;
import util.WriterFactory;

/**
 * Contains a scanning function that converts character streams into token objects
 * @author ktraff
 *
 */
public class Lexer {
    private int line = 1;
    private char peek = ' ';
    private Hashtable<String, Token> words = new Hashtable<String, Token>();
    BufferedReader reader;
    Writer writer;
    
    public Lexer(String filePath, String outputPath) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(filePath));
        writer = new WriterFactory(outputPath).create();
        this.reserve(new Word(Tag.TRUE, "true"));
        this.reserve(new Word(Tag.FALSE, "false"));
    }
    
    public void reserve(Word t) {
        words.put(t.lexeme, t);
    }
    
    public Token scan() throws IOException {
        this.parseWhitespace();
        // parse digits
        if (Character.isDigit(this.peek)) {
            return this.parseNum();
        }
        // parse reserved words and identifiers
        if (Character.isLetter(this.peek)) {
            return this.parseWord();
        }
        // return the current character as a token and reset peek to whitespace
        // to be stripped during the next scan
        Token t = new Token(this.peek);
        this.peek = ' ';
        return t;
    }
    
    /**
     * Parses through whitespace characters until a non-whitespace character is reached
     * @throws IOException
     */
    public void parseWhitespace() throws IOException {
        for ( ; ; this.peek = (char)System.in.read()) {
            if (this.peek == ' ' || this.peek == '\t') continue;
            else if (this.peek == '\n') this.line++;
            else break;
        }
    }
    
    /**
     * Parses a string of characters that represent a numeric value.
     * @return a number token representing the string of characters
     * @throws IOException
     */
    public Num parseNum() throws IOException{
        int value = 0;
        do {
            value = 10 * value + Character.digit(this.peek, 10);
            this.peek = (char)System.in.read();
        } while (Character.isDigit(this.peek));
        return new Num(value);
    }
    
    /**
     * Parses a string of characters that represent a reserved word or identifier.
     * @return a word token representing the string of characters
     * @throws IOException
     */
    public Word parseWord() throws IOException {
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

    public int getLine() {
        return line;
    }

    public char getPeek() {
        return peek;
    }

    public Hashtable<String, Token> getWords() {
        return words;
    }
}
