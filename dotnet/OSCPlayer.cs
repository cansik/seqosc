using System;
using System.Linq;
using System.Net;
using System.Threading;
using SeqOSC.Net;

namespace SeqOSC
{
    public class OSCPlayer
    {
        public OSCBuffer Buffer { get; set; }
        public float Speed { get; set; }
        public IPEndPoint Receiver { get; set; }
        
        public OSCClient Client { get; set; }

        public OSCPlayer(string host, int port, OSCBuffer buffer)
        {
            Receiver= new IPEndPoint(IPAddress.Parse(host), port);
            Buffer = buffer;
        }

        public void Play()
        {
            var lastTimeStamp = Buffer.Samples.First().Timestamp;

            foreach (var sample in Buffer.Samples)
            {
                var delta = sample.Timestamp - lastTimeStamp;
                
                Thread.Sleep((int)Math.Round(delta / Speed));
                Client.Send(Receiver, sample.Packet);

                lastTimeStamp = sample.Timestamp;
            }
        }
    }
}