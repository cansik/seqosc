package ch.bildspur.seqosc

import ch.bildspur.seqosc.net.OSCPacket

data class OSCSample(var timestamp : Long, var packet : OSCPacket)