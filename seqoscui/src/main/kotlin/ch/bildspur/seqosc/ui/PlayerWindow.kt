package ch.bildspur.seqosc.ui

import ch.bildspur.seqosc.OSCBuffer
import ch.bildspur.seqosc.OSCPlayer
import ch.bildspur.seqosc.OSCRecorder
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import java.nio.ByteBuffer
import java.nio.file.Files
import kotlin.concurrent.thread
import kotlin.math.max


class PlayerWindow() {
    lateinit var mainStage: Stage

    // osc
    val oscBuffer = OSCBuffer()
    var recorder = OSCRecorder(7400, oscBuffer)
    var player = OSCPlayer("localhost", 7400, 1.0f, oscBuffer)

    // ui
    @FXML
    lateinit var playPauseButton: Button
    @FXML
    lateinit var recordButton: Button
    @FXML
    lateinit var progressBar: ProgressBar
    @FXML
    lateinit var progressLabel: Label
    @FXML
    lateinit var hostField: TextField
    @FXML
    lateinit var portField: TextField
    @FXML
    lateinit var sampleCountField: Label
    @FXML
    lateinit var currentIndexField: Label
    @FXML
    lateinit var loopCheckBox: CheckBox

    enum class UIStates {
        Idle,
        Playing,
        Paused,
        Recording
    }

    var uiState = UIStates.Idle
    var bufferSize = 0

    fun onShown() {
        mainStage.title = "SeqOSC"

        thread(isDaemon = true, start = true) {
            while(true) {
                updateUIState()

                if(uiState == UIStates.Recording) {
                    recalculateBufferSize()
                    Thread.sleep(1000 / 1)
                } else {
                    Thread.sleep(1000 / 3)
                }
            }
        }

        loopCheckBox.selectedProperty().addListener { observable, oldValue, newValue ->
            player.loop = newValue
        }

        // setup binding
        /*
        // todo: fix this...
        hostField.textProperty().bindBidirectional(host)
        portField.textProperty().bindBidirectional(port, NumberStringConverter())
        */
    }

    private fun recalculateBufferSize() {
        this.bufferSize = oscBuffer.samples.sumBy { it.packet.data.size }
    }

    fun onLoad(actionEvent: ActionEvent) {
        val chooser = FileChooser()
        chooser.extensionFilters.add(FileChooser.ExtensionFilter("OSC File", "*.osc"))
        val file: File? = chooser.showOpenDialog(mainStage)

        if (file != null) {
            val data = Files.readAllBytes(file.toPath())
            oscBuffer.clear()
            oscBuffer.read(ByteBuffer.wrap(data))
            recalculateBufferSize()
        }

        updateUIState()
    }

    fun onSave(actionEvent: ActionEvent) {
        val chooser = FileChooser()
        chooser.extensionFilters.add(FileChooser.ExtensionFilter("OSC File", "*.osc"))
        val file: File? = chooser.showSaveDialog(mainStage)

        if (file != null)
            Files.write(file.toPath(), oscBuffer.write(compressed = true).array())

        updateUIState()
    }

    fun onSkipBack(actionEvent: ActionEvent) {

    }

    fun onReset(actionEvent: ActionEvent) {
        player.stop()
        player.reset()
        updateUIState()
    }

    fun onPlayPause(actionEvent: ActionEvent) {
        if (uiState == UIStates.Playing) {
            player.stop()
            uiState = UIStates.Paused
        } else {
            if (player.address.toString() != hostField.text || player.port.toString() != portField.text) {
                println("re init player...")
                player = OSCPlayer(hostField.text, portField.text.toInt(), 1.0f, oscBuffer)
            }
            player.loop = loopCheckBox.isSelected
            player.play()
            uiState = UIStates.Playing
        }

        updateUIState()
    }

    fun onSkipForward(actionEvent: ActionEvent) {

    }

    fun onRecord(actionEvent: ActionEvent) {
        if (uiState == UIStates.Recording) {
            recorder.stop()
            uiState = UIStates.Idle
        } else {
            oscBuffer.clear()

            if (player.port.toString() != portField.text) {
                println("re init recorder...")
                recorder = OSCRecorder(portField.text.toInt(), oscBuffer)
            }

            recorder.record()
            uiState = UIStates.Recording
        }

        updateUIState()
    }

    private fun updateUIState() {
        Platform.runLater {
            when (uiState) {
                UIStates.Idle -> {
                    mainStage.title = "SeqOSC"
                    playPauseButton.isDisable = false
                    recordButton.isDisable = false
                }
                UIStates.Playing -> {
                    mainStage.title = "SeqOSC - Playing"
                    playPauseButton.isDisable = false
                    recordButton.isDisable = true
                }
                UIStates.Paused -> {
                    mainStage.title = "SeqOSC - Paused"
                    playPauseButton.isDisable = false
                    recordButton.isDisable = true
                }
                UIStates.Recording -> {
                    mainStage.title = "SeqOSC - Recording"
                    playPauseButton.isDisable = true
                    recordButton.isDisable = false
                }
            }

            if(uiState == UIStates.Playing) {
                progressBar.progress = player.currentIndex / 1.coerceAtLeast(oscBuffer.samples.size).toDouble()
            } else {
                progressBar.progress = 0.0
            }

            sampleCountField.text = "Samples: ${oscBuffer.samples.size} (${(bufferSize / 1024.0 / 1024.0).format(2)} MBytes)"
            currentIndexField.text = "Index: ${player.currentIndex}"
        }
    }

    fun Number.format(digits: Int) = java.lang.String.format("%.${digits}f", this)
}