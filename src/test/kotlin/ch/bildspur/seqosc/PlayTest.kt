package ch.bildspur.seqosc

import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val data = Files.readAllBytes(Paths.get("recordings", "test.osc"))

    val buffer = OSCBuffer()
    buffer.fromByteBuffer(ByteBuffer.wrap(data))

    val player = OSCPlayer("localhost", 8000, buffer)

    println("playing ${buffer.samples.size} samples...")
    player.play()

    println("done!")
}