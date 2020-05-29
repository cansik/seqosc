package ch.bildspur.seqosc

import org.junit.Test

fun main() {
    val recorder = OSCRecorder(12000)
    val time = 5000L

    println("recording for ${time / 1000} seconds...")
    recorder.record()
    Thread.sleep(time)
    recorder.stop()

    println("recorded ${recorder.buffer.samples.size} samples!")
}