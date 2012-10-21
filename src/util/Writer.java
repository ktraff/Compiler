package util;
import java.io.*;

/**
 * Base class for all writers, writes to standard output by default
 * @author ktraff
 *
 */
public class Writer {
    public void write(String str) {
        System.out.println(str);
    }
    
    public void write(char c) {
        System.out.println(c);
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
class FileWriter extends Writer {
    String filePath;
    BufferedWriter writer;
    
    public FileWriter(String filePath){
        super();
        this.filePath = filePath;
        try {
            writer = new BufferedWriter(new java.io.FileWriter(this.filePath));
        }
        catch (IOException e) {
            System.out.println("Error writing to " + this.filePath + ": " + e.getMessage());
        }
    }
    
    public void write(String str){
        try {
            writer.write(str);
        }
        catch (IOException e) {
            System.out.println("Error writing to " + this.filePath + ": " + e.getMessage());
        }
    }
    
    public void write(char c){
        try {
            writer.write(c);
        }
        catch (IOException e) {
            System.out.println("Error writing to " + this.filePath + ": " + e.getMessage());
        }
    }
    
    public void close() {
        try {
            this.writer.close();
        }
        catch (IOException e) {
            System.out.println("Error closing writer: " + e.getMessage());
        }
    }
}
