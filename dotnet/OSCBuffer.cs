using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO;
using SeqOSC.Net;
using UnityEngine;

namespace SeqOSC
{
    public class OSCBuffer
    {
        public string Comment { get; set; } = "";
        public float Speed { get; set; } = 1.0f;
        public List<OSCSample> Samples { get; set; } = new List<OSCSample>();

        public void Read(byte[] data)
        {
            using (var reader = new BinaryReader(new MemoryStream(data)))
            {
                // read header
                var flags = reader.ReadInt32();
                var compressed = flags.getFlag(0);

                var sampleCount = reader.ReadInt32();
                sampleCount = sampleCount < 0 ? int.MaxValue : sampleCount;

                var payloadLength = reader.ReadInt32();

                Speed = reader.ReadSingle();
                Comment = System.Text.Encoding.UTF8.GetString(reader.ReadBytes(reader.ReadInt32()));

                // read payload
                var payload = reader.ReadBytes((int) (data.Length - reader.BaseStream.Position));

                if (compressed)
                    payload = payload.Uncompress();

                var count = 0;
                using (var payloadReader = new BinaryReader(new MemoryStream(payload)))
                {
                    // todo: check if can read is the right thing
                    while (count < sampleCount && payloadReader.BaseStream.HasRemaining())
                    {
                        var timestamp = payloadReader.ReadInt64();
                        var length = payloadReader.ReadInt32();
                        var packetData = payloadReader.ReadBytes(length);
                        var packet = new OSCPacket(packetData);

                        Samples.Add(new OSCSample(timestamp, packet));
                        count++;
                    }
                }
            }
        }
    }
}
