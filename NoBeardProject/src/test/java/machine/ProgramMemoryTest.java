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

import error.Error;
import error.ErrorHandler;
import machine.InstructionSet.Instruction;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class ProgramMemoryTest {
    
    public ProgramMemoryTest() {
    }
    
    private ErrorHandler errorHandler;
    
    @Before
    public void setUp() {
        errorHandler = new ErrorHandler(new FakeSourceCodeInfo());
    }

    @Test
    public void testStore() {
        ProgramMemory programMemory = new ProgramMemory(1024, errorHandler);
        byte[] program = {
            Instruction.LIT.getId(), (byte) 0, (byte) 42
        };
        programMemory.store(0, program);
        assertEquals(Instruction.LIT.getId(), programMemory.loadByte(0));
        assertEquals(0, programMemory.loadByte(1));
        assertEquals(42, programMemory.loadByte(2));
    }
    
    @Test
    public void testStoreMemoryOverflowError() {
        ProgramMemory programMemory = new ProgramMemory(2, errorHandler);
        byte[] program = {
            Instruction.LIT.getId(), (byte) 0, (byte) 42
        };
        programMemory.store(0, program);
        assertEquals(error.Error.ErrorType.PROGRAM_MEMORY_OVERFLOW.getNumber(), errorHandler.getLastError().getNumber());
    }
    
    @Test
    public void testLoadByteTooHighAddress() {
        ProgramMemory programMemory = new ProgramMemory(1024, errorHandler);
        byte[] program = {
            Instruction.LIT.getId(), (byte) 0, (byte) 42
        };
        programMemory.store(0, program);
        programMemory.loadByte(1024);
        assertEquals(error.Error.ErrorType.PROGRAM_ADDRESS_ERROR.getNumber(), errorHandler.getLastError().getNumber());
    }

    @Test
    public void testLoadByteTooLowAddress() {
        ProgramMemory programMemory = new ProgramMemory(1024, errorHandler);
        programMemory.loadByte(-1);
        assertEquals(Error.ErrorType.PROGRAM_ADDRESS_ERROR.getNumber(), errorHandler.getLastError().getNumber());
    }
    
    @Test
    public void testLoadHalfWord() {
        ProgramMemory programMemory = new ProgramMemory(1024, errorHandler);
        byte[] program = {
            Instruction.LIT.getId(), (byte) 1, (byte) 0
        };
        programMemory.store(0, program);
        assertEquals(256, programMemory.loadHalfWord(1));
    }
    
    @Test
    public void testLoadHalfWordTooHighAddress() {
        ProgramMemory programMemory = new ProgramMemory(1024, errorHandler);
        byte[] program = {
            Instruction.LIT.getId(), (byte) 1, (byte) 0
        };
        programMemory.store(0, program);
        programMemory.loadHalfWord(1023);
        assertEquals(error.Error.ErrorType.PROGRAM_ADDRESS_ERROR.getNumber(), errorHandler.getLastError().getNumber());
    }

    @Test
    public void testLoadHalfWordTooLowAddress() {
        ProgramMemory programMemory = new ProgramMemory(32, errorHandler);
        programMemory.loadHalfWord(-1);
        assertEquals(error.Error.ErrorType.PROGRAM_ADDRESS_ERROR.getNumber(), errorHandler.getLastError().getNumber());
    }

    @Test
    public void testReplaceInstructionAtIllegalAddress() {
        ProgramMemory programMemory = new ProgramMemory(1024, errorHandler);
        byte instruction = Instruction.INC.getId();

        boolean successfullyReplaced = programMemory.replaceInstruction(-1, instruction);
        assertThat(successfullyReplaced, is(false));
        assertThat(errorHandler.getLastError().getNumber(), is(Error.ErrorType.PROGRAM_ADDRESS_ERROR.getNumber()));

        successfullyReplaced = programMemory.replaceInstruction(1024, instruction);
        assertThat(successfullyReplaced, is(false));
        assertThat(errorHandler.getLastError().getNumber(), is(Error.ErrorType.PROGRAM_ADDRESS_ERROR.getNumber()));
    }

    @Test
    public void testReplaceInstruction() {
        ProgramMemory programMemory = new ProgramMemory(new byte[32], errorHandler);
        byte instruction = Instruction.STO.getId();

        boolean successfullyReplaced = programMemory.replaceInstruction(0, instruction);
        assertThat(successfullyReplaced, is(true));
    }
}
