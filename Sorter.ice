module Demo
{

    interface Worker{
        void processTask(string task);
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

        interface MasterSorter{
            void attachWorker(Worker* subscriber);
            void addPartialResult(string res);
        }


}