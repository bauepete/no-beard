package machine;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * Created by Egon on 31.08.2017.
 */
public class BreakpointsHandler implements Observer {
    private HashMap<Integer, Byte> breakpoints;
    private ProgramMemory programMemory;

    BreakpointsHandler(ProgramMemory programMemory) {
        this.programMemory = programMemory;
        breakpoints = new HashMap<>();
    }

    /**
     * @param atAddress
     * @param instructionId
     * @deprecated
     */
    void setBreakpoint(Integer atAddress, Byte instructionId) {
        boolean instructionReplacedSuccessfully = programMemory.replaceInstruction(atAddress, InstructionSet.Instruction.BREAK.getId());
        if (instructionReplacedSuccessfully)
            breakpoints.put(atAddress, instructionId);
    }

    void setBreakpoint(int atAddress) {
        byte originalInstruction = programMemory.loadByte(atAddress);
        if (originalInstruction != -1) {
            breakpoints.put(atAddress, programMemory.loadByte(atAddress));
            programMemory.replaceInstruction(atAddress, InstructionSet.Instruction.BREAK.getId());
        }
    }

    void removeBreakpoint(int atAddress) {
        if (breakpoints.containsKey(atAddress)) {
            programMemory.replaceInstruction(atAddress, breakpoints.get(atAddress));
            breakpoints.remove(atAddress);
        } else {
            // force program address error
            programMemory.loadByte(-1);
        }
    }

    void clearAllBreakpoints() {
        breakpoints.clear();
    }


    Set<Integer> getAllBreakpoints() {
        return breakpoints.keySet();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ControlUnit && arg != null) {
            int address = (int) arg;
            replaceInstructionAtAddress(address, null);
        }
    }

    void replaceInstructionAtAddress(int address, InstructionSet.Instruction instruction) {
        if (!breakpoints.isEmpty() && breakpoints.keySet().contains(address)) {
            if (instruction != null)
                programMemory.replaceInstruction(address, instruction.getId());
            else
                programMemory.replaceInstruction(address, breakpoints.get(address));
        }
    }
}
