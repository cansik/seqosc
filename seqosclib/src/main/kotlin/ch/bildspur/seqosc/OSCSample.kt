package ch.bildspur.seqosc

import ch.bildspur.seqosc.net.OSCPacket

data class OSCSample(var delta : Long, var packet : OSCPacket)