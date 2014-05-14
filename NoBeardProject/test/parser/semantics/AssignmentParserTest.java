/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import compiler.NbCompiler;
import nbm.Code;
import nbm.Nbm.Opcode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.AssignmentParser;
import scanner.Scanner;
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
     * Test of parse method, of class AssignmentParser.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        NbCompiler comp = new NbCompiler(new SrcStringReader("x = 3"));
        Scanner scanner = comp.getScanner();
        SymListManager sym = comp.getSymListManager();
        Code code = comp.getCode();
        AssignmentParser p = new AssignmentParser(scanner, sym, code, comp.getErrorHandler());
        byte[] expResult = {
            Opcode.LA.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 3,
            Opcode.STO.byteCode()
        };
        
        sym.newUnit(1);
        sym.newVar(0, SymListManager.ElementType.INT);
        
        assertEquals("Parse ", true, p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code: ", expResult, code.getByteCode());
    }
}
