package ch.bildspur.seqosc

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val recorder = OSCRecorder(7800)
    val time = 5000L

    println("recording for ${time / 1000} seconds...")
    recorder.record()
    Thread.sleep(time)
    recorder.stop()

    println("recorded ${recorder.buffer.samples.size} samples!")

    Files.write(Paths.get("recordings", "test.osc"), recorder.buffer.asByteBuffer().array())
}