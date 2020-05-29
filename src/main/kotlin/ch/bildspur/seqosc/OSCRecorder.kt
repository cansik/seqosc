package ch.bildspur.seqosc

import com.illposed.osc.OSCBadDataEvent
import com.illposed.osc.OSCPacketEvent
import com.illposed.osc.OSCPacketListener
import com.illposed.osc.transport.udp.OSCPortIn
import com.illposed.osc.transport.udp.OSCPortInBuilder

class OSCRecorder (val port : Int, val buffer : OSCBuffer = OSCBuffer()) : OSCPacketListener {

    val receiver : OSCPortIn = OSCPortInBuilder()
            .setPort(port)
            .addPacketListener(this)
            .build()

    @Volatile var recording = false
        private set

    @Volatile private var timeStamp = 0L

    fun record() {
        if(recording)
            return

        receiver.startListening()

        timeStamp = System.currentTimeMillis()
        recording = true
    }

    fun stop() {
        if(!recording)
            return

        receiver.stopListening()

        recording = false
    }

    override fun handlePacket(event: OSCPacketEvent?) {
        if(!recording)
            return

        event?.packet?.let {
            val ts = System.currentTimeMillis()
            buffer.samples.add(OSCSample(ts - timeStamp, it))
            timeStamp = ts
        }
    }

    override fun handleBadData(event: OSCBadDataEvent?) {

    }
}