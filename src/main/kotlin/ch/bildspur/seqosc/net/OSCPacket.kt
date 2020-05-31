package ch.bildspur.seqosc.net

data class OSCPacket(val data: ByteArray) {

    val x: Int
        get() = data[0].toInt()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OSCPacket

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }
}