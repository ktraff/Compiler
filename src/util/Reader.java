package util;

import java.io.*;
import java.net.URL;

/**
 * Base class for all writers, writes to standard output by default
 * @author ktraff
 *
 */
public class Reader {
    public char read() {
        try {
            return (char)System.in.read();
        } catch (IOException e) {
            System.out.println("Error reading from standard input: " + e.getMessage());
            return ' ';
        }
    }
    
    public void close() {
        // NOOP
    }
}

/**
 * Writes to a file designated by the file path
 * @author ktraff
 *
 */
class FileReader extends Reader {
    String filePath;
    BufferedReader reader;
    
    public FileReader(String filePath){
        super();
        this.filePath = filePath;
        try {
            reader = new BufferedReader(new java.io.FileReader(filePath));
        }
        catch (IOException e) {
            System.out.println("Error creating reader for " + this.filePath + ": " + e.getMessage());
        }
    }
    
    public char read(){
        try {
            return (char)reader.read();
        }
        catch (IOException e) {
            System.out.println("Error reading from " + this.filePath + ": " + e.getMessage());
            return ' ';
        }
    }
    
    public void close() {
        try {
            this.reader.close();
        }
        catch (IOException e) {
            System.out.println("Error closing reader: " + e.getMessage());
        }
    }
}
