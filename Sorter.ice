module Demo
{
    sequence<string> sorted;
    interface Worker{
        void processTask();
         ["java:type:java.util.ArrayList<String>"]sorted returnResult();
         void getData(["java:type:java.util.ArrayList<String>"]sorted srt);
    }
    sequence<string> Data;
    sequence<long> StringSeq;

    struct Block {
          ["java:type:java.util.ArrayList<String>"]Data data;
        }

        interface MetadataServer {
            string getStorageNodeForBlock(long blockId);
           ["java:type:java.util.ArrayList<Long>"]StringSeq getAvailableBlockIds();
        }

        interface StorageNode {
            Block readBlock(long blockId);
            void writeBlock(Block block);
        }
        sequence<string> result;
       interface MasterSorter{
                ["amd"]void attachWorker(Worker* subscriber);
                ["amd"]void addPartialResult(["java:type:java.util.ArrayList<String>"]result res);
                void deattachWorker(Worker* subscriber);
                ["amd"]string getTask();
                void initiateSort(bool flag);

       }
       interface CallbackFile  {
            void fileReadStat(bool flag);
            void processFile();
            ["java:type:java.util.ArrayList<String>"]result readData();
       };

}