package ch.bildspur.seqosc.ui

import javafx.application.Platform
import javafx.stage.Stage

fun main() {
    Platform.startup {
        val window = PlayerWindow()
        val stage = Stage()
        window.start(stage)
    }
}