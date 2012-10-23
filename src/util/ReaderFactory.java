package util;
/**
 * Classes to direct compiler output to different output streams, such as standard out
 * or a file.
 * @author ktraff
 *
 */
public class ReaderFactory {
    String outputPath;
    
    public ReaderFactory(String outputPath) {
        this.outputPath = outputPath;
    }
    
    public Reader create() {
        if (this.outputPath.equalsIgnoreCase("console")) {
            return new Reader();
        }
        else {
            return new FileReader(outputPath);
        }
    }
}