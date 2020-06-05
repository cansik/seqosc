package ch.bildspur.seqosc

import ch.bildspur.seqosc.net.OSCPacket
import java.nio.ByteBuffer
import java.nio.ByteOrder


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

        // reset payload position
        payload.position(0)

        // write header
        val rawComment = comment.toByteArray(Charsets.UTF_8)
        val headerLength = Int.SIZE_BYTES + Int.SIZE_BYTES + 4 + Int.SIZE_BYTES + rawComment.size

        val data = ByteBuffer.allocate(headerLength + payload.limit())
        data.order(ByteOrder.LITTLE_ENDIAN)

        val flags = compressed.toFlag(0)

        data.putInt(flags)
        data.putInt(samples.size)
        data.putFloat(speed)
        data.putInt(rawComment.size)
        data.put(rawComment)

        data.put(payload)

        return data
    }

    fun fromByteBuffer(data: ByteBuffer) {
        data.order(ByteOrder.LITTLE_ENDIAN)
        data.position(0)

        // read header
        val flags = data.int
        val compressed = flags.getFlag(0)

        var sampleCount = data.int
        sampleCount = if(sampleCount < 0) Int.MAX_VALUE else sampleCount

        this.speed = data.float
        this.comment = data.getBytes(data.int).toString(Charsets.UTF_8)

        var payload = data.slice()
        payload.order(ByteOrder.LITTLE_ENDIAN)

        if(compressed)
            payload = payload.decompress()

        payload.position(0)

        var count = 0
        while (count < sampleCount && payload.hasRemaining()) {
            val timestamp = payload.long
            val length = payload.int
            val packetData = payload.getBytes(length)
            val packet = OSCPacket(packetData)

            samples.add(OSCSample(timestamp, packet))
            count++
        }
    }
}