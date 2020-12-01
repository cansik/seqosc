package ch.bildspur.seqosc

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val recorder = OSCRecorder(7800, OSCBuffer("florian first recording"))
    val time = 1000L * 20

    println("recording for ${time / 1000} seconds...")
    recorder.record()
    Thread.sleep(time)
    recorder.stop()

    println("recorded ${recorder.buffer.samples.size} samples!")

    println("First Sample:")
    println(recorder.buffer.samples.first().packet.data.contentToString())

    Files.write(Paths.get("recordings", "test.osc"), recorder.buffer.write(compressed = false).array())
    Files.write(Paths.get("recordings", "test-compressed.osc"), recorder.buffer.write(compressed = true).array())
}