import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {
    public static void main(String args[]) throws IOException {
        KWayMergeSort k = new KWayMergeSort();
        String[] runs = k.DivideInputFileIntoRuns("Index.bin",3);
        runs = k.SortEachRunOnMemoryAndWriteItBack(runs);
        k.DoKWayMergeAndWriteASortedFile(runs,2,"sorted.bin");
        RandomAccessFile r = new RandomAccessFile("sorted.bin","r");
        for (int i = 0 ;i<64;i++)
        {
            System.out.println(i+" "+r.readInt()+" "+r.readInt());
        }
        r.close();
    }
}
