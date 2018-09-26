package machine;

import error.Error;
import error.ErrorHandler;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Observable;

import static machine.InstructionSet.Instruction.*;
import static org.junit.Assert.*;

public class BreakpointsHandlerTest {
    private ErrorHandler errorHandler = new ErrorHandler(new FakeSourceCodeInfo());
    private byte[] program = {
            LA.getId(), 0, 32, 0,
            LIT.getId(), 5, 0,
            STO.getId()
    };
    private ProgramMemory programMemory = new ProgramMemory(program, errorHandler);
    private BreakpointsHandler bph = new BreakpointsHandler(programMemory);


    @Test
    public void testCreation() {
        assertEquals(0, bph.getAllBreakpoints().size());
    }

    @Test
    public void testSetBreakpointFailing() {
        bph.setBreakpoint(-1);
        assertEquals(0, bph.getAllBreakpoints().size());
    }

    @Test
    public void testSetBreakpoint() {
        bph.setBreakpoint(0);
        bph.setBreakpoint(7);
        assertEquals(2, bph.getAllBreakpoints().size());
        assertEquals(BREAK.getId(), program[0]);
        assertEquals(BREAK.getId(), program[7]);
    }

    @Test
    public void testRemoveBreakpointFailing() {
        bph.setBreakpoint(0);
        bph.removeBreakpoint(-1);
        assertEquals(1, bph.getAllBreakpoints().size());
        assertEquals(Error.ErrorType.PROGRAM_ADDRESS_ERROR.getNumber(), errorHandler.getLastError().getNumber());
    }

    @Test
    public void removeBreakpoint() {
        bph.setBreakpoint(4);
        bph.removeBreakpoint(4);
        assertTrue(bph.getAllBreakpoints().isEmpty());
        assertEquals(LIT.getId(), program[4]);
    }

    @Test
    public void testOnStopAtBreakpointWithInvalidAddress() {
        bph.setBreakpoint(4);
        bph.onStopAtBreakpoint(3);
        assertEquals(BREAK.getId(), program[4]);
    }

    @Test
    public void testOnStopAtBreakpoint() {
        bph.setBreakpoint(4);
        bph.onStopAtBreakpoint(4);
        assertEquals(LIT.getId(), program[4]);
    }

    @Test
    public void testOnContinueFromBreakpoint() {
        bph.setBreakpoint(7);
        bph.onStopAtBreakpoint(7);
        bph.onContinueFromBreakpoint();
        assertEquals(BREAK.getId(), program[7]);
    }

    @Test
    public void clearBreakpoints() {
        bph.setBreakpoint(0);
        bph.setBreakpoint(4);
        bph.setBreakpoint(7);
        assertEquals(3, bph.getAllBreakpoints().size());
        bph.clearAllBreakpoints();
        assertTrue(bph.getAllBreakpoints().isEmpty());
    }

    @Test
    public void testUpdateFromInvalidObservable() {
        Observable o = new Observable() {

        };
        bph.setBreakpoint(0);
        bph.update(o, 0);
        assertEquals(BREAK.getId(), program[0]);
    }

    @Test
    public void testUpdate() {
        DataMemory dataMemory = new DataMemory(32, errorHandler);
        ControlUnit cu = new ControlUnit(programMemory, dataMemory, new CallStack(dataMemory, 0, 32),
                errorHandler, new FakeInputDevice(), new FakeOutputDevice());
        bph.setBreakpoint(0);
        bph.update(cu, 0);
        assertEquals(LA.getId(), program[0]);
    }

    @Test
    @Ignore
    public void replaceInstructionAtAddress() {
    }
}