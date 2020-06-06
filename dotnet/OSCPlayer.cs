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

        private volatile bool _playing = false;
        
        private volatile int _sampleIndex = 0;

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
            _sampleIndex = 0;

            var receiver = new IPEndPoint(IPAddress.Parse(Host), Port);
            var lastTimeStamp = Buffer.Samples.First().Timestamp;

            while (_sampleIndex < Buffer.Samples.Count && _playing)
            {
                var sample = Buffer.Samples[_sampleIndex++];
                var delta = sample.Timestamp - lastTimeStamp;
                
                Thread.Sleep((int)Math.Round(delta / Speed));
                Client.Send(receiver, sample.Packet);

                lastTimeStamp = sample.Timestamp;

                if (Loop && _sampleIndex >= Buffer.Samples.Count)
                {
                    lastTimeStamp = Buffer.Samples.First().Timestamp;
                    _sampleIndex = 0;
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