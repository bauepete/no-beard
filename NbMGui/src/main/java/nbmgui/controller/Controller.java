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

import java.io.*;

public class Controller {
    private String path;
    private NoBeardMachine machine;
    private BinaryFile objectFile;

    @FXML
    private AnchorPane outputPane;

    private PrintStream ps ;

    private TextArea outputView;

    public void initialize() {
        this.outputView = new OutputArea();
        AnchorPane.setTopAnchor(outputView, 0.0);
        AnchorPane.setLeftAnchor(outputView, 0.0);
        AnchorPane.setRightAnchor(outputView, 0.0);
        AnchorPane.setBottomAnchor(outputView, 0.0);
        outputPane.getChildren().add(outputView);
        ps = new PrintStream(new Console(outputView));
        System.setOut(ps);
        System.setErr(ps);
        machine = new NoBeardMachine();
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
            System.out.println("Select a NoBeard object file");
            return;
        }
        try {
            objectFile = BinaryFileHandler.open(path);
        } catch (IOException ex) {
            System.out.println("Unable to open " + path);
            return;
        }
        machine.loadStringConstants(objectFile.getStringStorage());
        machine.loadProgram(0, objectFile.getProgram());
        machine.runProgram(0);
    }

    public class Console extends OutputStream {
        private TextArea console;

        public Console(TextArea console) {
            this.console = console;
        }

        public void appendText(String valueOf) {
            Platform.runLater(() -> console.appendText(valueOf));
        }

        public void write(int b) throws IOException {
            appendText(String.valueOf((char)b));
        }
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