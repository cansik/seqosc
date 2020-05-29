package ch.bildspur.seqosc

import com.illposed.osc.OSCSerializerAndParserBuilder
import com.illposed.osc.transport.udp.OSCPortIn
import java.nio.ByteBuffer
import java.nio.ByteOrder

class OSCBuffer {
    val samples = mutableListOf<OSCSample>()

    private data class RawSample(val delta : Long, val data : ByteArray)

    fun asByteBuffer() : ByteBuffer {
        val buffer = ByteBuffer.allocate(OSCPortIn.BUFFER_SIZE)
        val serializer = OSCSerializerAndParserBuilder().buildSerializer(buffer)

        val raw = samples.map {
            serializer.write(it.packet)
            RawSample(it.delta, buffer.array())
        }

        // raw size: delta(64bit int) + length(32bit int) + data(?)
        val data = ByteBuffer.allocate((samples.size * (4 + 8)) + raw.sumBy { it.data.size })
        data.order(ByteOrder.LITTLE_ENDIAN)

        raw.forEach {
            data.putLong(it.delta)
            data.putInt(it.data.size)
            data.put(it.data)
        }

        return data
    }

    fun fromByteBuffer(data : ByteBuffer) {
        data.order(ByteOrder.LITTLE_ENDIAN)
        val parser = OSCSerializerAndParserBuilder().buildParser()

        while(data.hasRemaining()) {
            val delta = data.long
            val length = data.int
            val subpart = data.slice().limit(length)
            val packet = parser.convert(subpart)
            data.position(data.position() + length)

            samples.add(OSCSample(delta, packet))
        }
    }
}