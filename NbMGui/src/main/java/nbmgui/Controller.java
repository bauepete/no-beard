package nbmgui;

import io.BinaryFile;
import io.BinaryFileHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import machine.NoBeardMachine;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Semaphore;
import java.util.regex.Pattern;

public class Controller {
    private NoBeardMachine machine;
    private Semaphore semaphore;
    private BinaryFile objectFile;
    private Path path;
    private String input;

    @FXML
    private TextField inputView;

    @FXML
    private TextArea outputView;

    @FXML
    private TextArea programDataView;

    @FXML
    private Label fileTitle;

    @FXML
    private Button startButton;

    public void initialize() {
        machine = new NoBeardMachine(new FxInputDevice(this), new FxOutputDevice(this));
        semaphore = new Semaphore(0);
        inputView.setDisable(true);
        startButton.setDisable(true);
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
        if (selectedFile != null){
            this.path = Paths.get(selectedFile.getAbsolutePath());
            prepareObjectFile();
        }
        else
            outputView.appendText("Select a NoBeard object file\n");
    }

    private void prepareObjectFile() {
        try {
            objectFile = BinaryFileHandler.open(path.toString());
        } catch (IOException ex) {
            outputView.appendText("Unable to open " + path.getFileName().toString() + "\n");
            return;
        }
        programDataView.clear();
        Disassembler disassembler = new Disassembler(objectFile);
        for (String line : disassembler.getProgramData()) {
            programDataView.appendText(line + "\n");
        }
        fileTitle.setText(path.getFileName().toString());
        startButton.setDisable(false);
    }

    @FXML
    void startProgram(ActionEvent event) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
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