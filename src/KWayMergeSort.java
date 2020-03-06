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

    public static Integer[] removeTheElement(Integer[] arr, int index)
    {

        // If the array is empty
        // or the index is not in array range
        // return the original array
        if (arr == null
                || index < 0
                || index >= arr.length) {

            return arr;
        }

        // Create another array of size one less
        Integer[] anotherArray = new Integer[arr.length - 1];

        // Copy the elements except the index
        // from original array to the other array
        for (int i = 0, k = 0; i < arr.length; i++) {

            // if the index is
            // the removal element index
            if (i == index) {
                continue;
            }

            // if the index is not
            // the removal element index
            anotherArray[k++] = arr[i];
        }

        // return the resultant array
        return anotherArray;
    }

    public static RandomAccessFile[] removeTheElement(RandomAccessFile[] arr, int index)
    {

        // If the array is empty
        // or the index is not in array range
        // return the original array
        if (arr == null
                || index < 0
                || index >= arr.length) {

            return arr;
        }

        // Create another array of size one less
        RandomAccessFile[] anotherArray = new RandomAccessFile[arr.length - 1];

        // Copy the elements except the index
        // from original array to the other array
        for (int i = 0, k = 0; i < arr.length; i++) {

            // if the index is
            // the removal element index
            if (i == index) {
                continue;
            }

            // if the index is not
            // the removal element index
            anotherArray[k++] = arr[i];
        }

        // return the resultant array
        return anotherArray;
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
                }
                String currFileName = "Level"+levelNum+fileNum;
                nextLevel.add(currFileName);
                RandomAccessFile NextFile = new RandomAccessFile(currFileName,"rw");
                while (size != 0 && merge.length!=0)
                {
                    int min = Collections.min(Arrays.asList(merge));
                    int index = findIndex(merge,min) ;
                    NextFile.writeInt(min);
                    try {
                        currFiles[index].skipBytes(4);
                        if(currFiles[index].getFilePointer() == currFiles[index].length())
                        {
                            merge = removeTheElement(merge,index);
                            currFiles = removeTheElement(currFiles,index);
                        }
                        else
                            merge[index] = currFiles[index].readInt();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    size--;
                }
                fileNum++;
            }
            currLevel = new String[nextLevel.size()];
            for (int i =0; i < nextLevel.size(); i++)
                currLevel[i] = nextLevel.get(i);
            nextLevel.clear();
            levelNum++;
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
//test.
