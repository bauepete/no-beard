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
package compiler;

import machine.InstructionSet.Instruction;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.semantics.AssemblerCodeChecker;

/**
 * Holds acceptance tests
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class NoBeardCompilerTest {

    public NoBeardCompilerTest() {
    }

    /**
     * PAR01
     */
    @Test
    public void testEmptyProgram() {
        String source = "unit empty; do done empty;";
        byte[] expectedCode = {
            Instruction.INC.getId(), 0, 0,
            Instruction.HALT.getId()
        };

        testCompilationWithSourceString(source, expectedCode);
    }

    private void testCompilationWithSourceString(String source, byte[] expectedCode) {
        NoBeardCompiler.setSourceString(source);
        assertTrue(NoBeardCompiler.compile());
        assertTrue(NoBeardCompiler.lastCompilationWasSuccessful());
        AssemblerCodeChecker.assertCodeEquals(expectedCode, NoBeardCompiler.getByteCode());
    }

    @Test
    public void testSourceFileNotFoundException() {
        String source = "SamplePrograms/FileNotFound.nb";
        NoBeardCompiler.setSourceFile(source);
        assertFalse(NoBeardCompiler.compile());
        assertEquals(1, NoBeardCompiler.getErrorList().size());
        assertEquals("Error in line 1: Source file SamplePrograms/FileNotFound.nb not found.", NoBeardCompiler.getErrorList().get(0));
    }

    /**
     * PAR05
     */
    @Test
    public void testVariableDeclarationWithAssignment() {
        String source = "unit foo; do int x = 3; done foo;";
        byte[] expectedCode = {
            Instruction.INC.getId(), 0, 4,
            Instruction.LA.getId(), 0, 0, 32,
            Instruction.LIT.getId(), 0, 3,
            Instruction.STO.getId(),
            Instruction.HALT.getId()
        };
        testCompilationWithSourceString(source, expectedCode);
    }

    /**
     * PAR06
     */
    @Test
    public void testAssignmentToStringsAndPutThem() {
        String source = "SamplePrograms/NoBeardPrograms/VariableWorld.nb";
        byte[] expected = {
            Instruction.INC.getId(), 0, 29, // stack pointer plus size of vars
            Instruction.LA.getId(), 0, 0, 32, // address of string x
            Instruction.LIT.getId(), 0, 0, // address of source string
            Instruction.LIT.getId(), 0, 12, // length of string
            Instruction.ASSN.getId(), // Array assignment
            Instruction.LA.getId(), 0, 0, 32, // Load address of var to print
            Instruction.LIT.getId(), 0, 12, // Length of string
            Instruction.LIT.getId(), 0, 12, // Width parameter of print instr.
            Instruction.OUT.getId(), 2, // Output of a string
            Instruction.LA.getId(), 0, 0, 44, // address of string y
            Instruction.LIT.getId(), 0, 12, // address of source string
            Instruction.LIT.getId(), 0, 17, // length of second string
            Instruction.ASSN.getId(), // Array assignment
            Instruction.HALT.getId()
        };
        testCompilationWithSourceFile(source, expected);
    }

    /**
     * PAR07
     */
    @Test
    public void testOutputOfSimpleInt() {
        String source = "unit bah; do int x = 3; put (x); done bah;";
        byte[] expectedCode = {
            Instruction.INC.getId(), 0, 4,
            Instruction.LA.getId(), 0, 0, 32,
            Instruction.LIT.getId(), 0, 3,
            Instruction.STO.getId(),
            Instruction.LV.getId(), 0, 0, 32,
            Instruction.LIT.getId(), 0, 1,
            Instruction.OUT.getId(), 0,
            Instruction.HALT.getId()
        };
        testCompilationWithSourceString(source, expectedCode);
    }

    @Test
    public void testOutputOfIntExpression() {
        String source = "unit rsch; do int x = 3; int y = 1; put (x + y); done rsch;";
        byte[] expectedCode = {
            Instruction.INC.getId(), 0, 8,
            Instruction.LA.getId(), 0, 0, 32,
            Instruction.LIT.getId(), 0, 3,
            Instruction.STO.getId(),
            Instruction.LA.getId(), 0, 0, 36,
            Instruction.LIT.getId(), 0, 1,
            Instruction.STO.getId(),
            Instruction.LV.getId(), 0, 0, 32,
            Instruction.LV.getId(), 0, 0, 36,
            Instruction.ADD.getId(),
            Instruction.LIT.getId(), 0, 1,
            Instruction.OUT.getId(), 0,
            Instruction.HALT.getId()
        };
        testCompilationWithSourceString(source, expectedCode);
    }

    @Test
    public void testOutputOfSimpleChar() {
        String source = "unit foo; do char x = \"x\"; put(x); done foo;";
        byte[] expected = {
            Instruction.INC.getId(), 0, 1,
            Instruction.LA.getId(), 0, 0, 32,
            Instruction.LIT.getId(), 0, 120, // ascii code of 'x'
            Instruction.STC.getId(),
            Instruction.LC.getId(), 0, 0, 32,
            Instruction.LIT.getId(), 0, 1,
            Instruction.OUT.getId(), 1,
            Instruction.HALT.getId()
        };
        testCompilationWithSourceString(source, expected);
    }

    @Test
    public void testOutputOfString() {
        String sourceFilePath = "SamplePrograms/NoBeardPrograms/HelloWorld.nb";
        byte[] expected = {
            Instruction.INC.getId(), 0, 0,
            Instruction.LIT.getId(), 0, 0, // address of string
            Instruction.LIT.getId(), 0, 11, // length of string
            Instruction.LIT.getId(), 0, 11, // width parameter of print instr.
            Instruction.OUT.getId(), 2,
            Instruction.OUT.getId(), 3,
            Instruction.HALT.getId()
        };
        testCompilationWithSourceFile(sourceFilePath, expected);
    }

    private void testCompilationWithSourceFile(String sourceFilePath, byte[] expected) {
        NoBeardCompiler.setSourceFile(sourceFilePath);
        assertTrue(NoBeardCompiler.compile());
        AssemblerCodeChecker.assertCodeEquals(expected, NoBeardCompiler.getByteCode());
    }
}
