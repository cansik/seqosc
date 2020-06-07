package ch.bildspur.seqosc

import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val data = Files.readAllBytes(Paths.get("recordings", "test-compressed.osc"))

    val buffer = OSCBuffer()
    buffer.read(ByteBuffer.wrap(data))

    println("First:")
    println(buffer.samples.first().packet)

    val player = OSCPlayer("localhost", 8000, buffer.speed, buffer)

    println("playing ${buffer.samples.size} samples...")
    player.play()

    println("done!")
}