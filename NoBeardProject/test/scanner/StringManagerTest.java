/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import org.junit.Ignore;
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
public class StringManagerTest {
    
    public StringManagerTest() {
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
     * Test of readString method, of class StringManager.
     */
    @Test
    public void testReadString() {
        System.out.println("readString");
        SrcReader sr = new SrcStringReader("'hello';'a longer string here';'world';");
        StringManager instance = new StringManager(sr);
        
        sr.nextChar();
        instance.readString();
        assertEquals("First string address", 0, instance.getStringAddress());
        assertEquals("First string length", 5, instance.getStringLength());
        assertEquals("Next char ", ';', sr.getCurrentChar());
        
        sr.nextChar();
        instance.readString();
        assertEquals("2nd string address", 5, instance.getStringAddress());
        assertEquals("2nd string length", 20, instance.getStringLength());
        assertEquals("Next char ", ';', sr.getCurrentChar());
        
        sr.nextChar();
        instance.readString();
        assertEquals("3rd string address", 25, instance.getStringAddress());
        assertEquals("2nd string length", 5, instance.getStringLength());
        assertEquals("Next char ", ';', sr.getCurrentChar());
    }

    /**
     * Test of getStringAddress method, of class StringManager.
     */
    @Test
    public void testInvalidString() {
        System.out.println("testInvalidString");
        SrcReader sr = new SrcStringReader("'a longer string not ending in this line");
        StringManager instance = new StringManager(sr);
        
        sr.nextChar();
        instance.readString();
        assertEquals("Address: ", 0, instance.getStringAddress());
        assertEquals("Length: ", 0, instance.getStringAddress());
    }
}
