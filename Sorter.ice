module Demo
{
    interface Hello
    {
        void sayHello();
        void shutdown();
    }
    interface Clock
        {
            void tick(string time);
        }
    sequence<string> Data;
    sequence<long> StringSeq;

    struct Block {
          ["java:type:java.util.ArrayList<Long>"]Data data;
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