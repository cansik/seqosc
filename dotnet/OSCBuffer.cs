using System;
using System.Collections.Generic;
using System.IO;

namespace SeqOSC
{
    public class OSCBuffer
    {
        public string Comment { get; set; } = "";
        public float Speed { get; set; } = 1.0f;
        public List<OSCSample> Samples { get; set; } = new List<OSCSample>();

        public void Read(byte[] data)
        {
            if (BitConverter.IsLittleEndian)
                Array.Reverse(data);
            
            var ptr = 0;
        }
    }
}
