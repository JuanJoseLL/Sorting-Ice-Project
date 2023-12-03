import java.io.BufferedReader;

public class FileMergeHelper implements Comparable<FileMergeHelper>{

    String line;
    BufferedReader reader;

    public FileMergeHelper(String line, BufferedReader reader) {
        this.line = line;
        this.reader = reader;
    }

    @Override
    public int compareTo(FileMergeHelper o) {
        return this.line.compareTo(o.line);
    }
}
