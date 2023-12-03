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
    sequence<byte> ByteSeq;

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
                void attachWorker(Worker* subscriber);
                void addPartialResult(ByteSeq res);
                void deattachWorker(Worker* subscriber);
                string getTask();
                void initiateSort(bool flag);

       }
       interface CallbackFile  {
            void fileReadStat(bool flag);
            void processFile();
            ByteSeq readData();
       };

}