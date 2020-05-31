package ch.bildspur.seqosc.net

import ch.bildspur.event.Event
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import kotlin.concurrent.thread

class OSCServer(val port : Int, val bufferSize : Int = 9216) {

    val onPacketReceived = Event<OSCPacket>()

    @Volatile var running = false
        private set

    private val buffer = ByteBuffer.allocate(bufferSize)
    private lateinit var thread : Thread

    fun open() {
        if(running)
            return

        running = true

        thread = thread(start = true, isDaemon = true) {
            val socket = DatagramSocket(null)

            socket.broadcast = true
            socket.receiveBufferSize = bufferSize
            socket.reuseAddress = true

            socket.bind(InetSocketAddress(port))

            while (running) {
                val packet = DatagramPacket (buffer.array(), buffer.capacity())
                socket.receive(packet)

                if(packet.length == 0)
                    continue

                buffer.limit(packet.length)
                val data = ByteArray(buffer.remaining())
                buffer.get(data)
                buffer.clear()

                onPacketReceived(OSCPacket(data))
            }

            socket.close()
        }
    }

    fun close() {
        if(!running)
            return

        running = false
        thread.join(5000)
    }
}