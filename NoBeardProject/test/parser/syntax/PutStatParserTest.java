/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.PutStatParser;
import parser.general.PutStatParserTestSetup;

/**
 *
 * @author peter
 */
public class PutStatParserTest {
    
    public PutStatParserTest() {
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
     * Test of parse method, of class PutStatParser.
     */
    @Test
    public void testParsePutInt() {
        System.out.println("testParsePutInt");
        
        PutStatParser p = PutStatParserTestSetup.getPutIntSetup();
        assertTrue(p.parse());
    }
    
    @Test
    public void testParsePutString() {
        System.out.println("testParsePutString");
        
        PutStatParser p = PutStatParserTestSetup.getPutStringSetup();
        assertTrue(p.parse());
    }
    
    
    /**
     * Test of parse method, of class PutStatParser.
     */
    @Test
    public void testParsePutln() {
        System.out.println("testParsePutln");
        PutStatParser p = PutStatParserTestSetup.getPutlnSetup();
        assertTrue(p.parse());
    }
}
