package lexer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import util.Reader;
import util.ReaderFactory;
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
    Reader reader;
    Writer writer;
    
    public Lexer(String input, String output) {
        reader = new ReaderFactory(input).create();
        writer = new WriterFactory(output).create();
        this.reserve(new Word(Tag.TRUE, "true"));
        this.reserve(new Word(Tag.FALSE, "false"));
        // relational operators
        this.reserve(new Word(Tag.LESS_THAN, "<"));
        this.reserve(new Word(Tag.LTE, "<="));
        this.reserve(new Word(Tag.EQUAL_TO, "=="));
        this.reserve(new Word(Tag.NOT_EQUAL_TO, "!="));
        this.reserve(new Word(Tag.GTE, ">="));
        this.reserve(new Word(Tag.GREATER_THAN, ">"));
    }
    
    public void reserve(Word t) {
        words.put(t.lexeme, t);
    }
    
    public Token scan() {
        while (!this.isToken(this.peek)) {
            this.parseWhitespace();
            this.parseComments();
        }
        // parse digits
        if (this.isNum(this.peek)) {
            return this.parseNum();
        }
        // parse reserved words and identifiers
        else if (Character.isLetter(this.peek)) {
            return this.parseWord();
        }
        else if (this.isRelationalOperator(this.peek)) {
            return this.parseRelationalOperator();
        }
        // return the current character as a token and reset peek to whitespace
        // to be stripped during the next scan
        Token t = new Token(this.peek);
        this.peek = ' ';
        return t;
    }
    
    /**
     * Parses through whitespace characters until a non-whitespace character is reached
     */
    public void parseWhitespace() {
        for ( ; ; this.peek = (char)reader.read()) {
            if (this.peek == ' ' || this.peek == '\t' || 
                this.peek == '\r') continue;
            else if (this.peek == '\n') this.line++;
            else break;
        }
    }
    
    /**
     * Parses through single and multiline comments
     */
    public void parseComments() {
        if (this.peek == '/') {
            this.peek = reader.read();
            if (this.peek == '/') {
                // single-line comment '//'
                while (true) {
                    this.peek = (char)reader.read();
                    if (this.peek == '\n') {
                        this.line++;
                        this.peek = (char)reader.read();
                        break;
                    }
                }
            }
            else if (this.peek == '*') {
                // multi-line comment '/* */'
                while (true) {
                    this.peek = (char)reader.read();
                    if (this.peek == '\n') this.line++;
                    else if (this.peek == '*') {
                        this.peek = (char)reader.read();
                        if (this.peek == '/') {
                            this.peek = (char)reader.read();
                            break;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Parses a string of characters that represent a numeric value.
     * @return a number token representing the string of characters
     */
    public Num parseNum() {
        int value = 0;
        while (Character.isDigit(this.peek)) {
            value = 10 * value + Character.digit(this.peek, 10);
            this.peek = (char)reader.read();
        }
        // parse floating point number if applicable
        if (this.peek == '.') {
            this.peek = (char)reader.read();
            double floatingPointValue = (double)value;
            int exp = -1;
            while (Character.isDigit(this.peek)){
                floatingPointValue = floatingPointValue + ((double)Character.digit(this.peek, 10) * Math.pow(10, exp--));
                this.peek = (char)reader.read();
            }
            return new Double(floatingPointValue);
        }
        return new Num(value);
    }
    
    /**
     * Parses a string of characters that represent a relational operator.
     * @return a word token representing the relational operator
     */
    public Word parseRelationalOperator() {
        StringBuilder lexeme = new StringBuilder();
        do {
            lexeme.append(this.peek);
            this.peek = (char)reader.read();
        } while (this.isRelationalOperator(this.peek));
        String wordStr = lexeme.toString();
        Word savedWord = (Word)this.words.get(wordStr);
        if (savedWord != null) return savedWord;
        // the word has not been saved, create a new one
        Word newWord = new Word(Tag.ID, wordStr);
        this.words.put(wordStr, newWord);
        return newWord;
    }
    
    /**
     * Parses a string of characters that represent a reserved word or identifier.
     * @return a word token representing the string of characters
     */
    public Word parseWord() {
        StringBuilder lexeme = new StringBuilder();
        do {
            lexeme.append(this.peek);
            this.peek = (char)reader.read();
        } while (Character.isLetterOrDigit(this.peek));
        String wordStr = lexeme.toString();
        Word savedWord = (Word)this.words.get(wordStr);
        if (savedWord != null) return savedWord;
        // the word has not been saved, create a new one
        Word newWord = new Word(Tag.ID, wordStr);
        this.words.put(wordStr, newWord);
        return newWord;
    }
    
    /**
    * returns true if a character represents the beginning of a number
    * @param c
    * @return
    */
   public boolean isNum(char c) {
       return Character.isDigit(c) || 
              c == '.';
   } 
    
    /**
     * returns true if a character represents the beginning of a Token
     * @param c
     * @return
     */
    public boolean isToken(char c) {
        return Character.isLetter(c) || 
               this.isNum(c) ||
               this.isRelationalOperator(c);
    }
    
    /**
     * returns true if a character represents the beginning of a relational operator
     * @param c
     * @return
     */
    public boolean isRelationalOperator(char c) {
        return c == '<' || c == '=' || c == '!' || c == '>';
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
