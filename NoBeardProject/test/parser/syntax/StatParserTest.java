/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import nbm.Code;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.StatParser;
import scanner.Scanner;
import scanner.SrcStringReader;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class StatParserTest {
    private Scanner assignS;
    private Scanner noAssignS;
    private Scanner simplePutS;
    private Scanner putS;
    private Scanner noPutS;
    private SymListManager sym;
    private Code c;
    
    public StatParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        assignS = new Scanner(new SrcStringReader("a = 5;"));
        noAssignS = new Scanner(new SrcStringReader("a == 5;"));
        simplePutS = new Scanner(new SrcStringReader("put(5);"));
        putS = new Scanner(new SrcStringReader("put(5, 3);"));
        noPutS = new Scanner(new SrcStringReader("put(5, 3, 8);"));
        c = new Code();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of parse method, of class StatParser.
     */
    @Test
    public void testParseAssignStat() {
        System.out.println("testParseAssignStat");
        Scanner s = assignS;
        sym = new SymListManager(c, s.getNameManager());
        sym.newUnit(1);
        sym.newVar(0, SymListManager.ElementType.INT);
        Operand.setSymListManager(sym);
        s.nextToken();
        
        StatParser p = new StatParser(s, sym, c);
        assertEquals("Parse ", true, p.parse());
        
    }

    /**
     * Test of parse method, of class StatParser.
     */
    @Test
    public void testParsePutStat() {
        System.out.println("testParsePutStat");
        Scanner s = putS;
        s.nextToken();
        
        StatParser p = new StatParser(s, sym, c);
        assertEquals("Parse ", true, p.parse());
        
    }

    /**
     * Test of parse method, of class StatParser.
     */
    @Test
    public void testParseSimplePutStat() {
        System.out.println("testParseSimplePutStat");
        Scanner s = simplePutS;
        s.nextToken();
        
        StatParser p = new StatParser(s, sym, c);
        assertEquals("Parse ", true, p.parse());
        
    }
    /**
     * Test of parse method, of class StatParser.
     */
    @Test
    public void testParseNoAssignStat() {
        System.out.println("testParseNoAssignStat");
        Scanner s = noAssignS;
        sym = new SymListManager(c, s.getNameManager());
        sym.newUnit(1);
        sym.newVar(0, SymListManager.ElementType.INT);
        s.nextToken();
        
        StatParser p = new StatParser(s, sym, c);
        assertEquals("Parse ", false, p.parse());
        
    }
    /**
     * Test of parse method, of class StatParser.
     */
    @Test
    public void testParseNoPutStat() {
        System.out.println("testParseNoPutStat");
        Scanner s = noPutS;
        s.nextToken();
        
        StatParser p = new StatParser(s, sym, c);
        assertEquals("Parse ", false, p.parse());
        
    }
}
