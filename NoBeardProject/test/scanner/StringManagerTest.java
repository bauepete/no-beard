/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import error.ErrorHandler;
import error.Error;
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
        
        assertEquals("1st char ", 'h', instance.getCharAt(0));
        assertEquals("5th char ", 'o', instance.getCharAt(4));
        assertEquals("6th char ", 'a', instance.getCharAt(5));
        assertEquals("25th char ", 'e', instance.getCharAt(24));
        assertEquals("26th char ", 'w', instance.getCharAt(25));
        assertEquals("30th char ", 'd', instance.getCharAt(29));
    }

    /**
     * Test of getStringAddress method, of class StringManager.
     */
    @Test
    public void testInvalidString() {
        System.out.println("testInvalidString");
        SrcReader sr = new SrcStringReader("'a longer string not ending in this line");
        StringManager instance = new StringManager(sr);
        
        ErrorHandler.getInstance().reset();
        Error.setScanner(new Scanner(sr));

        instance.readString();
        assertEquals(0, ErrorHandler.getInstance().getLastError().getErrNo());
        assertEquals("Address: ", 0, instance.getStringAddress());
        assertEquals("Length: ", 0, instance.getStringAddress());
    }
    
    @Test
    public void testGetStringStorage() {
        System.out.println("getStringStorage");
        
        String src = "'hello';'a longer string here';'world';";
        SrcReader sr = new SrcStringReader(src);
        StringManager instance = new StringManager(sr);
        sr.nextChar();
        instance.readString();
        sr.nextChar();
        instance.readString();
        sr.nextChar();
        instance.readString();
        
        byte[] sStor = instance.getStringStorage();
        assertEquals("Byte 0", (byte)src.charAt(1), sStor[0]);
        assertEquals("Byte 4", (byte)src.charAt(5), sStor[4]);
        
        assertEquals("Byte 5", (byte)src.charAt(9), sStor[5]);
        assertEquals("Byte 24", (byte)src.charAt(28), sStor[24]);
        
        assertEquals("Byte 25", (byte)src.charAt(32), sStor[25]);
        assertEquals("Byte 29", (byte)src.charAt(36), sStor[29]);
    }
}
