package nbmgui;

import io.BinaryFile;
import io.BinaryFileHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import machine.NoBeardMachine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Controller {
    private NoBeardMachine machine;
    private Semaphore semaphore;
    private BinaryFile objectFile;
    private Path path;
    private String input;
    private HashMap<Integer, CheckBox> programDataMap;
    private int lastProgramLine;

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
        programDataMap = new HashMap<>();
        inputView.setDisable(true);
        startButton.setDisable(true);
        stepButton.setDisable(true);
        continueButton.setDisable(true);
        stopButton.setDisable(true);
        inputView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && inputView != null && !inputView.getText().isEmpty()) {
                inputIsAvailable(inputView.getText());
            }
        });
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public String getInput() {
        return input;
    }

    public TextArea getOutputView() {
        return outputView;
    }

    public TextField getInputView() {
        return inputView;
    }

    @FXML
    void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("NoBeard-object Files", "*.no"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            this.path = Paths.get(selectedFile.getAbsolutePath());
            prepareObjectFile();
        } else
            outputView.appendText("Select a NoBeard object file\n");
    }

    @FXML
    void startProgram(ActionEvent event) {
        stepButton.setDisable(false);
        continueButton.setDisable(false);
        stopButton.setDisable(false);
        lastProgramLine = -1;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                machine.loadStringConstants(objectFile.getStringStorage());
                machine.loadProgram(0, objectFile.getProgram());
                machine.runProgram(0);
                highlightNextInstructionToBeExecuted();
            }
        });
        thread.start();
    }

    @FXML
    void step(ActionEvent event) {
        machine.step();
        highlightNextInstructionToBeExecuted();
    }

    @FXML
    void continueToBreakpoint(ActionEvent event) {
        machine.runUntilNextBreakpoint();
        highlightNextInstructionToBeExecuted();
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
        VBox programData = new VBox();
        for (String lineStr : programDataList) {
            CheckBox line = new CheckBox(lineStr);
            line.setPadding(new Insets(1));
            line.setOnAction((event) -> {
                if (event.getSource() instanceof CheckBox) {
                    CheckBox breakpoint = (CheckBox) event.getSource();
                    if (breakpoint.isSelected())
                        machine.addBreakpoint(getAddressOfProgramLine(breakpoint.getText()));
                    else
                        machine.removeBreakpoint(getAddressOfProgramLine(breakpoint.getText()));
                }
            });
            programData.getChildren().add(line);
            programDataMap.put(getAddressOfProgramLine(line.getText()), line);
        }
        programDataView.setContent(programData);
    }

    private int getAddressOfProgramLine(String line) {
        return Integer.valueOf(line.substring(2, 5));
    }

    private void highlightNextInstructionToBeExecuted() {
        if (programDataMap.containsKey(machine.getPc()))
            programDataMap.get(machine.getPc()).setStyle("-fx-background-color: #999999");
        if (lastProgramLine > -1)
            programDataMap.get(lastProgramLine).setStyle("-fx-background-color: transparent");
        lastProgramLine = machine.getPc();
    }

    private void inputIsAvailable(String providedInput) {
        this.getOutputView().appendText(providedInput + "\n");
        this.input = providedInput;
        this.inputView.clear();
        this.inputView.setDisable(true);
        this.getSemaphore().release();
    }
}