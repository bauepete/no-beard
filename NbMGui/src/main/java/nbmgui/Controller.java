package nbmgui;

import io.BinaryFile;
import io.BinaryFileHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import machine.NoBeardMachine;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Controller {
    private NoBeardMachine machine;
    private Semaphore semaphore;
    private BinaryFile objectFile;
    private Path path;
    private String input;
    private VBox programData;

    @FXML
    private TextField inputView;
    @FXML
    private TextArea outputView;
    @FXML
    private ScrollPane programDataView;
    @FXML
    private Label fileTitle;
    @FXML
    private Button startButton;
    @FXML
    private Button stepButton;
    @FXML
    private Button continueButton;
    @FXML
    private Button stopButton;

    public void initialize() {
        machine = new NoBeardMachine(new FxInputDevice(this), new FxOutputDevice(this));
        semaphore = new Semaphore(0);
        inputView.setDisable(true);
        startButton.setDisable(true);
        stepButton.setDisable(true);
        continueButton.setDisable(true);
        stopButton.setDisable(true);
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

    @FXML
    void startProgram(ActionEvent event) {
        setBreakpoints();
        stepButton.setDisable(false);
        continueButton.setDisable(false);
        stopButton.setDisable(false);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                machine.loadStringConstants(objectFile.getStringStorage());
                machine.loadProgram(0, objectFile.getProgram());
                machine.runProgram(0);
                setCurrentPcOfUI();
            }
        });
        thread.start();
    }

    @FXML
    void step(ActionEvent event) {
        machine.step();
        setCurrentPcOfUI();
    }

    @FXML
    void continueToBreakpoint(ActionEvent event) {
        setBreakpoints();
        machine.jumpToNextBreakpoint();
        setCurrentPcOfUI();
    }
    @FXML
    void stopProgram(ActionEvent event) {
        stepButton.setDisable(true);
        continueButton.setDisable(true);
        stopButton.setDisable(true);
        machine.stopProgram();
    }

    private void prepareObjectFile() {
        try {
            objectFile = BinaryFileHandler.open(path.toString());
        } catch (IOException ex) {
            outputView.appendText("Unable to open " + path.getFileName().toString() + "\n");
            return;
        }
        Disassembler disassembler = new Disassembler(objectFile);
        fillProgramDataView(disassembler.getProgramData());
        fileTitle.setText(path.getFileName().toString());
        startButton.setDisable(false);
    }

    private void fillProgramDataView(List<String> programDataList) {
        programData = new VBox();
        for (String lineStr : programDataList) {
            CheckBox line = new CheckBox(lineStr);
            line.setPadding(new Insets(1));
            programData.getChildren().add(line);
        }
        programDataView.setContent(programData);
    }

    private void setCurrentPcOfUI() {
        for (int i = 0; i < programData.getChildren().size(); i++) {
            CheckBox checkBox = (CheckBox) programData.getChildren().get(i);
            if (Integer.valueOf(checkBox.getText().substring(2, 5)) == machine.getPc()) {
                ((CheckBox) programData.getChildren().get(i)).setStyle("-fx-background-color: #999999");
            }
            else
                ((CheckBox) programData.getChildren().get(i)).setStyle("-fx-background-color: transparent");
        }
    }

    private void setBreakpoints() {
        machine.clearBreakpoints();
        for (int i = 0; i < programData.getChildren().size(); i++) {
            CheckBox checkBox = (CheckBox) programData.getChildren().get(i);
            if (checkBox.isSelected()) {
                machine.addBreakpoint(Integer.valueOf(checkBox.getText().substring(2, 5)));
            }
        }
    }

    private void inputIsAvailable(String providedInput) {
        this.getOutputView().appendText(providedInput + "\n");
        this.input = providedInput;
        this.inputView.clear();
        this.inputView.setDisable(true);
        this.getSemaphore().release();
    }
}