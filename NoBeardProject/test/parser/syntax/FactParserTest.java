/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import error.ErrorHandler;
import symlist.SymListManager;
import nbm.Code;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import parser.FactParser;
import parser.Parser;
import scanner.Scanner;
import scanner.SrcStringReader;
import symlist.Operand;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class FactParserTest {
    Code c;
    SymListManager sym;
    Scanner identS;
    Scanner numberS;
    Scanner exprS;
    Scanner noFactS;
    
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
        ErrorHandler.getInstance().reset();
        identS = new Scanner(new SrcStringReader("a"));
        numberS = new Scanner(new SrcStringReader("2567"));
        exprS = new Scanner(new SrcStringReader("(a + b)"));
        noFactS = new Scanner(new SrcStringReader("{"));
        c = new Code();
        sym = new SymListManager(c, exprS.getNameManager());
        sym.newUnit(25);
        sym.newVar(0, SymListManager.ElementType.INT);
        sym.newVar(1, SymListManager.ElementType.INT);
        Operand.setSymListManager(sym);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of parse method, of class FactParser.
     */
    @Test
    public void testParseIdent() {
        System.out.println("testParseIdent");
        Scanner s = identS;
        
        s.nextToken();
        Parser p = new FactParser(s, sym, c);        
        assertEquals("Parse ", true, p.parse());
    }
    
    @Test
    public void testParseNumber() {
        System.out.println("testParseNumber");
        Scanner s = numberS;
        
        s.nextToken();
        Parser p = new FactParser(s, sym, c);
        assertEquals("Parse ", true, p.parse());
    }
    
    @Test
    public void testParseExpr() {
        System.out.println("testParseExpr");
        Scanner s = exprS;
        
        s.nextToken();
        Parser p = new FactParser(s, sym, c);
        assertEquals("Parse ", true, p.parse());
    }
    
    @Test
    public void testParseNoFact() {
        System.out.println("testParseNoFact");
        Scanner s = noFactS;
        s.nextToken();
        Parser p = new FactParser(s, sym, c);
        
        assertEquals("Parse ", false, p.parse());
        assertEquals("Error", 1, ErrorHandler.getInstance().getCount("SynErr"));
    }
}
