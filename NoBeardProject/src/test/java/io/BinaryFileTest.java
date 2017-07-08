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

import machine.InstructionSet.Instruction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class BinaryFileTest {

    private final String TEST_FILE_PATH = "test/io/";

    public BinaryFileTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreationOfEmptyFile() {
        BinaryFile file = BinaryFile.get(TEST_FILE_PATH + "EmptyFile.no");
        assertEquals(TEST_FILE_PATH + "EmptyFile.no", file.getPath());

        assertArrayEquals(new byte[]{'v', 1, 0, 0}, file.getVersion());
        assertArrayEquals(new byte[]{}, file.getStringStorage());
        assertArrayEquals(new byte[]{}, file.getProgram());

        assertArrayEquals(new byte[]{'1', '7', 'v', 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, file.getByteStream());
    }

    @Test
    public void testSetByteStreamForEmptyFile() {
        BinaryFile file = BinaryFile.get(TEST_FILE_PATH + "EmptyFile.no");
        file.setByteStream(new byte[]{'1', '7', 'v', 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        assertArrayEquals(new byte[]{'v', 1, 4, 0}, file.getVersion());
        assertArrayEquals(new byte[]{}, file.getStringStorage());
        assertArrayEquals(new byte[]{}, file.getProgram());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetByteStreamWithInvalidFormatId() {
        BinaryFile file = BinaryFile.get(TEST_FILE_PATH + "InvalidFile.no");
        file.setByteStream(new byte[]{'2', '7', 'v', 1, 4, 0});
    }

    @Test
    public void testSetString() {
        BinaryFile file = getTestFile();
        assertArrayEquals(new byte[]{'v', 1, 0, 0}, file.getVersion());
        assertArrayEquals(new byte[]{'f', 'o', 'o'}, file.getStringStorage());
        assertArrayEquals(new byte[]{}, file.getProgram());
        assertArrayEquals(new byte[]{'1', '7', 'v', 1, 0, 0, 0, 0, 0, 3, 'f', 'o', 'o', 0, 0, 0, 0}, file.getByteStream());
    }

    private BinaryFile getTestFile() {
        byte[] exampleStringStorage = {
            (byte) 'f', (byte) 'o', (byte) 'o'
        };
        BinaryFile file = BinaryFile.get(TEST_FILE_PATH + "TestFile.no");
        file.setStringStorage(exampleStringStorage);
        return file;
    }

    @Test
    public void testSetByteStreamWithString() {
        BinaryFile file = BinaryFile.get(TEST_FILE_PATH + "TestFile.no");
        file.setByteStream(new byte[]{'1', '7', 'v', 1, 0, 0, 0, 0, 0, 3, 'b', 'a', 'r', 0, 0, 0, 0});
        assertArrayEquals(new byte[]{'b', 'a', 'r'}, file.getStringStorage());
        assertArrayEquals(new byte[]{}, file.getProgram());
    }

    @Test
    public void testSetStringAndProgram() {
        byte[] exampleStringStorage = {
            'f', 'o', 'o', 'b', 'a', 'r'
        };
        byte[] exampleProgram = {
            Instruction.LA.getId(), 0, 0, 32,
            Instruction.LIT.getId(), 0, 0,
            Instruction.HALT.getId(),
            Instruction.LIT.getId(), 0, 3,
            Instruction.ASSN.getId()
        };

        byte[] expectedByteStream = {
            '1', '7', 'v', 1, 0, 0,
            0, 0, 0, 6, 'f', 'o', 'o', 'b', 'a', 'r',
            0, 0, 0, 12, Instruction.LA.getId(), 0, 0, 32,
            Instruction.LIT.getId(), 0, 0,
            Instruction.HALT.getId(),
            Instruction.LIT.getId(), 0, 3,
            Instruction.ASSN.getId()
        };
        BinaryFile file = BinaryFile.get(TEST_FILE_PATH + "Sample.no");
        file.setStringStorage(exampleStringStorage);
        file.setProgram(exampleProgram);
        assertArrayEquals(expectedByteStream, file.getByteStream());
    }

    @Test
    public void testSetByteStreamWithStringAndProgram() {
        BinaryFile file = BinaryFile.get(TEST_FILE_PATH + "FullSample.no");
        file.setByteStream(new byte[]{
            '1', '7', 'v', 1, 1, 0,
            0, 0, 0, 5, 'f', 'u', 'p', 's', 'i',
            0, 0, 0, 13, Instruction.LA.getId(), 0, 0, 32,
            Instruction.LIT.getId(), 0, 0,
            Instruction.HALT.getId(),
            Instruction.LIT.getId(), 0, 3,
            Instruction.ASSN.getId(),
            Instruction.HALT.getId()
        });

        byte[] expectedProgram = {
            Instruction.LA.getId(), 0, 0, 32,
            Instruction.LIT.getId(), 0, 0,
            Instruction.HALT.getId(),
            Instruction.LIT.getId(), 0, 3,
            Instruction.ASSN.getId(),
            Instruction.HALT.getId()
        };

        assertArrayEquals(new byte[]{'v', 1, 1, 0}, file.getVersion());
        assertArrayEquals(new byte[]{'f', 'u', 'p', 's', 'i'}, file.getStringStorage());
        assertArrayEquals(expectedProgram, file.getProgram());
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

        BinaryFile file = BinaryFile.get(TEST_FILE_PATH + "NullSample.no");
        file.setStringStorage(exampleStringStorage);
        file.setProgram(exampleProgram);

        file.setStringStorage(null);
        assertArrayEquals(new byte[]{}, file.getStringStorage());
        assertArrayEquals(exampleProgram, file.getProgram());

        file.setProgram(null);
        assertArrayEquals(new byte[]{}, file.getProgram());
    }
}
