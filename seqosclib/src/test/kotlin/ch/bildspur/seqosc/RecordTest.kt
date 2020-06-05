package ch.bildspur.seqosc

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val recorder = OSCRecorder(12000, OSCBuffer("test recording"))
    val time = 1000L * 3

    println("recording for ${time / 1000} seconds...")
    recorder.record()
    Thread.sleep(time)
    recorder.stop()

    println("recorded ${recorder.buffer.samples.size} samples!")

    println("First Sample:")
    println(recorder.buffer.samples.first().packet.data.contentToString())

    Files.write(Paths.get("recordings", "test.osc"), recorder.buffer.asByteBuffer(compressed = false).array())
}