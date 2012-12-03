package lexer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import symbols.Type;
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
    
    public Lexer() {
        reader = new ReaderFactory("input/lexer/default").create();
        writer = new WriterFactory("console").create();
        this.initReservedTokens();
    }
    
    public Lexer(String input, String output) {
        reader = new ReaderFactory(input).create();
        writer = new WriterFactory(output).create();
        this.initReservedTokens();
    }
    
    public void initReservedTokens() {
        this.reserve(new Word("if", Tag.IF));
        this.reserve(new Word("else", Tag.ELSE));
        this.reserve(new Word("while", Tag.WHILE));
        this.reserve(new Word("do", Tag.DO));
        this.reserve(new Word("break", Tag.BREAK));
        this.reserve(Word.True); this.reserve(Word.False);
        this.reserve(Type.Int); this.reserve(Type.Char);
        this.reserve(Type.Bool); this.reserve(Type.Float);
    }
    
    public void reserve(Word t) {
        words.put(t.lexeme, t);
    }
    
    void readch() throws IOException { peek = (char)System.in.read(); }
    
    /**
     * Used to read composite tokens, e.g. if '>' is read, this method can be called
     * to check if the next character is '='
     * @param c
     * @return
     * @throws IOException
     */
    boolean readch(char c) throws IOException {
       readch();
       if( peek != c ) return false;
       peek = ' ';
       return true;
    }
    
    public Token scan() throws IOException {
       for( ; ; readch() ) {
          if( peek == ' ' || peek == '\t' ) continue;
          else if( peek == '\n' ) line = line + 1;
          else break;
       }
       switch( peek ) {
       case '&':
          if( readch('&') ) return Word.and;  else return new Token('&');
       case '|':
          if( readch('|') ) return Word.or;   else return new Token('|');
       case '=':
          if( readch('=') ) return Word.eq;   else return new Token('=');
       case '!':
          if( readch('=') ) return Word.ne;   else return new Token('!');
       case '<':
          if( readch('=') ) return Word.le;   else return new Token('<');
       case '>':
          if( readch('=') ) return Word.ge;   else return new Token('>');
       }
       if( Character.isDigit(peek) ) {
          int v = 0;
          do {
             v = 10*v + Character.digit(peek, 10); readch();
          } while( Character.isDigit(peek) );
          if( peek != '.' ) return new Num(v);
          float x = v; float d = 10;
          for(;;) {
             readch();
             if( ! Character.isDigit(peek) ) break;
             x = x + Character.digit(peek, 10) / d; d = d*10;
          }
          return new Real(x);
       }
       if( Character.isLetter(peek) ) {
          StringBuffer b = new StringBuffer();
          do {
             b.append(peek); readch();
          } while( Character.isLetterOrDigit(peek) );
          String s = b.toString();
          Word w = (Word)words.get(s);
          if( w != null ) return w;
          w = new Word(s, Tag.ID);
          words.put(s, w);
          return w;
       } 
       Token tok = new Token(peek); peek = ' ';
       return tok;
    }
    
    /*public Token scan() {
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
    }*/
    
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
        Word newWord = new Word(wordStr, Tag.ID);
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
        Word newWord = new Word(wordStr, Tag.ID);
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
