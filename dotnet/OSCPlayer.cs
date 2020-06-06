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
        public float Speed { get; set; } = 1.0f;
        public string Host { get; set; } = IPAddress.Loopback.ToString();
        public int Port { get; set; } = 8000;
        
        public OSCClient Client { get; set; }

        public bool IsPlaying => _playing;

        private volatile bool _playing = false;

        public OSCPlayer()
        {
            Client = new OSCClient();
        }
        
        public OSCPlayer(OSCBuffer buffer) : this()
        {
            Buffer = buffer;
        }

        public void Play()
        {
            if (Buffer.Samples.Count == 0)
                return;
            
            _playing = true;

            var receiver = new IPEndPoint(IPAddress.Parse(Host), Port);
            var lastTimeStamp = Buffer.Samples.First().Timestamp;

            foreach (var sample in Buffer.Samples)
            {
                var delta = sample.Timestamp - lastTimeStamp;
                
                Thread.Sleep((int)Math.Round(delta / Speed));
                Client.Send(receiver, sample.Packet);

                lastTimeStamp = sample.Timestamp;
            }

            _playing = false;
        }
    }
}