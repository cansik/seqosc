package ch.bildspur.seqosc

import ch.bildspur.seqosc.net.OSCClient
import jdk.jshell.spi.ExecutionControl
import java.net.Inet4Address
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.math.roundToLong


class OSCPlayer(val host : String, val port : Int, var speed : Float = 1.0f, val buffer : OSCBuffer) {

    val client = OSCClient()
    val address = Inet4Address.getByName(host)

    private val playState = AtomicBoolean(false)
    private val index = AtomicInteger(0)

    var lastTimeStamp = 0L
    var loop = false

    lateinit var playThread : Thread

    val currentIndex : Int
        get() = index.get()

    val playing : Boolean
        get() = playState.get()

    fun play() {
        if(playing) return

        playState.set(true)
        playThread = thread(start = true, isDaemon = true) {

            lastTimeStamp = buffer.samples.first().timestamp

            while(playing && index.get() < buffer.samples.size) {
                val sample = buffer.samples[index.getAndIncrement()]
                val delta = sample.timestamp - lastTimeStamp
                Thread.sleep((delta / speed).roundToLong())
                client.send(address, port, sample.packet)

                lastTimeStamp = sample.timestamp

                if(loop && index.get() >= buffer.samples.size) {
                    lastTimeStamp = buffer.samples.first().timestamp
                    index.set(0)
                }
            }

            playState.set(false)
        }
    }

    fun stop() {
        if(!playing) return
        playState.set(false)
        playThread.join()
    }

    fun reset() {
        index.set(0)
    }
}