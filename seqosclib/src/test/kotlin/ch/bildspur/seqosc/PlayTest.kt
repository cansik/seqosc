package ch.bildspur.seqosc

import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val data = Files.readAllBytes(Paths.get("recordings", "florian.osc"))

    val buffer = OSCBuffer()
    buffer.read(ByteBuffer.wrap(data))

    println("First:")
    println(buffer.samples.first().packet)

    val player = OSCPlayer("localhost", 7800, buffer.speed, buffer)

    println("playing ${buffer.samples.size} samples...")
    player.play()
    player.loop = true

    while(player.playing) {
        println("Sample: ${player.currentIndex} of ${player.buffer.samples.size}")
        Thread.sleep(1000 / 1)
    }
    println("done!")
}