using System;
using System.IO;
using System.IO.Compression;

namespace SeqOSC
{
    public static class SeqOSCUtils
    {
        public static byte[] Compress(this byte[] data)
        {
            using (var compressedStream = new MemoryStream())
            using (var zipStream = new GZipStream(compressedStream, CompressionMode.Compress))
            {
                zipStream.Write(data, 0, data.Length);
                zipStream.Close();
                return compressedStream.ToArray();
            }
        }

        public static byte[] Uncompress(this byte[] data)
        {
            using (var compressedStream = new MemoryStream(data))
            using (var zipStream = new GZipStream(compressedStream, CompressionMode.Decompress))
            using (var resultStream = new MemoryStream())
            {
                zipStream.CopyTo(resultStream);
                return resultStream.ToArray();
            }
        }

        public static bool HasRemaining(this Stream stream)
        {
            return stream.Position < stream.Length;
        }
        
        public static bool getFlag(this int data, int position)
        {
            return ((data >> position) & 0xF) == 1;
        }
    }
}