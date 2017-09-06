package machine;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Egon on 31.08.2017.
 */
public class Debugger implements Observer {
    private final HashMap<Integer, InstructionSet.Instruction> breakpoints;
    private final ProgramMemory programMemory;
    private byte lastInstructionId;
    private Integer currentBreakpoint;

    Debugger(ProgramMemory programMemory) {
        this.programMemory = programMemory;
        breakpoints = new HashMap<>();
    }

    void setBreakpoint(Integer address, InstructionSet.Instruction instruction) {
        this.breakpoints.put(address, instruction);
    }

    void removeBreakpoint(Integer address) {
        this.breakpoints.remove(address);
    }

    void clearBreakpoints() {
        this.breakpoints.clear();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null)
            prepareNextBreakpoint((Integer) arg);
        else
            programMemory.replaceInstruction(currentBreakpoint, lastInstructionId);
    }

    void prepareNextBreakpoint(int pc) {
        if (!breakpoints.isEmpty()) {
            currentBreakpoint = breakpoints.keySet().stream().filter(e -> e > pc).findFirst().orElse(null);
            if (currentBreakpoint != null) {
                lastInstructionId = breakpoints.get(currentBreakpoint).getId();
                programMemory.replaceInstruction(currentBreakpoint, (byte) 32);
            }
        }
    }
}
