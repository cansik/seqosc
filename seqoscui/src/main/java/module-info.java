module seqoscui {
    requires kotlin.stdlib;

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;

    requires seqosclib;

    opens ch.bildspur.seqosc.ui;
}