package ch.bildspur.seqosc

import ch.bildspur.seqosc.net.OSCServer

class OSCRecorder(val port: Int, val buffer: OSCBuffer = OSCBuffer()) {

    val server = OSCServer(port)

    @Volatile
    var recording = false
        private set

    @Volatile
    private var timeStamp = 0L

    init {
        server.onPacketReceived += {
            if (recording) {
                val ts = System.currentTimeMillis()
                buffer.samples.add(OSCSample(ts - timeStamp, it))
                timeStamp = ts
            }
        }
    }

    fun record() {
        if (recording)
            return

        server.open()

        timeStamp = System.currentTimeMillis()
        recording = true
    }

    fun stop() {
        if (!recording)
            return

        server.close()
        recording = false
    }
}