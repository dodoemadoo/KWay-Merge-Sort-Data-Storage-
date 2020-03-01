import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {
    public static void main(String args[]) throws IOException {
        KWayMergeSort k = new KWayMergeSort();
        String[] runs = k.DivideInputFileIntoRuns("Index.bin",2);
        k.SortEachRunOnMemoryAndWriteItBack(runs);
    }
}
