package nbmgui;

import io.BinaryFile;
import io.BinaryFileHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML
    private ListView<String> dataMemoryListView;
    @FXML
    private Label dataMemoryHeader;
    @FXML
    private Label versionLabel;

    Semaphore getSemaphore() {
        return semaphore;
    }

    String getInput() {
        return input;
    }

    TextArea getOutputView() {
        return outputView;
    }

    TextField getInputView() {
        return inputView;
    }

    ListView<String> getDataMemoryListView() {
        return dataMemoryListView;
    }

    NoBeardMachine getMachine() {
        return machine;
    }

    double getDataMemoryHeaderHeight() {
        return dataMemoryHeader.getHeight();
    }

    public void initialize() {
        machine = new NoBeardMachine(new FxInputDevice(this), new FxOutputDevice(this));
        semaphore = new Semaphore(0);
        programDataMap = new HashMap<>();
        setDebuggerButtonsDisable(true);
        inputView.setDisable(true);
        startButton.setDisable(true);
        inputView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && inputView != null && !inputView.getText().isEmpty())
                inputIsAvailable(inputView.getText());
        });
        dataMemoryListView.setFocusTraversable(false);
        dataMemoryListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        dataMemoryHeader.setText("\t\tAddress\t  0\t+1\t+2\t+3");
        versionLabel.setText(NoBeardMachine.getVersion());
    }

    void setDebuggerButtonsDisable(boolean state) {
        stepButton.setDisable(state);
        continueButton.setDisable(state);
        stopButton.setDisable(state);
    }

    private void inputIsAvailable(String providedInput) {
        getOutputView().appendText(providedInput + "\n");
        input = providedInput;
        inputView.clear();
        inputView.setDisable(true);
        setDebuggerButtonsDisable(false);
        getSemaphore().release();
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
            machine.loadStringConstants(objectFile.getStringStorage());
            machine.loadProgram(0, objectFile.getProgram());
            dataMemoryListView.getItems().clear();
        } else
            outputView.appendText("Select a NoBeard object file\n");
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
        machine.removeAllBreakpoints();
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

    @FXML
    void startProgram(ActionEvent event) {
        setDebuggerButtonsDisable(false);
        startButton.setDisable(true);
        dataMemoryListView.getItems().clear();
        lastProgramLine = -1;
        new Thread(() -> {
            machine.runProgram(0);
            highlightNextInstructionToBeExecuted();
            Platform.runLater(() -> DataMemoryView.update(this, getRawDataMemoryList()));
        }).start();
    }

    private void highlightNextInstructionToBeExecuted() {
        if (programDataMap.containsKey(machine.getCurrentLine()))
            programDataMap.get(machine.getCurrentLine()).setStyle("-fx-background-color: #999999");
        else  {
            setDebuggerButtonsDisable(true);
            startButton.setDisable(false);
        }
        if (lastProgramLine > -1 && lastProgramLine != machine.getCurrentLine())
            programDataMap.get(lastProgramLine).setStyle("-fx-background-color: transparent");
        lastProgramLine = machine.getCurrentLine();
    }

    ObservableList<String> getRawDataMemoryList() {
        ObservableList<String> result = FXCollections.observableArrayList();
        for (int i = 0; i <= machine.getCallStack().getStackPointer(); i += 4) {
            StringBuilder line = new StringBuilder(String.format("%0" + 4 + "d", i));
            for (int j = i; j < i + 4; j++) {
                line.append(String.format("%0" + 3 + "d", machine.getDataMemory().loadByte(j)));
            }
            result.add(line.toString());
        }
        return result;
    }

    @FXML
    void step(ActionEvent event) {
        new Thread(() -> {
            if (machine.getBreakpoints().contains(machine.getCurrentLine()))
                machine.replaceBreakInstruction();
            machine.step();
            machine.setBreakInstructionIfNeeded();
            highlightNextInstructionToBeExecuted();
            Platform.runLater(() -> DataMemoryView.update(this, getRawDataMemoryList()));
        }).start();
    }

    @FXML
    void continueToBreakpoint(ActionEvent event) {
        new Thread(() -> {
            machine.step();
            machine.setBreakInstructionIfNeeded();
            machine.runUntilNextBreakpoint();
            highlightNextInstructionToBeExecuted();
            Platform.runLater(() -> DataMemoryView.update(this, getRawDataMemoryList()));
        }).start();
    }

    @FXML
    void stopProgram(ActionEvent event) {
        setDebuggerButtonsDisable(true);
        startButton.setDisable(false);
        machine.stopProgram();
        dataMemoryListView.getItems().clear();
        programDataMap.get(lastProgramLine).setStyle("-fx-background-color: transparent");
    }
}