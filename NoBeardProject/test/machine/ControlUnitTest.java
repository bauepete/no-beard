/*
 * Copyright ©2016. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
 * Department of Informatics and Media Technique, HTBLA Leonding, 
 * Limesstr. 12 - 14, 4060 Leonding, AUSTRIA. All Rights Reserved. Permission
 * to use, copy, modify, and distribute this software and its documentation
 * for educational, research, and not-for-profit purposes, without fee and
 * without a signed licensing agreement, is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies, modifications, and distributions. Contact the Head of
 * Informatics and Media Technique, HTBLA Leonding, Limesstr. 12 - 14,
 * 4060 Leonding, Austria, for commercial licensing opportunities.
 * 
 * IN NO EVENT SHALL HTBLA LEONDING BE LIABLE TO ANY PARTY FOR DIRECT,
 * INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST
 * PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF HTBLA LEONDING HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * HTBLA LEONDING SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE. THE SOFTWARE AND ACCOMPANYING DOCUMENTATION, IF ANY,
 * PROVIDED HEREUNDER IS PROVIDED "AS IS". HTBLA LEONDING HAS NO OBLIGATION
 * TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */
package machine;

import error.ErrorHandler;
import machine.InstructionSet.Instruction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class ControlUnitTest {
    
    private ErrorHandler errorHandler;
    private DataMemory dataMemory;
    private CallStack callStack;
    private ProgramMemory programMemory;
    private ControlUnit controlUnit;
    
    public ControlUnitTest() {
    }
    
    @Before
    public void setUp() {
        errorHandler = new ErrorHandler(new FakeSourceCodeInfo());
        dataMemory = new DataMemory(1024, errorHandler);
        callStack = new CallStack(dataMemory, 0, 28);
        programMemory = new ProgramMemory(1024, errorHandler);
        controlUnit = new ControlUnit(programMemory, dataMemory, callStack, errorHandler);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testConstruction() {
        assertEquals(0, controlUnit.getPc());
        assertEquals(ControlUnit.MachineState.STOPPED, controlUnit.getMachineState());
    }
    
    @Test
    public void testGetLiteral() {
        byte[] program = {
            Instruction.LIT.getId(), (byte) 1, (byte) 255
        };
        programMemory.store(0, program);
        controlUnit.fetchInstruction();
        assertEquals(511, controlUnit.getLiteral());
    }
    
    @Test
    public void testGetDisplacementAndGetAddress() {
        byte[] program = {
            Instruction.LA.getId(), (byte) 1, (byte) 1, (byte) 0
        };
        programMemory.store(0, program);
        controlUnit.fetchInstruction();
        assertEquals(1, controlUnit.getDisplacement());
        assertEquals(256, controlUnit.getAddress());
    }

    @Test
    public void testGetType() {
        byte[] program = {
            Instruction.REL.getId(), 3
        };
        programMemory.store(0, program);
        controlUnit.fetchInstruction();
        assertEquals(3, controlUnit.getType());
    }

    @Test
    public void testFetchInstruction() {
        byte[] program = {
            Instruction.LA.getId(), 0, 0, 32,
        };
        programMemory.store(0, program);
        controlUnit.fetchInstruction();
        assertEquals(0, controlUnit.getDisplacement());
        assertEquals(32, controlUnit.getAddress());
    }
    
    @Test
    public void testOneInstructionCycle() {
        byte[] program = {
            Instruction.LA.getId(), 0, 0, 32,
        };
        programMemory.store(0, program);
        controlUnit.fetchInstruction();
        controlUnit.executeInstruction();
        assertEquals(32, callStack.peek());
    }
    
    @Test
    public void testExecCycle() {
        byte[] program = {
            Instruction.LA.getId(), 0, 0, 32,
            Instruction.LIT.getId(), 0, 17,
            Instruction.STO.getId()
        };
        programMemory.store(0, program);
        assertEquals(0, controlUnit.getPc());
        controlUnit.executeCycle();
        assertEquals(4, controlUnit.getPc());
        assertEquals(ControlUnit.MachineState.STOPPED, controlUnit.getMachineState());
    }
    
    @Test
    public void testExecJumpCycle() {
        byte[] program = {
            Instruction.JMP.getId(), 0, 32
        };
        programMemory.store(0, program);
        controlUnit.executeCycle();
        assertEquals(32, controlUnit.getPc());
    }
}
