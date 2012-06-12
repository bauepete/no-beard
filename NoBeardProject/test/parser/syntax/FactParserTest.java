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
import parser.Parser;
import parser.general.FactParserTestSetup;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class FactParserTest {

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
        Parser p = FactParserTestSetup.getIdentifierTestSetup();
        assertEquals("Parse ", true, p.parse());
    }
    
    @Test
    public void testParseNumber() {
        System.out.println("testParseNumber");
        Parser p = FactParserTestSetup.getNumberTestSetup();
        assertEquals("Parse ", true, p.parse());
    }
    
    @Test
    public void testParseString() {
        System.out.append("testParseString");
        Parser p = FactParserTestSetup.getStringTestSetup();
        assertEquals("Parse ", true, p.parse());
    }
    
    @Test
    public void testParseExpr() {
        System.out.println("testParseExpr");
        Parser p = FactParserTestSetup.getExprSetup();
        assertEquals("Parse ", true, p.parse());
    }
    
    @Test
    public void testParseNoFact() {
        System.out.println("testParseNoFact");
        Parser p = FactParserTestSetup.getNoFactSetup();
        
        assertEquals("Parse ", false, p.parse());
        assertEquals("Error", 1, ErrorHandler.getInstance().getCount("SynErr"));
    }
}
