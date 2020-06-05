namespace SeqOSC.Net
{
    public class OSCPacket
    {
        public OSCPacket(byte[] data)
        {
            Data = data;
        }

        public byte[] Data { get; private set; }
    }
}