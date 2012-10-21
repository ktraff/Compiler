package util;
/**
 * Classes to direct compiler output to different output streams, such as standard out
 * or a file.
 * @author ktraff
 *
 */
public class WriterFactory {
    String outputPath;
    
    public WriterFactory(String outputPath) {
        this.outputPath = outputPath;
    }
    
    public Writer create() {
        if (this.outputPath.equalsIgnoreCase("console")) {
            return new Writer();
        }
        else {
            return new FileWriter(outputPath);
        }
    }
}