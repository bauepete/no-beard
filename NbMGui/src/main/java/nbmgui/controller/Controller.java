package nbmgui.controller;

import io.BinaryFile;
import io.BinaryFileHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import machine.NoBeardMachine;
import nbmgui.InputReader;
import nbmgui.OutputPrinter;

import java.io.*;

public class Controller {
    private String path;
    private NoBeardMachine machine;
    private BinaryFile objectFile;

    @FXML
    private AnchorPane outputPane;

    private TextArea outputView;

    public void initialize() {
        this.outputView = new OutputArea();
        AnchorPane.setTopAnchor(outputView, 0.0);
        AnchorPane.setLeftAnchor(outputView, 0.0);
        AnchorPane.setRightAnchor(outputView, 0.0);
        AnchorPane.setBottomAnchor(outputView, 0.0);
        outputPane.getChildren().add(outputView);
        machine = new NoBeardMachine(new InputReader(this), new OutputPrinter(this));
    }

    public TextArea getOutputView() {
        return outputView;
    }

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
            outputView.appendText("Select a NoBeard object file\n");
            return;
        }
        try {
            objectFile = BinaryFileHandler.open(path);
        } catch (IOException ex) {
            outputView.appendText("Unable to open " + path + "\n");
            return;
        }
        machine.loadStringConstants(objectFile.getStringStorage());
        machine.loadProgram(0, objectFile.getProgram());
        machine.runProgram(0);
    }

    public static class OutputArea extends TextArea {
        @Override
        public void replaceText(int start, int end, String text) {
            String current = getText();
            // only insert if no new lines after insert position:
            if (! current.substring(start).contains("\n")) {
                super.replaceText(start, end, text);
            }
        }
        @Override
        public void replaceSelection(String text) {
            String current = getText();
            int selectionStart = getSelection().getStart();
            if (! current.substring(selectionStart).contains("\n")) {
                super.replaceSelection(text);
            }
        }
    }
}