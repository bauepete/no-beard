package machine;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * Created by Egon on 31.08.2017.
 */
public class Debugger implements Observer {
    private HashMap<Integer, Byte> breakpoints;
    private ProgramMemory programMemory;

    Debugger(ProgramMemory programMemory) {
        this.programMemory = programMemory;
        breakpoints = new HashMap<>();
    }

    public void setProgramMemory(ProgramMemory programMemory) {
        this.programMemory = programMemory;
    }

    void setBreakpoint(Integer address, Byte instructionId) {
        this.breakpoints.put(address, instructionId);
        programMemory.replaceInstruction(address, InstructionSet.Instruction.BREAK.getId());
    }

    void removeBreakpoint(Integer address) {
        programMemory.replaceInstruction(address, breakpoints.get(address));
        this.breakpoints.remove(address);
    }

    void clearBreakpoints() {
        breakpoints = new HashMap<>();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            int address = (int) arg;
            replaceInstructionAtAddress(address, null);
        }
    }

    public void replaceInstructionAtAddress(int address, InstructionSet.Instruction instruction) {
        if (!breakpoints.isEmpty() && breakpoints.keySet().contains(address)) {
            if (instruction != null)
                programMemory.replaceInstruction(address, instruction.getId());
            else
                programMemory.replaceInstruction(address, breakpoints.get(address));
        }
    }

    public Set<Integer> getAllBreakpoints() {
        return breakpoints.keySet();
    }
}
