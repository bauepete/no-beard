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

import nbm.Nbm.Opcode;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
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
            Opcode.INC.byteCode(), 0, 0,
            Opcode.HALT.byteCode()
        };

        testCompilationWithSourceString(source, expectedCode);
    }

    private void testCompilationWithSourceString(String source, byte[] expectedCode) {
        NoBeardCompiler.setSourceString(source);
        assertTrue(NoBeardCompiler.compile());
        AssemblerCodeChecker.assertCodeEquals(expectedCode, NoBeardCompiler.getByteCode());
    }

    @Test
    public void testSourceFileNotFoundException() {
        String source = "SamplePrograms/FileNotFound.nb";
        NoBeardCompiler.setSourceFile(source);
        assertFalse(NoBeardCompiler.compile());
    }

    /**
     * PAR05
     */
    @Test
    public void testVariableDeclarationWithAssignment() {
        String source = "unit foo; do int x = 3; done foo;";
        byte[] expectedCode = {
            Opcode.INC.byteCode(), 0, 4,
            Opcode.LA.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 3,
            Opcode.STO.byteCode(),
            Opcode.HALT.byteCode()
        };
        testCompilationWithSourceString(source, expectedCode);
    }

    /**
     * PAR06
     */
    @Test
    @Ignore
    public void testAssignmentToStringsAndPutThem() {
        String source = "SamplePrograms/VariableWorld.nb";
        byte[] expected = {
            Opcode.INC.byteCode(), 0, 29, // stack pointer plus size of vars
            Opcode.LA.byteCode(), 0, 0, 32, // address of string x
            Opcode.LIT.byteCode(), 0, 0, // address of source string
            Opcode.LIT.byteCode(), 0, 12, // length of string
            Opcode.ASSN.byteCode(), // Array assignment
            Opcode.LA.byteCode(), 0, 0, 32, // Load address of var to print
            Opcode.LIT.byteCode(), 0, 12, // Length of string
            Opcode.LIT.byteCode(), 0, 12, // Width parameter of print instr.
            Opcode.PUT.byteCode(), 2, // Output of a string
            Opcode.LA.byteCode(), 0, 0, 44, // address of string y
            Opcode.LIT.byteCode(), 0, 12, // address of source string
            Opcode.LIT.byteCode(), 0, 17, // length of second string
            Opcode.ASSN.byteCode(), // Array assignment
            Opcode.HALT.byteCode()
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
            Opcode.INC.byteCode(), 0, 4,
            Opcode.LA.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 3,
            Opcode.STO.byteCode(),
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 0,
            Opcode.PUT.byteCode(), 0,
            Opcode.HALT.byteCode()
        };
        testCompilationWithSourceString(source, expectedCode);
    }

    @Test
    public void testOutputOfIntExpression() {
        String source = "unit rsch; do int x = 3; int y = 1; put (x + y); done rsch;";
        byte[] expectedCode = {
            Opcode.INC.byteCode(), 0, 8,
            Opcode.LA.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 3,
            Opcode.STO.byteCode(),
            Opcode.LA.byteCode(), 0, 0, 36,
            Opcode.LIT.byteCode(), 0, 1,
            Opcode.STO.byteCode(),
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.ADD.byteCode(),
            Opcode.LIT.byteCode(), 0, 0,
            Opcode.PUT.byteCode(), 0,
            Opcode.HALT.byteCode()
        };
        testCompilationWithSourceString(source, expectedCode);
    }

    @Test
    public void testOutputOfSimpleChar() {
        String source = "unit foo; do char x = \"x\"; put(x); done foo;";
        byte[] expected = {
            Opcode.INC.byteCode(), 0, 1,
            Opcode.LA.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 120, // ascii code of 'x'
            Opcode.STC.byteCode(),
            Opcode.LC.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 0,
            Opcode.PUT.byteCode(), 1,
            Opcode.HALT.byteCode()
        };
        testCompilationWithSourceString(source, expected);
    }

    @Test
    public void testOutputOfString() {
        String sourceFilePath = "SamplePrograms/HelloWorld.nb";
        byte[] expected = {
            Opcode.INC.byteCode(), 0, 0,
            Opcode.LIT.byteCode(), 0, 0, // address of string
            Opcode.LIT.byteCode(), 0, 11, // length of string
            Opcode.LIT.byteCode(), 0, 11, // width parameter of print instr.
            Opcode.PUT.byteCode(), 2,
            Opcode.PUT.byteCode(), 3,
            Opcode.HALT.byteCode()
        };
        testCompilationWithSourceFile(sourceFilePath, expected);
    }

    private void testCompilationWithSourceFile(String sourceFilePath, byte[] expected) {
        NoBeardCompiler.setSourceFile(sourceFilePath);
        assertTrue(NoBeardCompiler.compile());
        AssemblerCodeChecker.assertCodeEquals(expected, NoBeardCompiler.getByteCode());
    }
}
