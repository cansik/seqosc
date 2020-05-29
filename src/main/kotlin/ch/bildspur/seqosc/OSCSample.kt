package ch.bildspur.seqosc

import com.illposed.osc.OSCMessage
import com.illposed.osc.OSCPacket

data class OSCSample(var delta : Long, var packet : OSCPacket)