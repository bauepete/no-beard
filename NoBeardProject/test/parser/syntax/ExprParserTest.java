/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import error.ErrorHandler;
import nbm.Code;
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
    private Scanner subS;
    private Scanner negAddS;
    private Scanner negS;
    private Scanner complexExprS;
    private Scanner noExprS;
    
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
        subS = new Scanner(new SrcStringReader("a - b"));
        negAddS = new Scanner(new SrcStringReader("-a + b"));
        negS = new Scanner(new SrcStringReader("-b"));
        complexExprS = new Scanner(new SrcStringReader("-5 * (a + b)/17"));
        noExprS = new Scanner(new SrcStringReader("*b"));

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
    public void testAdd() {
        System.out.println("testAdd");

        Scanner s = addS;
        s.nextToken();
        ExprParser p = new ExprParser(s, sym, c);

        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testSub() {
        System.out.println("testSub");

        Scanner s = negS;
        s.nextToken();
        ExprParser p = new ExprParser(s, sym, c);

        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testNegAdd() {
        System.out.println("testNegAdd");

        Scanner s = negAddS;
        s.nextToken();
        ExprParser p = new ExprParser(s, sym, c);

        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testNeg() {
        System.out.println("testNeg");

        Scanner s = negS;
        s.nextToken();
        ExprParser p = new ExprParser(s, sym, c);

        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testComplexExpr() {
        System.out.println("testComplexExpr");

        Scanner s = complexExprS;
        s.nextToken();
        ExprParser p = new ExprParser(s, sym, c);

        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testNoExpr() {
        System.out.println("testNoExpr");

        Scanner s = noExprS;
        s.nextToken();
        ExprParser p = new ExprParser(s, sym, c);

        assertEquals("Parse ", false, p.parse());
    }
}
