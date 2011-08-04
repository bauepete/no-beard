/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import nbm.Code;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.FactParser;
import scanner.Scanner;
import scanner.SrcStringReader;
import symlist.Operand.OperandKind;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class FactParserTest {

    private Scanner identS = new Scanner(new SrcStringReader("a25"));
    private Scanner numberS = new Scanner(new SrcStringReader("42"));
    private Code c;
    private SymListManager sym;

    public FactParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        c = new Code();
        sym = new SymListManager(c, identS);
        sym.newUnit(25);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parse method, of class FactParser.
     */
    @Test
    public void testParseIdentifier() {
        System.out.println("testParseIdentifier");

        Scanner s = identS;
        s.nextToken();
        sym.newVar(s.getCurrentToken().getValue(), SymListManager.ElementType.INT);

        FactParser p = new FactParser(s, sym, c);

        assertEquals("Parse ", true, p.parse());
        assertEquals("Operand ", OperandKind.VARIABLE, p.getOperand().getKind());
    }

    @Test
    public void testParseConstant() {
        System.out.println("testParseConstant");
        
        Scanner s = numberS;
        s.nextToken();

        FactParser p = new FactParser(s, sym, c);

        assertEquals("Parse ", true, p.parse());
        assertEquals("Operand ", OperandKind.CONSTANT, p.getOperand().getKind());
        assertEquals("Value ", 42, p.getOperand().getValaddr());
    }
}
