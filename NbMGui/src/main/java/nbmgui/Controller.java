package nbmgui;

import io.BinaryFile;
import io.BinaryFileHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import machine.NoBeardMachine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    private ListView<String> dataMemoryView;

    public void initialize() {
        machine = new NoBeardMachine(new FxInputDevice(this), new FxOutputDevice(this));
        semaphore = new Semaphore(0);
        programDataMap = new HashMap<>();
        inputView.setDisable(true);
        startButton.setDisable(true);
        setDebuggerButtonsDisable(true);
        inputView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && inputView != null && !inputView.getText().isEmpty())
                inputIsAvailable(inputView.getText());
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
            machine.loadStringConstants(objectFile.getStringStorage());
            machine.loadProgram(0, objectFile.getProgram());
        } else
            outputView.appendText("Select a NoBeard object file\n");
    }

    @FXML
    void startProgram(ActionEvent event) {
        setDebuggerButtonsDisable(false);
        dataMemoryView.getItems().clear();
        lastProgramLine = -1;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                machine.runProgram(0);
                highlightNextInstructionToBeExecuted();
                updateDataMemory();
            }
        });
        thread.start();
    }

    @FXML
    void step(ActionEvent event) {
        if (machine.getBreakpoints().contains(machine.getCurrentLine()))
            machine.replaceBreakInstruction();
        machine.step();
        machine.setBreakInstructionIfNeeded();
        highlightNextInstructionToBeExecuted();
        updateDataMemory();
    }

    @FXML
    void continueToBreakpoint(ActionEvent event) {
        machine.step();
        machine.setBreakInstructionIfNeeded();
        machine.runUntilNextBreakpoint();
        highlightNextInstructionToBeExecuted();
        updateDataMemory();
    }

    @FXML
    void stopProgram(ActionEvent event) {
        setDebuggerButtonsDisable(true);
        machine.stopProgram();
        programDataMap.get(lastProgramLine).setStyle("-fx-background-color: transparent");
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

    private ObservableList<String> getDataMemory() {
        ObservableList<String> result = FXCollections.observableArrayList();
        result.add("\t\tAddress\t  0\t+1\t+2\t+3");
        for (int i = 0; i < machine.getCurrentLine(); i+=4) {
            StringBuilder line = new StringBuilder(String.format("\t\t%0" + 4 + "d\t", i));
            for (int j = i; j < i+4; j++) {
                line.append(String.format("\t%0" + 3 + "d", machine.getDataMemory().loadByte(j)));
            }
            result.add(line.toString());
        }
        return result;
    }

    private void updateDataMemory() {
        dataMemoryView.setItems(getDataMemory());
        dataMemoryView.setCellFactory((list) -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        if (getIndex() == 0) {
                            setText(item);
                            setStyle("-fx-background-color: #002c73;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-font-weight: bold;");
                        } else {
                            HBox line = new HBox();
                            int address = (getIndex() - 1) * 4;
                            setPointer(address, item).forEach(line.getChildren()::add);
                            setGraphic(line);
                        }
                    }
                }
            };
        });
    }

    private List<Label> setPointer(int startAddress, String line) {
        List<Label> result = new ArrayList<>();
        String[] split = line.split("\t");
        int framePointer = machine.getCallStack().getFramePointer();
        int stackPointer = machine.getCallStack().getStackPointer();
        for (int i = 0; i < split.length; i++) {
            int currentAddress = i - 4 + startAddress;
            Label label = new Label(split[i] + "\t");
            if (currentAddress >= startAddress && currentAddress == framePointer) {
                label.setStyle("-fx-background-color: #0038AC;" +
                        "-fx-text-fill: white;");
            }
            if (currentAddress >= startAddress && currentAddress == stackPointer) {
                result.get(1).setStyle("-fx-text-fill: #ac080e;");
                label.setStyle("-fx-background-color: #ac080e;" +
                        "-fx-text-fill: white;");
            }
            result.add(label);
        }
        return result;
    }

    private void highlightNextInstructionToBeExecuted() {
        if (programDataMap.containsKey(machine.getCurrentLine()))
            programDataMap.get(machine.getCurrentLine()).setStyle("-fx-background-color: #999999");
        else
            setDebuggerButtonsDisable(true);
        if (lastProgramLine > -1)
            programDataMap.get(lastProgramLine).setStyle("-fx-background-color: transparent");
        lastProgramLine = machine.getCurrentLine();
    }

    private void setDebuggerButtonsDisable(boolean state) {
        stepButton.setDisable(state);
        continueButton.setDisable(state);
        stopButton.setDisable(state);
    }

    private void inputIsAvailable(String providedInput) {
        this.getOutputView().appendText(providedInput + "\n");
        this.input = providedInput;
        this.inputView.clear();
        this.inputView.setDisable(true);
        this.getSemaphore().release();
    }
}