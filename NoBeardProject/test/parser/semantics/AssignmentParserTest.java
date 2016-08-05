/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import error.ErrorHandler;
import nbm.CodeGenerator;
import nbm.InstructionSet.Opcode;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.AssignmentParser;
import parser.Parser;
import parser.ParserFactory;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import scanner.SrcReader;
import scanner.SrcStringReader;
import symboltable.SymbolTable;

/**
 *
 * @author peter
 */
public class AssignmentParserTest {

    private Scanner scanner;
    
    public AssignmentParserTest() {
    }

    /**
     * Test assignment to simple int variable.
     */
    @Test
    public void testAssignmentToSimpleInt() {
        byte[] expResult = {
            Opcode.LA.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 3,
            Opcode.STO.byteCode()
        };
        Parser p = setupTestEnvironmentAndParser("x = 3;");

        assertEquals("Parse ", true, p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code: ", expResult, ParserFactory.getCodeGenerator().getByteCode());
        assertEquals(Symbol.EOFSY, scanner.getCurrentToken().getSymbol());
    }

    private Parser setupTestEnvironmentAndParser(final String srcLine) {
        setupParserFactory(srcLine);
        fillSymbolList();
        return ParserFactory.create(AssignmentParser.class);
    }

    private void setupParserFactory(final String srcLine) {
        SrcReader srcReader = new SrcStringReader(srcLine);
        ErrorHandler errorHandler = new ErrorHandler(srcReader);
        CodeGenerator codeGen = new CodeGenerator(32);
        scanner = new Scanner(srcReader, errorHandler);
        ParserFactory.setup(srcReader, errorHandler, scanner, codeGen, new SymbolTable(scanner, errorHandler));
    }

    private void fillSymbolList() {
        ParserFactory.getSymbolListManager().newUnit(1);
        ParserFactory.getSymbolListManager().newVar(0, SymbolTable.ElementType.INT);
    }
    
    @Test
    public void testSemicolonMissing() {
        Parser p = setupTestEnvironmentAndParser("x = 3");
        assertFalse(p.parse());
        assertEquals(error.Error.ErrorType.SYMBOL_EXPECTED.getNumber(), ParserFactory.getErrorHandler().getLastError().getNumber());
    }
    
    @Test
    public void testNoAssignmentStatement() {
        Parser p = setupTestEnvironmentAndParser("x == 3");
        assertFalse(p.parse());
        assertEquals(error.Error.ErrorType.SYMBOL_EXPECTED.getNumber(), ParserFactory.getErrorHandler().getLastError().getNumber());
    }
    
    @Test
    public void testUndefinedVariable() {
        setupParserFactory("x = 3;");
        Parser p = ParserFactory.create(AssignmentParser.class);
        assertFalse(p.parse());
        assertEquals(error.Error.ErrorType.NAME_UNDEFINED.getNumber(), ParserFactory.getErrorHandler().getLastError().getNumber());
    }
    
    @Test
    public void testAssignmentToArray() {
        byte[] expectedCode = {
            Opcode.LA.byteCode(), 0, 0, 32, // load address of x
            Opcode.LIT.byteCode(), 0, 0, // load address of string constant
            Opcode.LIT.byteCode(), 0, 5, // length of string constant
            Opcode.ASSN.byteCode()
        };
        setupParserFactory("x = 'fubar';");
        ParserFactory.getSymbolListManager().newUnit(1);
        ParserFactory.getSymbolListManager().newVar(0, SymbolTable.ElementType.CHAR, 5);
        Parser p = ParserFactory.create(AssignmentParser.class);
        assertTrue(p.parse());
        AssemblerCodeChecker.assertCodeEquals(expectedCode, ParserFactory.getCodeGenerator().getByteCode());
    }
}
