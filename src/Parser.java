import java.io.*;

import util.Writer;
import util.WriterFactory;
import exception.*;

/**
 * A recursive descent parser
 * @author ktraff
 *
 */
public class Parser {
    // lookahead is an integer (not a character) so that not only single 
    // character tokens can be introduced
    static int lookahead;
    BufferedReader reader;
    Writer writer;
    
    public Parser(String filePath, String outputPath) throws IOException {
        reader = new BufferedReader(new FileReader(filePath));
        writer = new WriterFactory(outputPath).create();
        lookahead = reader.read();
    }
    
    /**
     * Parses an expression -> term rest
     * 
     * @throws IOException
     */
    public void expr() throws IOException {
        term();
        rest();
    }
    
    /**
     * Parses a term ->
     * 0 | .. | 9
     * 
     * @throws IOException
     */
    public void term() throws IOException, SyntaxError {
        if (Character.isDigit((char) lookahead)) {
            writer.write((char) lookahead);
            match(lookahead);
        }
    }
    
    /**
     * Parses a chunk of statements -> + term { print('+') } rest
     * | - term { print('-') } rest
     * | empty
     * 
     * @throws IOException
     */
    public void rest() throws IOException {
        while (true) {
            if (lookahead == '+') {
                match('+'); term(); writer.write('+');
                continue;
            }
            else if (lookahead == '-') {
                match('-'); term(); writer.write('-');
                continue;
            }
            break;
        }
    }
    
    public void match(int t) throws IOException, SyntaxError {
        if (t == lookahead) { // Progress the lookahead
            lookahead = reader.read();
        }
        else throw new SyntaxError("syntax error");
    }
    
    public void finish() throws IOException {
        writer.close();
    }
}
