/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import error.ErrorHandler;
import error.SourceCodeInfo;
import nbm.CodeGenerator;
import nbm.Nbm.Opcode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import parser.Parser;
import parser.ParserFactory;
import scanner.Scanner;
import scanner.SrcReader;
import scanner.SrcStringReader;
import symboltable.SymListManager;

/**
 *
 * @author peter
 */
public class AssignmentParserTest {

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
        System.out.println("parse");
        byte[] expResult = {
            Opcode.LA.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 3,
            Opcode.STO.byteCode()
        };
        SrcReader srcReader = new SrcStringReader("x = 3");
        ErrorHandler errorHandler = new ErrorHandler(srcReader);
        CodeGenerator codeGen = new CodeGenerator(32);
        Scanner scanner = new Scanner(srcReader, errorHandler);

        ParserFactory.setup(srcReader, errorHandler, scanner, codeGen, new SymListManager(codeGen, scanner, errorHandler));
        ParserFactory.getSymbolListManager().newUnit(1);
        ParserFactory.getSymbolListManager().newVar(0, SymListManager.ElementType.INT);

        Parser p = ParserFactory.createAssignmentParser();

        assertEquals("Parse ", true, p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code: ", expResult, ParserFactory.getCodeGenerator().getByteCode());
    }
}
