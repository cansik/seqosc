package ch.bildspur.seqosc

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.zip.Deflater
import java.util.zip.Inflater

fun ByteBuffer.compress() : ByteBuffer {
    this.position(0)
    val output = ByteBuffer.allocate(this.limit())
    output.order(ByteOrder.LITTLE_ENDIAN)
    val compressor = Deflater(Deflater.BEST_COMPRESSION)
    compressor.setInput(this)
    compressor.finish()
    val compressedDataLength = compressor.deflate(output)
    compressor.end()
    output.position(0)
    output.limit(compressedDataLength)
    return output
}

fun ByteBuffer.decompress(bufferSize : Int) : ByteBuffer {
    this.position(0)
    val decompresser = Inflater()
    decompresser.setInput(this)
    val result = ByteBuffer.allocate(bufferSize)
    result.order(ByteOrder.LITTLE_ENDIAN)
    val resultLength = decompresser.inflate(result)
    decompresser.end()
    result.position(0)
    result.limit(resultLength)
    return result
}

/**
 * Reads byte array out of byte buffer
 */
fun ByteBuffer.getBytes(length : Int) : ByteArray {
    val subpart = this.slice().limit(length)
    subpart.order(ByteOrder.LITTLE_ENDIAN)
    val data = ByteArray(subpart.remaining())
    subpart.get(data)
    this.position(this.position() + length);
    return data
}

fun Boolean.toFlag(position : Int) : Int  {
    return (if(this) 1 else 0) shl position
}

fun Int.getFlag(position: Int) : Boolean {
    return (this and (1 shl position)) != 0;
}