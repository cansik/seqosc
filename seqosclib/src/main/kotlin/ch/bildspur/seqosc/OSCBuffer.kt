package ch.bildspur.seqosc

import ch.bildspur.seqosc.net.OSCPacket
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.zip.Deflater
import java.util.zip.Inflater


class OSCBuffer(var comment: String = "") {
    // meta
    var speed = 1.0f

    // data
    val samples = mutableListOf<OSCSample>()

    fun asByteBuffer(compressed: Boolean = false): ByteBuffer {
        // write payload
        val payloadLength = (samples.size * (Int.SIZE_BYTES + Long.SIZE_BYTES)) + samples.sumBy { it.packet.data.size }
        var payload = ByteBuffer.allocate(payloadLength)
        payload.order(ByteOrder.LITTLE_ENDIAN)

        samples.forEach {
            payload.putLong(it.timestamp)
            payload.putInt(it.packet.data.size)
            payload.put(it.packet.data)
        }

        // compression
        if (compressed)
            payload = payload.compress()

        // reset position
        payload.position(0)

        // write header
        val rawComment = comment.toByteArray(Charsets.UTF_8)
        val headerLength = Int.SIZE_BYTES + Int.SIZE_BYTES + 4 + Int.SIZE_BYTES + rawComment.size

        val data = ByteBuffer.allocate(headerLength + payload.limit())
        val flags = compressed.toFlag(0)

        data.putInt(flags)
        data.putInt(samples.size)
        data.putFloat(speed)
        data.putInt(rawComment.size)
        data.put(rawComment)
        data.order(ByteOrder.LITTLE_ENDIAN)

        data.put(payload)

        return data
    }

    fun fromByteBuffer(data: ByteBuffer) {
        data.order(ByteOrder.LITTLE_ENDIAN)

        while (data.hasRemaining()) {
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