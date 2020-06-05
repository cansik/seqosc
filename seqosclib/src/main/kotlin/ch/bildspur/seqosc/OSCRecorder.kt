package ch.bildspur.seqosc

import ch.bildspur.seqosc.net.OSCServer

class OSCRecorder(val port: Int, val buffer: OSCBuffer = OSCBuffer()) {

    val server = OSCServer(port)

    @Volatile
    var recording = false
        private set

    init {
        server.onPacketReceived += {
            if (recording) {
                buffer.samples.add(OSCSample(System.currentTimeMillis(), it))
            }
        }
    }

    fun record() {
        if (recording)
            return

        server.open()

        recording = true
    }

    fun stop() {
        if (!recording)
            return

        server.close()
        recording = false
    }
}