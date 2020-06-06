using SeqOSC.Net;

namespace SeqOSC
{
    public class OSCSample
    {
        public OSCSample(long timestamp, OSCPacket packet)
        {
            Timestamp = timestamp;
            Packet = packet;
        }

        public long Timestamp { get; private set; }
        public OSCPacket Packet { get; private set; }
    }
}