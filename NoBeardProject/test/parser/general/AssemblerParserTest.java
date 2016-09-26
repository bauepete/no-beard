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
package parser.general;

import parser.AssemblerParser;
import error.ErrorHandler;
import io.SourceReader;
import io.SourceStringReader;
import machine.CodeGenerator;
import machine.InstructionSet.Instruction;
import machine.NoBeardMachine;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.ParserFactory;
import scanner.NameManager;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symboltable.SymbolTable;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class AssemblerParserTest {

    private ErrorHandler errorHandler;
    private AssemblerParser p;
    private byte[] expectedProgramMemory;

    @Test
    public void testAssembleEmptyProgram() {
        setupTest("", new byte[]{});
        assertTrue(p.parse());
    }

    private void setupTest(final String assemblerInstruction, final byte[] machineCode) {
        SourceReader sr = new SourceStringReader(assemblerInstruction);
        errorHandler = new ErrorHandler(sr);
        Scanner scanner = new Scanner(sr, errorHandler, new NameManager(sr));
        SymbolTable symbolTable = new SymbolTable(scanner, errorHandler);
        CodeGenerator codeGenerator = new CodeGenerator(NoBeardMachine.MAX_PROG);
        ParserFactory.setup(sr, errorHandler, scanner, codeGenerator, symbolTable);
        p = ParserFactory.create(AssemblerParser.class);

        expectedProgramMemory = machineCode;
    }

    @Test
    public void testAssembleOneInstruction() {
        setupTest("halt", new byte[]{
            Instruction.HALT.getId()
        });
        assertTrue(p.parse());
        assertArrayEquals(expectedProgramMemory, p.getByteCode());
    }

    @Test
    public void testAdd() {
        byte[] expectedCode = {
            Instruction.ADD.getId()
        };
        setupTest("add", expectedCode);
        assertTrue(p.parse());
        assertArrayEquals(expectedProgramMemory, p.getByteCode());
    }

    @Test
    public void testInvalidOpcode() {
        setupTest("if", new byte[]{});
        assertFalse(p.parse());
        assertEquals(error.Error.ErrorType.SYMBOL_EXPECTED.getNumber(), errorHandler.getLastError().getNumber());
    }

    @Test
    public void testInvalidOpcode2() {
        setupTest("foo", new byte[]{});
        assertFalse(p.parse());
        assertEquals(error.Error.ErrorType.SYMBOL_EXPECTED.getNumber(), errorHandler.getLastError().getNumber());
    }

    @Test
    public void testAssembleInstructionWithAOneByteOperand() {
        setupTest("out 1", new byte[]{Instruction.OUT.getId(), 1});
        assertTrue(p.parse());
        assertEquals(Symbol.EOFSY, ParserFactory.getScanner().getCurrentToken().getSymbol());
        assertArrayEquals(expectedProgramMemory, p.getByteCode());
    }

    @Test
    public void testAssembleInstructionWithOneByteOperandTooLarge() {
        setupTest("out 256", new byte[]{});
        assertFalse(p.parse());
        assertEquals(error.Error.ErrorType.OPERAND_RANGE_ERROR.getNumber(), errorHandler.getLastError().getNumber());
    }

    @Test
    public void testAssembleInstructionWithAHalfWordOperand() {
        setupTest("lit 65535", new byte[]{Instruction.LIT.getId(), (byte) 255, (byte) 255});
        assertTrue(p.parse());
        assertArrayEquals(expectedProgramMemory, p.getByteCode());
    }

    @Test
    public void testAssembleInstructionWithAHalfWordOperandTooLarge() {
        setupTest("lit 65536", new byte[]{Instruction.LIT.getId(), (byte) 255, (byte) 255});
        assertFalse(p.parse());
        assertEquals(error.Error.ErrorType.OPERAND_RANGE_ERROR.getNumber(), errorHandler.getLastError().getNumber());
    }

    @Test
    public void testAssembleInstructionWithOneByteAndTwoByteOperand() {
        setupTest("la 0 32", new byte[]{Instruction.LA.getId(), 0, 0, 32});
        assertTrue(p.parse());
        assertEquals(Symbol.EOFSY, ParserFactory.getScanner().getCurrentToken().getSymbol());
        assertArrayEquals(expectedProgramMemory, p.getByteCode());
    }

    @Test
    public void testAssembleMoreInstructions() {
        byte[] expectedCode = {
            Instruction.INC.getId(), 0, 8,
            Instruction.LA.getId(), 0, 0, 32,
            Instruction.LIT.getId(), 0, 17,
            Instruction.STO.getId(),
            Instruction.LA.getId(), 0, 0, 36,
            Instruction.LV.getId(), 0, 0, 32,
            Instruction.STO.getId(),
            Instruction.HALT.getId()
        };
        setupTest("inc 8 la 0 32 lit 17 sto la 0 36 lv 0 32 sto halt", expectedCode);
        assertTrue(p.parse());
        assertEquals(Symbol.EOFSY, ParserFactory.getScanner().getCurrentToken().getSymbol());
        assertArrayEquals(expectedProgramMemory, p.getByteCode());
    }

    @Test
    public void testIncludeStringConstants() {
        byte[] expectedCode = {
            Instruction.LIT.getId(), 0, 0,
            Instruction.LIT.getId(), 0, 5,
            Instruction.LIT.getId(), 0, 6,
            Instruction.OUT.getId(), 2,
            Instruction.LIT.getId(), 0, 5,
            Instruction.LIT.getId(), 0, 5,
            Instruction.LIT.getId(), 0, 5,
            Instruction.OUT.getId(), 2,
            Instruction.OUT.getId(), 3,
            Instruction.HALT.getId()
        };
        byte[] expectedStringConstants = {
            (byte) 'H', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o',
            (byte) 'w', (byte) 'o', (byte) 'r', (byte) 'l', (byte) 'd'
        };
        setupTest("\"Helloworld\"lit 0 lit 5 lit 6 out 2 lit 5 lit 5 lit 5 out 2 out 3 halt", expectedCode);
        assertTrue(p.parse());
        assertArrayEquals(expectedProgramMemory, p.getByteCode());
        assertArrayEquals(expectedStringConstants, ParserFactory.getScanner().getStringManager().getStringStorage());
    }
}
