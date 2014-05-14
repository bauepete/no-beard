/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import error.Error.ErrorType;
import parser.FactParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import parser.general.FactParserTestSetup;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class FactParserTest {

    public FactParserTest() {
    }
    
    @Before
    public void setUp() {
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
        FactParser p = FactParserTestSetup.getIdentifierTestSetup();
        assertEquals("Parse ", true, p.parse());
    }
    
    @Test
    public void testParseNumber() {
        System.out.println("testParseNumber");
        FactParser p = FactParserTestSetup.getNumberTestSetup();
        assertEquals("Parse ", true, p.parse());
    }
    
    @Test
    public void testParseString() {
        System.out.append("testParseString");
        FactParser p = FactParserTestSetup.getStringTestSetup();
        assertEquals("Parse ", true, p.parse());
    }
    
    @Test
    public void testParseExpr() {
        System.out.println("testParseExpr");
        FactParser p = FactParserTestSetup.getExprSetup();
        assertEquals("Parse ", true, p.parse());
    }
    
    @Test
    public void testParseNoFact() {
        System.out.println("testParseNoFact");
        FactParser p = FactParserTestSetup.getNoFactSetup();
        
        assertEquals("Parse ", false, p.parse());
        assertEquals("Last error", ErrorType.SYMBOL_EXPECTED.getNumber(), p.getErrorHandler().getLastError().getNumber());
    }
}
