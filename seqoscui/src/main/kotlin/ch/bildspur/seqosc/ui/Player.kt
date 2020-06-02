package ch.bildspur.seqosc.ui

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage


class Player : Application() {

    override fun start(primaryStage: Stage) {
        primaryStage.scene = Scene(FXMLLoader.load(this.javaClass.classLoader.getResource("player.fxml")))
        primaryStage.show()
    }

}