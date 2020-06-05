using SeqOSC.Net;

namespace SeqOSC
{
    public class OSCSample
    {
        public OSCSample(OSCPacket packet, long timestamp)
        {
            Packet = packet;
            Timestamp = timestamp;
        }

        public long Timestamp { get; private set; }
        public OSCPacket Packet { get; private set; }
    }
}