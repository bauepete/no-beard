/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import error.ErrorHandler;
import nbm.Code;
import nbm.Nbm.Opcode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.ExprParser;
import scanner.Scanner;
import scanner.SrcStringReader;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class ExprParserTest {

    private Scanner addS;
    private Scanner addMulS;
    private Scanner complexExprS;
    
    private SymListManager sym;
    private Code c;

    public ExprParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        ErrorHandler.getInstance().reset();
        addS = new Scanner(new SrcStringReader("a + b"));
        addMulS = new Scanner(new SrcStringReader("a - b * 3"));
        complexExprS = new Scanner(new SrcStringReader("-5 * (a + b)/17"));

        c = new Code();
        sym = new SymListManager(c, addS);
        sym.newUnit(25);
        sym.newVar(0, SymListManager.ElementType.INT);
        sym.newVar(1, SymListManager.ElementType.INT);
        Operand.setSymListManager(sym);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parse method, of class ExprParser.
     */
    @Test
    public void testParseAdd() {
        System.out.println("testParseAdd");
        
        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.ADD.byteCode()
        };

        Scanner s = addS;
        s.nextToken();
        ExprParser p = new ExprParser(s, sym, c);

        assertEquals("Parse ", true, p.parse());
        assertCodeEquals("Code ", expected, c.getByteCode());
    }

    /**
     * Test of parse method, of class ExprParser.
     */
    @Test
    public void testParseAddMul() {
        System.out.println("testParseAddMul");
        
        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.LIT.byteCode(), 0, 3,
            Opcode.MUL.byteCode(),
            Opcode.SUB.byteCode()
        };

        Scanner s = addMulS;
        s.nextToken();
        ExprParser p = new ExprParser(s, sym, c);

        assertEquals("Parse ", true, p.parse());
        assertCodeEquals("Code ", expected, c.getByteCode());
    }

    /**
     * Test of parse method, of class ExprParser.
     */
    @Test
    public void testComplexExpr() {
        System.out.println("testComplexExpr");
        // "-5 * (a + b)/17"
        byte[] expected = {
            Opcode.LIT.byteCode(), 0, 5,
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.ADD.byteCode(),
            Opcode.MUL.byteCode(),
            Opcode.LIT.byteCode(), 0, 17,
            Opcode.DIV.byteCode(),
            Opcode.NEG.byteCode(),
        };

        Scanner s = complexExprS;
        s.nextToken();
        ExprParser p = new ExprParser(s, sym, c);

        assertEquals("Parse ", true, p.parse());
        assertCodeEquals("Code ", expected, c.getByteCode());
    }
    
    private void assertCodeEquals(String msg, byte[] exp, byte[] act) {
        AssmCodeChecker.assertCodeEquals(msg, exp, act);
    }
}
