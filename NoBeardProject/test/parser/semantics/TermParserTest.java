/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import nbm.Code;
import nbm.Nbm.Opcode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.TermParser;
import scanner.Scanner;
import scanner.SrcStringReader;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class TermParserTest {

    private Scanner mulS;
    private Scanner divS;
    
    private SymListManager sym;
    private Code c;

    public TermParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        mulS = new Scanner(new SrcStringReader("a * b"));
        divS = new Scanner(new SrcStringReader("1 / 2"));
        c = new Code();
        sym = new SymListManager(c, mulS);
        sym.newUnit(25);
        sym.newVar(0, SymListManager.ElementType.INT);
        sym.newVar(1, SymListManager.ElementType.INT);
        Operand.setSymListManager(sym);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parse method, of class TermParser.
     */
    @Test
    public void testParseMulVar() {
        System.out.println("testParseMul");
        Scanner s = mulS;

        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.MUL.byteCode()
        };

        s.nextToken();
        TermParser p = new TermParser(s, sym, c);
        assertEquals("Parse", true, p.parse());
        assertCodeEquals("Code ", expected, c.getByteCode());
    }

    /**
     * Test of parse method, of class TermParser.
     */
    @Test
    public void testParseDivConst() {
        System.out.println("testParseDivConst");
        Scanner s = divS;

        byte[] expected = {
            Opcode.LIT.byteCode(), 0, 1,
            Opcode.LIT.byteCode(), 0, 2,
            Opcode.DIV.byteCode()
        };

        s.nextToken();
        TermParser p = new TermParser(s, sym, c);
        assertEquals("Parse", true, p.parse());
        assertCodeEquals("Code ", expected, c.getByteCode());
    }

    private void assertCodeEquals(String msg, byte[] exp, byte[] act) {
        AssmCodeChecker.assertCodeEquals(msg, exp, act);
    }
}
