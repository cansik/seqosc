package ch.bildspur.seqosc

import ch.bildspur.seqosc.net.OSCPacket
import java.nio.ByteBuffer
import java.nio.ByteOrder

class OSCBuffer {
    val samples = mutableListOf<OSCSample>()

    fun asByteBuffer() : ByteBuffer {
        // raw size: delta(64bit int) + length(32bit int) + data(?)
        val data = ByteBuffer.allocate((samples.size * (4 + 8)) + samples.sumBy { it.packet.data.size })
        data.order(ByteOrder.LITTLE_ENDIAN)

        samples.forEach {
            data.putLong(it.delta)
            data.putInt(it.packet.data.size)
            data.put(it.packet.data)
        }

        return data
    }

    fun fromByteBuffer(data : ByteBuffer) {
        data.order(ByteOrder.LITTLE_ENDIAN)

        while(data.hasRemaining()) {
            val delta = data.long
            val length = data.int
            val subpart = data.slice().limit(length)

            val packetData = ByteArray(subpart.remaining())
            subpart.get(packetData)

            val packet = OSCPacket(packetData)
            data.position(data.position() + length)

            samples.add(OSCSample(delta, packet))
        }
    }
}