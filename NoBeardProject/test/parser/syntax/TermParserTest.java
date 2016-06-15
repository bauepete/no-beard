/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import error.Error.ErrorType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import parser.TermParser;
import parser.general.TermParserTestSetup;

/**
 *
 * @author peter
 */
@Ignore
public class TermParserTest {

    public TermParserTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parseOldStyle method, of class TermParser.
     */
    @Test
    public void testParseMul() {
        System.out.println("testParseMul");
        TermParser p = TermParserTestSetup.getMulTermSetup();
        assertEquals("Parse", true, p.parseOldStyle());
    }
    
    @Test
    public void testParseDiv() {
        System.out.println("testParseDiv");
        TermParser p = TermParserTestSetup.getDivTermSetup();
        assertEquals("Parse", true, p.parseOldStyle());
    }
    
    @Test
    public void testParseMod() {
        System.out.println("testParseMod");
        TermParser p = TermParserTestSetup.getModTermSetup();
        assertEquals("Parse", true, p.parseOldStyle());
    }
    
  
    @Test
    public void testParseAnd() {
        System.out.println("testParseAndTerm");
        TermParser p = TermParserTestSetup.getAndTermSetup();
        
        assertTrue("Parse ", p.parseOldStyle());
    }
    
    @Test
    public void testParseNoTerm() {
        System.out.println("testParseNoTerm");
        TermParser p = TermParserTestSetup.getNoTermSetup();
        assertEquals("Parse", false, p.parseOldStyle());
        assertEquals("Last error", ErrorType.SYMBOL_EXPECTED.getNumber(), p.getErrorHandler().getLastError().getNumber());
    }
}
