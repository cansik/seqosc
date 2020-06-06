package ch.bildspur.seqosc

import ch.bildspur.seqosc.net.OSCPacket
import java.nio.ByteBuffer
import java.nio.ByteOrder


class OSCBuffer(var comment: String = "") {
    // meta
    var speed = 1.0f

    // data
    val samples = mutableListOf<OSCSample>()

    fun write(compressed: Boolean = false): ByteBuffer {
        // write payload
        val payloadLength = (samples.size * (Int.SIZE_BYTES + Long.SIZE_BYTES)) + samples.sumBy { it.packet.data.size }
        var payload = ByteBuffer.allocate(payloadLength)
        payload.order(ByteOrder.LITTLE_ENDIAN)

        samples.forEach {
            payload.putLong(it.timestamp)
            payload.putInt(it.packet.data.size)
            payload.put(it.packet.data)
        }

        payload.position(0)
        var payloadRaw = payload.getBytes(payload.limit())

        // compression
        if (compressed) {
            payloadRaw = payloadRaw.gzipCompress()
        }

        // write header
        val rawComment = comment.toByteArray(Charsets.UTF_8)
        val headerLength = Int.SIZE_BYTES + Int.SIZE_BYTES + Int.SIZE_BYTES + 4 + Int.SIZE_BYTES + rawComment.size

        val data = ByteBuffer.allocate(headerLength + payloadRaw.size)
        data.order(ByteOrder.LITTLE_ENDIAN)

        val flags = compressed.toFlag(0)

        data.putInt(flags)
        data.putInt(samples.size)
        data.putInt(payloadLength)
        data.putFloat(speed)
        data.putInt(rawComment.size)
        data.put(rawComment)

        data.put(payloadRaw)

        return data
    }

    fun read(data: ByteBuffer) {
        data.order(ByteOrder.LITTLE_ENDIAN)
        data.position(0)

        // read header
        val flags = data.int
        val compressed = flags.getFlag(0)

        var sampleCount = data.int
        sampleCount = if(sampleCount < 0) Int.MAX_VALUE else sampleCount

        val payloadLength  = data.int

        this.speed = data.float
        this.comment = data.getBytes(data.int).toString(Charsets.UTF_8)

        // extract payload
        var payloadRaw = data.getBytes(data.limit() - data.position())

        if(compressed)
            payloadRaw = payloadRaw.gzipUncompress()

        val payload = ByteBuffer.wrap(payloadRaw)
        payload.order(ByteOrder.LITTLE_ENDIAN)

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