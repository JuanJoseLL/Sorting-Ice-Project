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
}