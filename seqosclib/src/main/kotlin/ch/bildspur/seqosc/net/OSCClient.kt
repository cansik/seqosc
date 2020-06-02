package ch.bildspur.seqosc.net

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class OSCClient {

    val socket = DatagramSocket()

    init {
        socket.broadcast = true
    }

    fun send(address : InetAddress, port : Int, packet : OSCPacket) {
        val datagramPacket = DatagramPacket(packet.data, 0, packet.data.size, address, port)
        socket.send(datagramPacket)
    }
}