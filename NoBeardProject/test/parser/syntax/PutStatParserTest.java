/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.PutParser;
import parser.general.PutParserTestSetup;
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
        PutParser p = PutParserTestSetup.getPutIntSetup();
        assertTrue(p.parse());
        assertEquals(Symbol.EOFSY, PutParserTestSetup.getScanner().getCurrentToken().getSymbol());
    }

    /**
     * Test output of a char constant.
     */
    @Test
    public void testParsePutChar() {        
        PutParser p = PutParserTestSetup.getPutCharSetup();
        assertTrue(p.parse());
        assertEquals(Symbol.EOFSY, PutParserTestSetup.getScanner().getCurrentToken().getSymbol());
    }
    
    /**
     * Test output of a string constant.
     */
    @Test
    public void testParsePutString() {
        PutParser p = PutParserTestSetup.getPutStringSetup();
        assertTrue(p.parse());
        assertEquals(Symbol.EOFSY, PutParserTestSetup.getScanner().getCurrentToken().getSymbol());
    }
    
    /**
     * Test output where a column width spec is given as second parameter.
     */
    @Test
    public void testPutWithColumnWidthSpecification() {
        PutParser p = PutParserTestSetup.getPutWithWidthSpecification();
        assertTrue(p.parse());
    }
    
    /**
     * Test of putln.
     */
    @Test
    public void testParsePutln() {
        PutParser p = PutParserTestSetup.getPutlnSetup();
        assertTrue(p.parse());
        assertEquals(Symbol.EOFSY, PutParserTestSetup.getScanner().getCurrentToken().getSymbol());
    }
}
