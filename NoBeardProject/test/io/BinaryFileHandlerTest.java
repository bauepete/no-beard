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
import org.junit.Ignore;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class BinaryFileHandlerTest {

    private final String TEST_DIRECTORY = "test/io";
    private final String TEST_FILE = "/ExampleProgram.no";
    private final String TEST_FILE_PATH = TEST_DIRECTORY + TEST_FILE;

    public BinaryFileHandlerTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() throws IOException {
        deleteAllBinaryFiles();
    }

    private void deleteAllBinaryFiles() throws IOException {
        Path path = Paths.get(TEST_DIRECTORY);
        Files.list(path).filter(p -> p.toString().endsWith(".no")).forEach((p) -> {
            try {
                Files.deleteIfExists(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testEmptyFileSave() throws IOException {
        BinaryFile emptyFile = BinaryFile.get(TEST_DIRECTORY + "/EmptyFile.no");
        BinaryFileHandler.save(emptyFile);
        Path p = Paths.get(TEST_DIRECTORY + "/EmptyFile.no");
        assertTrue(Files.isRegularFile(p));
        
        byte[] expectedFileContent = {
            '1', '7', 'v', 1, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0
        };
        byte[] fileContent = Files.readAllBytes(p);
        assertArrayEquals(expectedFileContent, fileContent);
    }


    @Test
    public void testOpenFile() throws IOException {
        byte[] exampleStringStorage = {
            (byte) 'f', (byte) 'o', (byte) 'o'
        };
        byte[] exampleProgram = {
            InstructionSet.Instruction.LA.getId(), 0, 0, 32,
            InstructionSet.Instruction.LIT.getId(), 0, 0,
            InstructionSet.Instruction.LIT.getId(), 0, 3,
            InstructionSet.Instruction.ASSN.getId(),
        };
        BinaryFile exampleFile = BinaryFile.get(TEST_FILE_PATH);
        exampleFile.setStringStorage(exampleStringStorage);
        exampleFile.setProgram(exampleProgram);        
        BinaryFileHandler.save(exampleFile);
        
        BinaryFile readFile = BinaryFileHandler.open(TEST_FILE_PATH);
        
        assertArrayEquals(new byte[] {'v', 1, 0, 0}, readFile.getVersion());
        assertArrayEquals(exampleStringStorage, readFile.getStringStorage());
        assertArrayEquals(exampleProgram, readFile.getProgram());
    }
    
    @Test
    public void testFileWithHaltNotAtEnd() throws IOException {
        byte[] exampleStringStorage = {
            (byte) 'f', (byte) 'o', (byte) 'o'
        };
        byte[] exampleProgram = {
            InstructionSet.Instruction.LA.getId(), 0, 0, 32,
            InstructionSet.Instruction.LIT.getId(), 0, 0,
            InstructionSet.Instruction.HALT.getId(),
            InstructionSet.Instruction.LIT.getId(), 0, 3,
            InstructionSet.Instruction.ASSN.getId(),
        };
        
        BinaryFile exampleFile = BinaryFile.get(TEST_FILE_PATH);
        exampleFile.setStringStorage(exampleStringStorage);
        exampleFile.setProgram(exampleProgram);
        BinaryFileHandler.save(exampleFile);
        
        BinaryFile readFile = BinaryFileHandler.open(TEST_FILE_PATH);
        assertArrayEquals(exampleStringStorage, readFile.getStringStorage());
        assertArrayEquals(exampleProgram, readFile.getProgram());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNoNoBeardFile() throws IOException {
        Path path = Paths.get(TEST_FILE_PATH);
        Files.write(path, new byte[] {});
        
        BinaryFile readFile = BinaryFileHandler.open(TEST_FILE_PATH);
    }
}
