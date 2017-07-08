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
import parser.TermParser;
import parser.general.ParserTestSetup;
import parser.general.TermParserTestSetup;
import scanner.Scanner.Symbol;

/**
 *
 * @author peter
 */
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
     * Test parsing a multiplication.
     */
    @Test
    public void testParseMul() {
        System.out.println("testParseMul");
        TermParser p = TermParserTestSetup.getMulTermSetup();
        assertEquals("Parse", true, p.parse());
        assertEquals(Symbol.EOFSY, ParserTestSetup.getScanner().getCurrentToken().getSymbol());
    }
    
    @Test
    public void testParseNoTerm() {
        System.out.println("testParseNoTerm");
        TermParser p = TermParserTestSetup.getNoTermSetup();
        assertEquals("Parse", false, p.parse());
        assertEquals("Last error", ErrorType.SYMBOL_EXPECTED.getNumber(), p.getErrorHandler().getLastError().getNumber());
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
}
