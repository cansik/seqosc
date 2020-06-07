package ch.bildspur.seqosc.ui

import ch.bildspur.seqosc.OSCBuffer
import ch.bildspur.seqosc.OSCPlayer
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.property.*
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.stage.Stage


class PlayerWindow : Application() {

    // osc
    val oscBuffer = OSCBuffer()

    // model
    val host = SimpleStringProperty("localhost")
    val port = SimpleIntegerProperty(8000)

    val compressed = SimpleBooleanProperty(true)
    val comment = SimpleStringProperty("")
    val speed = SimpleFloatProperty(1.0f)

    // ui
    @FXML lateinit var playPauseButton : Button
    @FXML lateinit var recordButton : Button
    @FXML lateinit var progressBar : ProgressBar
    @FXML lateinit var progressLabel : Label

    override fun start(primaryStage: Stage) {
        primaryStage.scene = Scene(FXMLLoader.load(this.javaClass.classLoader.getResource("player.fxml")))
        primaryStage.isResizable = false
        primaryStage.title = "SeqOSC"

        // setup binding

        primaryStage.show()
    }

    fun onLoad(actionEvent: ActionEvent) {

    }

    fun onSave(actionEvent: ActionEvent) {

    }

    fun onSkipBack(actionEvent: ActionEvent) {

    }

    fun onReset(actionEvent: ActionEvent) {

    }

    fun onPlayPause(actionEvent: ActionEvent) {

    }

    fun onSkipForward(actionEvent: ActionEvent) {

    }

    fun onRecord(actionEvent: ActionEvent) {

    }

    private fun updateUIState() {
        Platform.runLater {

        }
    }
}