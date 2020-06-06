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

        public bool Loop { get; set; } = false;
        public string Host { get; set; } = IPAddress.Loopback.ToString();
        public int Port { get; set; } = 8000;

        public OSCClient Client { get; set; }

        public bool IsPlaying => _playing;

        public int Position => _position;

        private volatile bool _playing = false;
        
        private volatile int _position = 0;

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
            _position = 0;

            var receiver = new IPEndPoint(IPAddress.Parse(Host), Port);
            var lastTimeStamp = Buffer.Samples.First().Timestamp;

            while (_position < Buffer.Samples.Count && _playing)
            {
                var sample = Buffer.Samples[_position++];
                var delta = sample.Timestamp - lastTimeStamp;
                
                Thread.Sleep((int)Math.Round(delta / Speed));
                Client.Send(receiver, sample.Packet);

                lastTimeStamp = sample.Timestamp;

                if (Loop && _position >= Buffer.Samples.Count)
                {
                    lastTimeStamp = Buffer.Samples.First().Timestamp;
                    _position = 0;
                }
            }

            _playing = false;
        }

        public void Stop()
        {
            _playing = false;
        }
    }
}