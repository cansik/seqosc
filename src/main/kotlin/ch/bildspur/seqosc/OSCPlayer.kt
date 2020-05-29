package ch.bildspur.seqosc

import com.illposed.osc.OSCSerializerAndParserBuilder
import com.illposed.osc.transport.udp.OSCPortOut
import java.net.Inet4Address
import kotlin.math.roundToLong


class OSCPlayer(val host : String, val port : Int, val buffer : OSCBuffer, var speed : Float = 1.0f) {

    val client = OSCPortOut(Inet4Address.getByName(host), port)

    fun play() {
        buffer.samples.forEach {
            Thread.sleep((it.delta / speed).roundToLong())
            client.send(it.packet)
        }
    }

    fun stop() {

    }

    fun reset() {

    }
}