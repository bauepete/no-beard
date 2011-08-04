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
    private Scanner modS;
    private Scanner noTermS;
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
        ErrorHandler.getInstance().reset();
        mulS = new Scanner(new SrcStringReader("a * b"));
        divS = new Scanner(new SrcStringReader("a / b"));
        modS = new Scanner(new SrcStringReader("a % b"));
        noTermS = new Scanner(new SrcStringReader("-b"));
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
    public void testParseMul() {
        System.out.println("testParseMul");
        Scanner s = mulS;

        s.nextToken();
        TermParser p = new TermParser(s, sym, c);
        assertEquals("Parse", true, p.parse());
    }
    
    @Test
    public void testParseDiv() {
        System.out.println("testParseDiv");
        Scanner s = divS;

        s.nextToken();
        TermParser p = new TermParser(s, sym, c);
        assertEquals("Parse", true, p.parse());
    }
    
    @Test
    public void testParseMod() {
        System.out.println("testParseMod");
        Scanner s = modS;

        s.nextToken();
        TermParser p = new TermParser(s, sym, c);
        assertEquals("Parse", true, p.parse());
    }
    
    @Test
    public void testParseNoTerm() {
        System.out.println("testParseNoTerm");
        Scanner s = noTermS;
        
        s.nextToken();
        TermParser p = new TermParser(s, sym, c);
        assertEquals("Parse", false, p.parse());
        assertEquals("Error", 1, ErrorHandler.getInstance().getCount("SynErr"));
    }
}
