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
package parser;

import machine.CodeGenerator;
import machine.InstructionSet;
import machine.InstructionSet.Instruction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class LabelToAddressMapTest {

    private static final int PROGRAM_STORAGE_SIZE = 1024;

    private CodeGenerator codeGenerator;
    private LabelToAddressMap ltm;

    public LabelToAddressMapTest() {
    }

    @Before
    public void setUp() {
        codeGenerator = new CodeGenerator(PROGRAM_STORAGE_SIZE);
        ltm = new LabelToAddressMap(codeGenerator);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddWithAddress() {
        ltm.add(".some_label", 5);
        ltm.add(".another_label", 8);
        assertEquals(5, ltm.getAddress(".some_label"));
        assertEquals(8, ltm.getAddress(".another_label"));
        assertEquals(0, ltm.getUnresolvedJmpAddresses(".some_label").size());
        assertEquals(0, ltm.getUnresolvedJmpAddresses(".another_label").size());
    }

    @Test
    public void testGetAddressOfNonExistingLabel() {
        codeGenerator.emit(Instruction.JMP);
        assertEquals(LabelToAddressMap.UNDEFINED_ADDRESS, ltm.getAddress(".some_label"));
        codeGenerator.emit(LabelToAddressMap.UNDEFINED_ADDRESS);
        assertEquals(1, ltm.getUnresolvedJmpAddresses(".some_label").size());
        assertEquals(1, (int)ltm.getUnresolvedJmpAddresses(".some_label").get(0));
    }

    @Test
    public void testAddReferencedLabel() {
        codeGenerator.emit(Instruction.JMP);
        assertEquals(LabelToAddressMap.UNDEFINED_ADDRESS, ltm.getAddress(".some_label"));
        codeGenerator.emit(LabelToAddressMap.UNDEFINED_ADDRESS);
        
        ltm.add(".some_label", 5);
        assertEquals(5, ltm.getAddress(".some_label"));
        assertEquals(5, codeGenerator.getCodeHalfWord(1));
        assertEquals(0, ltm.getUnresolvedJmpAddresses(".some_label").size());
    }
    
    @Test
    public void testMultiplyReferencedLabel() {
        codeGenerator.emit(Instruction.JMP);
        codeGenerator.emit(ltm.getAddress(".some_label"));
        codeGenerator.emit(Instruction.JMP);
        codeGenerator.emit(ltm.getAddress(".some_label"));
        ltm.add(".some_label", 6);
        assertEquals(6, codeGenerator.getCodeHalfWord(1));
        assertEquals(6, codeGenerator.getCodeHalfWord(4));
    }
    
    @Test
    public void testHasUndefinedLabels() {
        ltm.getAddress(".some_label");
        ltm.getAddress(".another_label");
        
        assertTrue(ltm.hasUndefinedLabels());
        assertArrayEquals(new String[] {".another_label", ".some_label"}, ltm.getUndefinedLabels());
        
        ltm.add(".some_label", 17);
        ltm.add(".another_label", 42);
        assertFalse(ltm.hasUndefinedLabels());
    }
}
