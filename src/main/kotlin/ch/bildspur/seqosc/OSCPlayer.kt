package ch.bildspur.seqosc

import com.illposed.osc.OSCSerializerAndParserBuilder
import com.illposed.osc.transport.udp.OSCPortOut
import java.net.Inet4Address


class OSCPlayer(val host : String, val port : Int, val buffer : OSCBuffer) {

    val client = OSCPortOut(Inet4Address.getByName(host), port)

    fun play() {
        buffer.samples.forEach {
            Thread.sleep(it.delta)
            client.send(it.packet)
        }
    }

    fun stop() {

    }

    fun reset() {

    }
}