using System.Net;
using System.Net.Sockets;

namespace SeqOSC.Net
{
    public class OSCClient
    {
        public UdpClient Client { get; private set; } = new UdpClient();

        public void Send(IPEndPoint endPoint, OSCPacket packet)
        {
            Client.Send(packet.Data, packet.Data.Length, endPoint);
        }
    }
}