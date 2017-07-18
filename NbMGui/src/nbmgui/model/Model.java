package nbmgui.model;

import io.BinaryFile;
import io.BinaryFileHandler;
import machine.ControlUnit;
import machine.NoBeardMachine;

import java.io.IOException;

/**
 * Created by Egon on 15.07.2017.
 */
public class Model {
    private String path = "HelloWorld.no";
    NoBeardMachine machine;


    public void startProgram() {
        BinaryFile objectFile;
        try {
            objectFile = BinaryFileHandler.open(path);
        } catch (IOException ex) {
            System.err.println("Unable to open " + path);
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
