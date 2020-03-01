import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class KWayMergeSort {
    String [] DivideInputFileIntoRuns (String Inputfilename, int runSize) throws IOException {
        int numOfRuns= 64/runSize;
        String[] fileNames = new String[numOfRuns];
        for (int i = 0;i<numOfRuns;i++)
        {
            String currFile = "run"+i+".bin";
            RandomAccessFile index = new RandomAccessFile(Inputfilename,"r");
            RandomAccessFile run = new RandomAccessFile(currFile,"rw");
            fileNames[i] = currFile;
            // i * run size * record size;
            index.seek(i*runSize*8);
            for (int j=0;j<runSize;j++)
            {
                int key = index.readInt();
                int offset = index.readInt();
                run.writeInt(key);
                run.writeInt(offset);
            }
            index.close();
            run.close();
        }
        return fileNames;
    }
    String [] SortEachRunOnMemoryAndWriteItBack (String [] RunsFilesNames) throws IOException {
        TreeMap<Integer,Integer> keys;
        for (int i=0;i<RunsFilesNames.length;i++)
        {
            RandomAccessFile run = new RandomAccessFile(RunsFilesNames[i],"rw");
            int recNum = (int) (run.length())/8;
            keys = new TreeMap<Integer, Integer>();
            for (int j=0 ; j<recNum ; j++)
            {
                int key  =  run.readInt();
                int offset = run.readInt();
                keys.put(key,offset);
            }
            System.out.println(keys);
            run.seek(0);
            for (Map.Entry m:keys.entrySet())
            {
                run.writeInt((Integer) m.getKey());
                run.writeInt((Integer) m.getKey());
            }
            run.close();
        }

        return RunsFilesNames;
}
    void DoKWayMergeAndWriteASortedFile(String [] SortedRunsNames, int K ,String Sortedfilename)
    {

    }
    int BinarySearchOnSortedFile(String Sortedfilename, int RecordKey) throws IOException {
        RandomAccessFile file = new RandomAccessFile(Sortedfilename,"r");
        int first=0;
        int last = (int) file.length()/8;
        int middle = -1;

        //Loop until found or end of list.
        while(first <= last)
        {
            middle = (first + last) /2;
            file.seek(middle*8);
            int key = file.readInt();
            if(key==RecordKey)
                return middle*8;
            else
            {
                if(key > RecordKey)
                    last = middle -1;
                else
                    first = middle + 1;
            }
        }// end while

        return -1;
    }
}
