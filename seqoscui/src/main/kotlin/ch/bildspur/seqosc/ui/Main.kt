package ch.bildspur.seqosc.ui

import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

fun main() {
    SeqOscUIApp().run()
}

class SeqOscUIApp {
    fun run() {
        Platform.startup {
            val stage = Stage()
            val resource = PlayerWindow::class.java.getResource("/ch/bildspur/seqosc/ui/player.fxml")
            val loader = FXMLLoader(resource)
            val root = loader.load<Any>() as Parent
            val controller = loader.getController<Any>() as PlayerWindow

            controller.mainStage = stage

            stage.title = "SeqOSC"
            stage.scene = Scene(root)

            // setup on shown event
            stage.setOnShown { controller.onShown() }
            stage.isResizable = false
            stage.show()
        }
    }
}