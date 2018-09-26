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
    private int programCurrentlyStoppedAtAddress = -1;

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

    /**
     * Stores a breakpoint for a given address. If the given address is invalid (out of range of program memory)
     * a PROGRAM_ADDRESS_ERROR is thrown and nothing is stored.
     * @param atAddress The address where the breakpoint shall be stored.
     * @implNote The method does not check whether an instruction is stored at the given address.
     */
    void setBreakpoint(int atAddress) {
        byte originalInstruction = programMemory.loadByte(atAddress);
        if (originalInstruction != -1) {
            breakpoints.put(atAddress, programMemory.loadByte(atAddress));
            programMemory.replaceInstruction(atAddress, InstructionSet.Instruction.BREAK.getId());
        }
    }

    /**
     * Deletes a breakpoint at a given address. If there is no breakpoint stored at the given address a
     * PROGRAM_ADDRESS_ERROR is thrown
     * @param atAddress The address where the breakpoint shall be deleted.
     */
    void removeBreakpoint(int atAddress) {
        if (breakpoints.containsKey(atAddress)) {
            programMemory.replaceInstruction(atAddress, breakpoints.get(atAddress));
            breakpoints.remove(atAddress);
        } else {
            // force program address error
            programMemory.loadByte(-1);
        }
    }

    /**
     * onStopAtBreakpoint restores the original instruction at the given address. If the given
     * address does not have a breakpoint the method does nothing.
     * @param atAddress The address where the original instruction shall be restored
     */
    void onStopAtBreakpoint(int atAddress) {
        if (breakpoints.containsKey(atAddress)) {
            programMemory.replaceInstruction(atAddress, breakpoints.get(atAddress));
            programCurrentlyStoppedAtAddress = atAddress;
        }
    }

    /**
     * Restores the break instruction at the address where the last onStopAtBreakpoint was called.
     */
    void onContinueFromBreakpoint() {
        programMemory.replaceInstruction(programCurrentlyStoppedAtAddress, InstructionSet.Instruction.BREAK.getId());
    }

    /**
     * Deletes all breakpoints
     */
    void clearAllBreakpoints() {
        breakpoints.clear();
    }

    /**
     *
     * @return All addresses where a breakpoint is stored.
     */
    Set<Integer> getAllBreakpoints() {
        return breakpoints.keySet();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ControlUnit && arg != null) {
            int atAddress = (int) arg;
            onStopAtBreakpoint(atAddress);
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
