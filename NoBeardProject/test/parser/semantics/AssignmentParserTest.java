/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import error.ErrorHandler;
import nbm.CodeGenerator;
import nbm.Nbm.Opcode;
import org.junit.After;
import org.junit.Before;
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

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parseOldStyle method, of class AssignmentParser.
     */
    @Test
    public void testParse() {
        byte[] expResult = {
            Opcode.LA.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 3,
            Opcode.STO.byteCode()
        };
        Parser p = setupTestEnvironmentAndParser("x = 3;");

        assertEquals("Parse ", true, p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code: ", expResult, ParserFactory.getCodeGenerator().getByteCode());
        assertEquals(Symbol.EOFSY, scanner.getCurrentToken().getSy());
    }

    private Parser setupTestEnvironmentAndParser(final String srcLine) {
        setupParserFactory(srcLine);
        fillSymbolList();
        return ParserFactory.createAssignmentParser();
    }

    private void setupParserFactory(final String srcLine) {
        SrcReader srcReader = new SrcStringReader(srcLine);
        ErrorHandler errorHandler = new ErrorHandler(srcReader);
        CodeGenerator codeGen = new CodeGenerator(32);
        scanner = new Scanner(srcReader, errorHandler);
        ParserFactory.setup(srcReader, errorHandler, scanner, codeGen, new SymbolTable(codeGen, scanner, errorHandler));
    }

    private void fillSymbolList() {
        ParserFactory.getSymbolListManager().newUnit(1);
        ParserFactory.getSymbolListManager().newVar(0, SymbolTable.ElementType.INT);
    }
    
    @Test
    public void testSemicolonMissing() {
        Parser p = setupTestEnvironmentAndParser("x = 3");
        assertFalse(p.parse());
    }
    
    @Test
    public void testUndefinedVariable() {
        setupParserFactory("x = 3;");
        Parser p = ParserFactory.create(AssignmentParser.class);
        assertFalse(p.parse());
    }
}
