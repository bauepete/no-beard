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
package io;

import nbm.InstructionSet.Instruction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class BinaryFileTest {

    private final String testFilePath = "test/io";

    public BinaryFileTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreation() {
        BinaryFile file = BinaryFile.get(testFilePath + "EmptyFile.no");
        assertEquals(testFilePath + "EmptyFile.no", file.getPath());
        assertArrayEquals(new byte[]{}, file.getStringStorage());
        assertArrayEquals(new byte[]{}, file.getProgram());
    }

    @Test
    public void testSetProperties() {
        byte[] exampleStringStorage = {
            (byte) 'f', (byte) 'o', (byte) 'o'
        };
        byte[] exampleProgram = {
            Instruction.LA.getId(), 0, 0, 32,
            Instruction.LIT.getId(), 0, 0,
            Instruction.LIT.getId(), 0, 3,
            Instruction.ASSN.getId()
        };

        BinaryFile file = BinaryFile.get(testFilePath + "Sample.no");
        file.setStringStorage(exampleStringStorage);
        assertArrayEquals(exampleStringStorage, file.getStringStorage());

        file.setProgram(exampleProgram);
        assertArrayEquals(exampleProgram, file.getProgram());
    }
    
    @Test
    public void testSetNullProperties() {
        byte[] exampleStringStorage = {
            (byte) 'f', (byte) 'o', (byte) 'o'
        };
        byte[] exampleProgram = {
            Instruction.LA.getId(), 0, 0, 32,
            Instruction.LIT.getId(), 0, 0,
            Instruction.LIT.getId(), 0, 3,
            Instruction.ASSN.getId()
        };

        BinaryFile file = BinaryFile.get(testFilePath + "NullSample.no");
        file.setStringStorage(exampleStringStorage);
        file.setProgram(exampleProgram);
        
        file.setStringStorage(null);
        assertArrayEquals(new byte[]{}, file.getStringStorage());
        assertArrayEquals(exampleProgram, file.getProgram());
        
        file.setProgram(null);
        assertArrayEquals(new byte[]{}, file.getProgram());
    }
}
