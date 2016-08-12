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
import nbm.CodeGenerator;
import nbm.InstructionSet.Instruction;
import nbm.NoBeardMachine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.ParserFactory;
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

    public AssemblerParserTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAssembleOneInstruction() {
        setupTest("halt", new byte[]{
            Instruction.HALT.getId()
        });
        assertTrue(p.parse());
        assertArrayEquals(expectedProgramMemory, p.getByteCode());
    }

    private void setupTest(final String assemblerInstruction, final byte[] machineCode) {
        SourceReader sr = new SourceStringReader(assemblerInstruction);
        errorHandler = new ErrorHandler(sr);
        Scanner scanner = new Scanner(sr, errorHandler);
        SymbolTable symbolTable = new SymbolTable(scanner, errorHandler);
        CodeGenerator codeGenerator = new CodeGenerator(NoBeardMachine.MAX_PROG);
        ParserFactory.setup(sr, errorHandler, scanner, codeGenerator, symbolTable);
        p = ParserFactory.create(AssemblerParser.class);

        expectedProgramMemory = new byte[1024];
        System.arraycopy(machineCode, 0, expectedProgramMemory, 0, machineCode.length);
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
        setupTest("out 1", new byte[] {Instruction.OUT.getId(), 1});
        assertTrue(p.parse());
        assertEquals(Symbol.EOFSY, ParserFactory.getScanner().getCurrentToken().getSymbol());
        assertArrayEquals(expectedProgramMemory, p.getByteCode());
    }
    
    @Test
    public void testAssembleInstructionWithOneByteOperandTooLarge() {
        setupTest("out 256", new byte[] {});
        assertFalse(p.parse());
        assertEquals(error.Error.ErrorType.OPERAND_RANGE_ERROR.getNumber(), errorHandler.getLastError().getNumber());
    }
    
    @Test
    public void testAssembleInstructionWithAHalfWordOperand() {
        setupTest("lit 65535", new byte[] {Instruction.LIT.getId(), (byte) 255, (byte) 255});
        assertTrue(p.parse());
        assertArrayEquals(expectedProgramMemory, p.getByteCode());
    }
    
    @Test
    public void testAssembleInstructionWithAHalfWordOperandTooLarge() {
        setupTest("lit 65536", new byte[] {Instruction.LIT.getId(), (byte) 255, (byte) 255});
        assertFalse(p.parse());
        assertEquals(error.Error.ErrorType.OPERAND_RANGE_ERROR.getNumber(), errorHandler.getLastError().getNumber());
    }
    
    @Test
    public void testAssembleInstructionWithOneByteAndTwoByteOperand() {
        setupTest("la 0 32", new byte[] {Instruction.LA.getId(), 0, 0, 32});
        assertTrue(p.parse());
        assertEquals(Symbol.EOFSY, ParserFactory.getScanner().getCurrentToken().getSymbol());
        assertArrayEquals(expectedProgramMemory, p.getByteCode());
    }
}
