/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class SrcReaderTest {
    SrcReader sr;
    SrcReader fr;
    
    String testString = "This is no source";

    public SrcReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        sr = new SrcStringReader(testString);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of nextChar method, of class SrcReader.
     */
    @Test
    public void testFirstChars() throws Exception {
        System.out.println("testNextChars");
   
        sr.nextChar();
        assertTrue("First char not ok", sr.getCurrentChar() == 'T');
        assertEquals(1, sr.getCurrentCol());
        assertEquals(1, sr.getCurrentLine());

        sr.nextChar();
        assertTrue("Second char not ok", sr.getCurrentChar() == 'h');
        assertEquals(2, sr.getCurrentCol());
        assertEquals(1, sr.getCurrentLine());
    }
    
    @Test
    public void testLastCharAndBeyond() throws Exception {
        System.out.println("testLastCharAndBeyond");
        
        for (int i = 0; i < testString.length(); i++) {
            sr.nextChar();
        }
        
        assertTrue("Last char not ok", sr.getCurrentChar() == 'e');
        assertEquals(testString.length(), sr.getCurrentCol());
        assertEquals(1, sr.getCurrentLine());
        
        sr.nextChar();
        assertTrue("Reading beyond last char not ok", sr.getCurrentChar() == -1);
        sr.nextChar();
        assertTrue("Reading beyond last char not ok", sr.getCurrentChar() == -1);
    }
}