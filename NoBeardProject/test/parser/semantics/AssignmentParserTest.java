/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import nbm.Nbm.Opcode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import parser.Parser;
import parser.ParserFactory;
import scanner.SrcStringReader;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class AssignmentParserTest {

    public AssignmentParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
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
    @Ignore
    public void testParse() {
        System.out.println("parse");
        byte[] expResult = {
            Opcode.LA.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 3,
            Opcode.STO.byteCode()
        };

        ParserFactory.setup(new SrcStringReader("x = 3"));
        ParserFactory.getSymbolListManager().newUnit(1);
        ParserFactory.getSymbolListManager().newVar(0, SymListManager.ElementType.INT);

        Parser p = ParserFactory.createAssignmentParser();

        assertEquals("Parse ", true, p.parseOldStyle());
        AssemblerCodeChecker.assertCodeEquals("Code: ", expResult, ParserFactory.getCodeGenerator().getByteCode());
    }
}
