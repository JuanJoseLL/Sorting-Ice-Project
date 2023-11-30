module Demo
{
    sequence<string> sorted;
    interface Worker{
        void processTask();
         ["java:type:java.util.ArrayList<String>"]sorted returnResult();
    }

    interface DataC {
        void read();
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



}