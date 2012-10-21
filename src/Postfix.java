import java.io.*;
import java.net.URL;

import exception.SyntaxError;

public class Postfix {
    public static void main(String [] args) throws IOException {
        try {
            URL url = Postfix.class.getResource("input.txt");
            if (url != null) {
                Parser parse = new Parser(url.getPath(), "input/output.txt");
                parse.expr();
                parse.finish();
            }
        }
        catch (SyntaxError e) {
            System.out.println("Syntax Error: " + e.getMessage());
        }
    }
}
