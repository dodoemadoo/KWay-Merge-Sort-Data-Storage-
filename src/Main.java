import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) throws IOException {
        Scanner input=new Scanner(System.in);
        KWayMergeSort k = new KWayMergeSort();
        System.out.println("Enter size of runs");
        try{ //to avoid invalid input. 
        int numberOfRans= input.nextInt();
        String[] runs = k.DivideInputFileIntoRuns("Index.bin",numberOfRans);
        runs = k.SortEachRunOnMemoryAndWriteItBack(runs);
        System.out.println("Enter number of runs you want to merge at time");
        int KWayMerge= input.nextInt();
        System.out.println("");
        System.out.println("sorting...");
        System.out.println("");
        k.DoKWayMergeAndWriteASortedFile(runs,KWayMerge,"sorted.bin");
        RandomAccessFile r = new RandomAccessFile("sorted.bin","r");
        System.out.println("Sorted file: ");
        for (int i = 0 ;i<64;i++)
        {
            System.out.println(i+" "+r.readInt()+" "+r.readInt());
        }
        System.out.println("Done!");
        System.out.println("");
        System.out.println("Enter key to search for");
        int searchKey=input.nextInt();
        int result=k.BinarySearchOnSortedFile("sorted.bin",searchKey);
        if (result==-1)System.out.println("Sorry, Key not found :(' ");
        else System.out.println("offset of Key "+searchKey+":  "+result);
        r.close();
        }catch(Exception e){
            System.out.println("invalid input ");
        }
    }
}
