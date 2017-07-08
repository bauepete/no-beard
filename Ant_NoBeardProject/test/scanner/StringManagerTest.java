/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import io.SourceStringReader;
import io.SourceReader;
import error.ErrorHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class StringManagerTest {

    SourceReader sourceReader;
    ErrorHandler errorHandler;
    StringManager stringManager;

    public StringManagerTest() {
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
        setupComponents("'hello';'a longer string here';'world';");

        sourceReader.nextChar();
        stringManager.readString();
        assertEquals("First string address", 0, stringManager.getStringAddress());
        assertEquals("First string length", 5, stringManager.getStringLength());
        assertEquals("Next char ", ';', sourceReader.getCurrentChar());

        sourceReader.nextChar();
        stringManager.readString();
        assertEquals("2nd string address", 5, stringManager.getStringAddress());
        assertEquals("2nd string length", 20, stringManager.getStringLength());
        assertEquals("Next char ", ';', sourceReader.getCurrentChar());

        sourceReader.nextChar();
        stringManager.readString();
        assertEquals("3rd string address", 25, stringManager.getStringAddress());
        assertEquals("2nd string length", 5, stringManager.getStringLength());
        assertEquals("Next char ", ';', sourceReader.getCurrentChar());

        assertEquals("1st char ", 'h', stringManager.getCharAt(0));
        assertEquals("5th char ", 'o', stringManager.getCharAt(4));
        assertEquals("6th char ", 'a', stringManager.getCharAt(5));
        assertEquals("25th char ", 'e', stringManager.getCharAt(24));
        assertEquals("26th char ", 'w', stringManager.getCharAt(25));
        assertEquals("30th char ", 'd', stringManager.getCharAt(29));
    }

    private void setupComponents(String srcString) {
        sourceReader = new SourceStringReader(srcString);
        errorHandler = new ErrorHandler(sourceReader);
        stringManager = new StringManager(sourceReader, errorHandler);
    }

    /**
     * Test of getStringAddress method, of class StringManager.
     */
    @Test
    public void testInvalidString() {
        System.out.println("testInvalidString");
        setupComponents("'a longer string not ending in this line");
        sourceReader.nextChar();

        stringManager.readString();
        assertEquals(2, errorHandler.getLastError().getNumber());
        assertEquals("Address: ", 0, stringManager.getStringAddress());
        assertEquals("Length: ", 0, stringManager.getStringLength());
    }

    @Test
    public void testGetStringStorage() {
        System.out.println("getStringStorage");

        String src = "'hello';'a longer string here';'world';";
        setupComponents(src);
        sourceReader.nextChar();
        stringManager.readString();
        sourceReader.nextChar();
        stringManager.readString();
        sourceReader.nextChar();
        stringManager.readString();

        byte[] sStor = stringManager.getStringStorage();
        assertEquals("Byte 0", (byte) src.charAt(1), sStor[0]);
        assertEquals("Byte 4", (byte) src.charAt(5), sStor[4]);

        assertEquals("Byte 5", (byte) src.charAt(9), sStor[5]);
        assertEquals("Byte 24", (byte) src.charAt(28), sStor[24]);

        assertEquals("Byte 25", (byte) src.charAt(32), sStor[25]);
        assertEquals("Byte 29", (byte) src.charAt(36), sStor[29]);
    }
}
