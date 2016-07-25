/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.PutStatParser;
import parser.general.PutStatParserTestSetup;
import scanner.Scanner.Symbol;

/**
 *
 * @author peter
 */
public class PutStatParserTest {
    
    public PutStatParserTest() {
    }

    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test output of an integer constant.
     */
    @Test
    public void testParsePutInt() {
        PutStatParser p = PutStatParserTestSetup.getPutIntSetup();
        assertTrue(p.parse());
        assertEquals(Symbol.EOFSY, PutStatParserTestSetup.getScanner().getCurrentToken().getSymbol());
    }

    /**
     * Test output of a char constant.
     */
    @Test
    public void testParsePutChar() {        
        PutStatParser p = PutStatParserTestSetup.getPutCharSetup();
        assertTrue(p.parse());
        assertEquals(Symbol.EOFSY, PutStatParserTestSetup.getScanner().getCurrentToken().getSymbol());
    }
    
    /**
     * Test output of a string constant.
     */
    @Test
    public void testParsePutString() {
        PutStatParser p = PutStatParserTestSetup.getPutStringSetup();
        assertTrue(p.parse());
        assertEquals(Symbol.EOFSY, PutStatParserTestSetup.getScanner().getCurrentToken().getSymbol());
    }
    
    
    /**
     * Test of putln.
     */
    @Test
    public void testParsePutln() {
        PutStatParser p = PutStatParserTestSetup.getPutlnSetup();
        assertTrue(p.parse());
        assertEquals(Symbol.EOFSY, PutStatParserTestSetup.getScanner().getCurrentToken().getSymbol());
    }
}
