import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {
    public static void main(String args[]) throws IOException {
        KWayMergeSort k = new KWayMergeSort();
        String[] runs = k.DivideInputFileIntoRuns("Index.bin",4);
        runs = k.SortEachRunOnMemoryAndWriteItBack(runs);
        k.DoKWayMergeAndWriteASortedFile(runs,4,"");
//        RandomAccessFile r = new RandomAccessFile("Level10.bin","r");
//        for (int i = 0 ;i<16;i++)
//        {
//            System.out.println(i+" "+r.readInt()+" "+r.readInt());
//        }
//        r.close();
    }
}
