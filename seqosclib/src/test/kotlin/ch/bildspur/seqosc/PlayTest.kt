package ch.bildspur.seqosc

import ch.bildspur.seqosc.net.OSCMessage
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val data = Files.readAllBytes(Paths.get("recordings", "test.osc"))

    val buffer = OSCBuffer()
    buffer.fromCompressedByteBuffer(ByteBuffer.wrap(data))

    println("First:")
    println(buffer.samples.first().packet)

    val msg = OSCMessage(buffer.samples.first().packet.data)
    val isBundle = msg.isBundle

    val player = OSCPlayer("localhost", 7800, buffer, 1f)

    println("playing ${buffer.samples.size} samples...")
    player.play()

    println("done!")
}