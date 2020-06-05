namespace SeqOSC
{
    public static class SeqOSCUtils
    {
        public static bool getFlag(this int data, int position)
        {
            return (data & (1 << position-1)) != 0;
        }
    }
}