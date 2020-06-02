package ch.bildspur.seqosc.net

class OSCMessage(data : ByteArray) : OSCPacket(data) {

    val address : String by lazy {
        data.takeWhile { it.toChar() != ',' && it.toChar() != 0.toChar() }
            .toByteArray()
            .toString(Charsets.UTF_8)
            .trim()
    }

    override fun toString(): String {
        return "OSC Message [${address}]}"
    }
}