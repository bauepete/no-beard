package nbmgui;

import io.BinaryFile;
import io.BinaryFileHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import machine.NoBeardMachine;

import java.io.*;
import java.util.concurrent.Semaphore;

public class Controller {
    private String path;
    private NoBeardMachine machine;
    private BinaryFile objectFile;
    private Semaphore semaphore;
    private String input;

    @FXML
    private TextField inputView;

    @FXML
    private TextArea outputView;

    public void initialize() {
        machine = new NoBeardMachine(new FxInputDevice(this), new FxOutputDevice(this));
        semaphore = new Semaphore(0);
        inputView.setDisable(true);
        inputView.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER && inputView != null && !inputView.getText().isEmpty()){
                inputIsAvailable(inputView.getText());
            }
        });
    }

    public Semaphore getSemaphore() { return semaphore; }

    public String getInput() { return input; }

    public TextArea getOutputView() {
        return outputView;
    }

    public TextField getInputView() { return inputView; }

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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
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
        });
        thread.start();
    }

    private void inputIsAvailable(String providedInput) {
        this.getOutputView().appendText(providedInput + "\n");
        this.input = providedInput;
        this.inputView.clear();
        this.inputView.setDisable(true);
        this.getSemaphore().release();
    }
}