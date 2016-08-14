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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import machine.InstructionSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class BinaryFileHandlerTest {

    private final String testFilePath = "test/io";

    public BinaryFileHandlerTest() {
    }

    @Before
    public void setUp() throws IOException {
    }

    @After
    public void tearDown() throws IOException {
        deleteAllBinaryFiles();
    }

    private void deleteAllBinaryFiles() throws IOException {
        Path path = Paths.get(testFilePath);
        Files.list(path).filter(p -> p.toString().endsWith(".no")).forEach((p) -> {
            try {
                Files.deleteIfExists(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testEmptyFile() throws IOException {
        BinaryFile emptyFile = BinaryFile.get(testFilePath + "/EmptyFile.no");
        BinaryFileHandler.save(emptyFile);
        Path p = Paths.get(testFilePath + "/EmptyFile.no");
        assertTrue(Files.isRegularFile(p));
        BinaryFile readFile = BinaryFileHandler.open(testFilePath + "/EmptyFile.no");
        assertEquals(testFilePath + "/EmptyFile.no", readFile.getPath());
        assertArrayEquals(new byte[]{}, readFile.getProgram());
        assertArrayEquals(new byte[]{}, readFile.getStringStorage());
    }

    @Test
    public void testNonEmptyFile() throws IOException {
        byte[] exampleStringStorage = {
            (byte) 'f', (byte) 'o', (byte) 'o'
        };
        byte[] exampleProgram = {
            InstructionSet.Instruction.LA.getId(), 0, 0, 32,
            InstructionSet.Instruction.LIT.getId(), 0, 0,
            InstructionSet.Instruction.LIT.getId(), 0, 3,
            InstructionSet.Instruction.ASSN.getId(),
            InstructionSet.Instruction.HALT.getId()
        };
        BinaryFile exampleFile = BinaryFile.get(testFilePath + "/ExampleProgram.no");
        exampleFile.setStringStorage(exampleStringStorage);
        exampleFile.setProgram(exampleProgram);
        
        BinaryFileHandler.save(exampleFile);
        BinaryFile readFile = BinaryFileHandler.open(testFilePath + "/ExampleProgram.no");
        
        assertArrayEquals(exampleStringStorage, readFile.getStringStorage());
        assertArrayEquals(exampleProgram, readFile.getProgram());
    }

    @Test
    public void testNonEmptyFileWithMissingHalt() throws IOException {
        byte[] exampleStringStorage = {
            (byte) 'f', (byte) 'o', (byte) 'o'
        };
        byte[] exampleProgram = {
            InstructionSet.Instruction.LA.getId(), 0, 0, 32,
            InstructionSet.Instruction.LIT.getId(), 0, 0,
            InstructionSet.Instruction.LIT.getId(), 0, 3,
            InstructionSet.Instruction.ASSN.getId(),
        };
        BinaryFile exampleFile = BinaryFile.get(testFilePath + "/ExampleProgram.no");
        exampleFile.setStringStorage(exampleStringStorage);
        exampleFile.setProgram(exampleProgram);        
        BinaryFileHandler.save(exampleFile);
        
        BinaryFile readFile = BinaryFileHandler.open(testFilePath + "/ExampleProgram.no");
        
        byte[] expectedTrash = new byte[exampleStringStorage.length + exampleProgram.length];
        System.arraycopy(exampleProgram, 0, expectedTrash, 0, exampleProgram.length);
        System.arraycopy(exampleStringStorage, 0, expectedTrash, exampleProgram.length, exampleStringStorage.length);
        
        assertArrayEquals(expectedTrash, readFile.getProgram());
    }
}
