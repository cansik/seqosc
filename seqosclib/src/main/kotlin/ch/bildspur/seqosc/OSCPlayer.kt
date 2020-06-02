package ch.bildspur.seqosc

import ch.bildspur.seqosc.net.OSCClient
import java.net.Inet4Address
import kotlin.math.roundToLong


class OSCPlayer(val host : String, val port : Int, val buffer : OSCBuffer, var speed : Float = 1.0f) {

    val client = OSCClient()
    val address = Inet4Address.getByName(host)

    fun play() {
        buffer.samples.forEach {
            Thread.sleep((it.delta / speed).roundToLong())
            client.send(address, port, it.packet)
        }
    }

    fun stop() {

    }

    fun reset() {

    }
}