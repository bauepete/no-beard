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
package nbm;

import error.ErrorHandler;
import nbm.InstructionSet.Instruction;
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
        dataMemory = new DataMemory(32, errorHandler);
        callStack = new CallStack(dataMemory, 0, 28);
        programMemory = new ProgramMemory(1024, errorHandler);
        controlUnit = new ControlUnit(programMemory, dataMemory, callStack);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testConstruction() {
        assertEquals(0, controlUnit.getPc());
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
}
