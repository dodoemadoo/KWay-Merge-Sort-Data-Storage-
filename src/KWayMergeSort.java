import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class KWayMergeSort {
    int findIndex(Integer[] arr, int t)
    {
        if(arr == null)
            return  -1;

        int len = arr.length;
        int i=0;

        while (i<len)
        {
            if (arr[i] == t){
                return i;
            }
            else
                i++;
        }
        return -1;
    }
    String [] DivideInputFileIntoRuns (String Inputfilename, int runSize) throws IOException {
        int numOfRuns= 64/runSize;
        int remRecords = 64%runSize;
        if(remRecords != 0)
            numOfRuns++;
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
                if (remRecords != 0 && i == numOfRuns-1 && j == remRecords-1 )
                    break;
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
    void DoKWayMergeAndWriteASortedFile(String [] SortedRunsNames, int K ,String Sortedfilename) throws IOException {
        RandomAccessFile sorted = new RandomAccessFile(Sortedfilename,"rw");
        String[] currLevel = SortedRunsNames;
        ArrayList<String> nextLevel = new ArrayList<String>();
        int levelNum=0,fileNum=0;
        int remFiles = SortedRunsNames.length%K;
        while (currLevel.length!=1)
        {
            for (int i=0;i<currLevel.length;i+=K)
            {
                RandomAccessFile[] currFiles  = new RandomAccessFile[K];
                Integer[] merge = new Integer[K];
                int size = 0 ;
                for(int j=i ; j<K ;j++)
                {
                    RandomAccessFile file = new RandomAccessFile(currLevel[j],"r");
                    currFiles[j-i] =  file;
                    merge[j-i] = file.readInt();
                    size += ((int)file.length()/8);
                    file.close();
                }
                String currFileName = "Level"+levelNum+fileNum;
                RandomAccessFile NextFile = new RandomAccessFile(currFileName,"rw");
                size -= K;
                while (size != 0 && merge.length!=0)
                {
                    int min = Collections.min(Arrays.asList(merge));
                    int index = findIndex(merge,min) ;
                    sorted.writeInt(min);
                    currFiles[index].skipBytes(4);
                    merge[index] = currFiles[index].readInt();
                    size--;
                }

            }
        }
    }
    int BinarySearchOnSortedFile(String Sortedfilename, int RecordKey) throws IOException {
        RandomAccessFile file = new RandomAccessFile(Sortedfilename,"r");
        int first=0;
        int last = (int) file.length()/8;
        int middle ;

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
