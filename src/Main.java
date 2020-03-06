import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {
    public static void main(String args[]) throws IOException {
        KWayMergeSort k = new KWayMergeSort();
        String[] runs = k.DivideInputFileIntoRuns("Index.bin",4);
        k.DoKWayMergeAndWriteASortedFile(runs,4,"");
        k.SortEachRunOnMemoryAndWriteItBack(runs);
    }
}
