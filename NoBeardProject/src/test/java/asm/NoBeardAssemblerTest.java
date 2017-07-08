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
package asm;

import machine.InstructionSet.Instruction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.ParserFactory;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class NoBeardAssemblerTest {
    
    public NoBeardAssemblerTest() {
    }
    
    @Before
    public void setUp() {
        ParserFactory.shutDown();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of assemble method, of class NoBeardAssembler.
     */
    @Test
    public void testSetSourceString() {
        assertFalse(ParserFactory.isReady());
        NoBeardAssembler.setSourceString("\"wtf\"lit 0 lit 3 lit 3 out 2 out 3 halt");
        assertTrue(ParserFactory.isReady());
        assertFalse(NoBeardAssembler.wasSuccessfull());
    }
    
    @Test
    public void testAssemble() {
        NoBeardAssembler.setSourceString("\"wtf\"lit 0 lit 3 lit 3 out 2 out 3 halt");
        assertFalse(NoBeardAssembler.wasSuccessfull());
        assertTrue(NoBeardAssembler.assemble());
        assertTrue(NoBeardAssembler.wasSuccessfull());
        
        byte[] expectedProgram = {
            Instruction.LIT.getId(), 0, 0,
            Instruction.LIT.getId(), 0, 3,
            Instruction.LIT.getId(), 0, 3,
            Instruction.OUT.getId(), 2,
            Instruction.OUT.getId(), 3,
            Instruction.HALT.getId()
        };
        assertArrayEquals(expectedProgram, NoBeardAssembler.getByteCode());
        
        byte[] expectedStringStorage = {
            (int) 'w', (int) 't', (int) 'f'
        };
        assertArrayEquals(expectedStringStorage, NoBeardAssembler.getStringStorage());
    }
}
