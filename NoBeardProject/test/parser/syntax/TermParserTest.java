/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import error.ErrorHandler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.TermParser;
import parser.general.TermParserTestSetup;

/**
 *
 * @author peter
 */
public class TermParserTest {

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
        TermParser p = TermParserTestSetup.getMulTermSetup();
        assertEquals("Parse", true, p.parse());
    }
    
    @Test
    public void testParseDiv() {
        System.out.println("testParseDiv");
        TermParser p = TermParserTestSetup.getDivTermSetup();
        assertEquals("Parse", true, p.parse());
    }
    
    @Test
    public void testParseMod() {
        System.out.println("testParseMod");
        TermParser p = TermParserTestSetup.getModTermSetup();
        assertEquals("Parse", true, p.parse());
    }
    
  
    @Test
    public void testParseAnd() {
        System.out.println("testParseAndTerm");
        TermParser p = TermParserTestSetup.getAndTermSetup();
        
        assertTrue("Parse ", p.parse());
    }
    
    @Test
    public void testParseNoTerm() {
        System.out.println("testParseNoTerm");
        TermParser p = TermParserTestSetup.getNoTermSetup();
        assertEquals("Parse", false, p.parse());
        assertEquals("Error", 1, ErrorHandler.getInstance().getCount("SynErr"));
    }
}
