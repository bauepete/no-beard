package nbmgui.controller;

import io.BinaryFile;
import io.BinaryFileHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import machine.ControlUnit;
import machine.NoBeardMachine;

import java.io.File;
import java.io.IOException;

public class Controller {
    private String path;
    private NoBeardMachine machine;
    private BinaryFile objectFile;

    @FXML
    private TextArea outputView;

    @FXML
    void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("NoBeard-object Files", "*.no"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null)
            this.path = selectedFile.getAbsolutePath();
    }

    @FXML
    void startProgram(ActionEvent event) {
        if (path == null) {
            outputView.setText(outputView.getText() + "\nSelect a NoBeard object file");
            return;
        }
        try {
            objectFile = BinaryFileHandler.open(path);
        } catch (IOException ex) {
            outputView.setText(outputView.getText() + "\nUnable to open " + path);
            return;
        }
        machine = new NoBeardMachine();
        machine.loadStringConstants(objectFile.getStringStorage());
        machine.loadProgram(0, objectFile.getProgram());
        machine.getControlUnit().startMachine();

        while(machine.getState() == ControlUnit.MachineState.RUNNING) {
            machine.step();
        }
    }
}
