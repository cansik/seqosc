package ch.bildspur.seqosc.net

open class OSCPacket(val data: ByteArray) {
    companion object {
        @JvmStatic
        val bundleIdentifier = "#bundle".toByteArray(Charsets.UTF_8)
    }

    val isBundle: Boolean by lazy {
        data.take(bundleIdentifier.size).toByteArray().contentEquals(bundleIdentifier)
    }

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

    override fun toString(): String {
        return data.take(30).toByteArray().toString(Charsets.UTF_8)
    }
}