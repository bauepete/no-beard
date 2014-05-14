/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import error.ErrorHandler;
import scanner.Scanner.Symbol;
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
public class ScannerTest {
    private SrcReader sourceReader;
    private ErrorHandler errorHandler;
    private Scanner scanner;

    public ScannerTest() {
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
     * Test of nextToken method, of class Scanner.
     */
    @Test
    public void testCommentLine() {
        setupReaderAndErrorHandler("# This is a comment line");
        scanner.nextToken();

        assertTrue("EOFSY expected", scanner.getCurrentToken().getSy() == Symbol.EOFSY);
    }
    
    private void setupReaderAndErrorHandler(String srcLine) {
        sourceReader = new SrcStringReader(srcLine);
        errorHandler = new ErrorHandler(sourceReader);
        scanner = new Scanner(sourceReader, errorHandler);
    }
    
    @Test
    public void testComment() {
        setupReaderAndErrorHandler("# This is a comment line \n if");
        scanner.nextToken();

        assertTrue("IFSY expected", scanner.getCurrentToken().getSy() == Symbol.IFSY);
    }

    @Test
    public void testSimpleSymbols() {
        System.out.println("testSimpleSymbols");
        
        setupReaderAndErrorHandler("();=+-*/%,");

        scanner.nextToken();
        assertTrue("LPARSY expected", scanner.getCurrentToken().getSy() == Symbol.LPARSY);

        scanner.nextToken();
        assertTrue("RPARSY expected", scanner.getCurrentToken().getSy() == Symbol.RPARSY);

        scanner.nextToken();
        assertTrue("SEMICOLON expected", scanner.getCurrentToken().getSy() == Symbol.SEMICOLONSY);

        scanner.nextToken();
        assertTrue("ASSIGNSY expected", scanner.getCurrentToken().getSy() == Symbol.ASSIGNSY);

        scanner.nextToken();
        assertTrue("PLUSSY expected", scanner.getCurrentToken().getSy() == Symbol.PLUSSY);

        scanner.nextToken();
        assertTrue("MINUSSY expected", scanner.getCurrentToken().getSy() == Symbol.MINUSSY);

        scanner.nextToken();
        assertTrue("TIMESSY expected", scanner.getCurrentToken().getSy() == Symbol.TIMESSY);

        scanner.nextToken();
        assertTrue("DIVSY expected", scanner.getCurrentToken().getSy() == Symbol.DIVSY);

        scanner.nextToken();
        assertTrue("MODSY expected", scanner.getCurrentToken().getSy() == Symbol.MODSY);

        scanner.nextToken();
        assertTrue("COMMASY expected", scanner.getCurrentToken().getSy() == Symbol.COMMASY);
    }

    @Test
    public void testNumber() {
        System.out.println("testNumber");
        
        setupReaderAndErrorHandler("42;");
        
        scanner.nextToken();
        assertTrue("NUMBERSY expected", scanner.getCurrentToken().getSy() == Symbol.NUMBERSY);
        assertEquals("Value", 42, scanner.getCurrentToken().getValue());
        assertEquals("Expected next char", ';', sourceReader.getCurrentChar());
    }
    
    @Test
    public void testName() {
        setupReaderAndErrorHandler("aVar; anotherVar;");
        
        scanner.nextToken();
        assertTrue("IDENTSY expected", scanner.getCurrentToken().getSy() == Symbol.IDENTSY);
        int spix1 = scanner.getCurrentToken().getValue();
        
        scanner.nextToken();
        assertTrue("SEMICOLONSY expected", scanner.getCurrentToken().getSy() == Symbol.SEMICOLONSY);
        
        scanner.nextToken();
        assertTrue("IDENTSY expected", scanner.getCurrentToken().getSy() == Symbol.IDENTSY);
        int spix2 = scanner.getCurrentToken().getValue();
        
        assertTrue("Expected spix1 != spix2", spix1 != spix2);
        assertEquals("Expected next char", ';', sourceReader.getCurrentChar());
    }
    
    @Test
    public void testSameName() {
        System.out.println("testSameName");
        
        setupReaderAndErrorHandler("aVar; aVar2; aVar");
        
        scanner.nextToken();
        assertTrue("IDENTSY expected", scanner.getCurrentToken().getSy() == Symbol.IDENTSY);
        int spix1 = scanner.getCurrentToken().getValue();
        
        scanner.nextToken();
        assertTrue("SEMICOLONSY expected", scanner.getCurrentToken().getSy() == Symbol.SEMICOLONSY);
        
        scanner.nextToken();
        assertTrue("IDENTSY expected", scanner.getCurrentToken().getSy() == Symbol.IDENTSY);
        int spix2 = scanner.getCurrentToken().getValue();

        scanner.nextToken();
         assertTrue("SEMICOLONSY expected", scanner.getCurrentToken().getSy() == Symbol.SEMICOLONSY);
        
        scanner.nextToken();
        assertTrue("IDENTSY expected", scanner.getCurrentToken().getSy() == Symbol.IDENTSY);
        int spix3 = scanner.getCurrentToken().getValue();
        
        assertEquals("Expected spix1 = spix2", spix1, spix3);

    }
    
    @Test
    public void testString() {
        System.out.println("testString");
        
        setupReaderAndErrorHandler("'a string' \"another string\"");
        scanner.nextToken();
        assertEquals("Sy ", Symbol.STRINGSY, scanner.getCurrentToken().getSy());
        assertEquals("Start addr ", 0, scanner.getStringAddress());
        assertEquals("Length ", 8, scanner.getStringLength());
        
        scanner.nextToken();
        assertEquals("Sy ", Symbol.STRINGSY, scanner.getCurrentToken().getSy());
        assertEquals("Start addr ", 8, scanner.getStringAddress());
        assertEquals("Length ", 14, scanner.getStringLength());
    }
    
    @Test
    public void testKeywords() {
        System.out.println("testKeywords");
        
        setupReaderAndErrorHandler("unit foo; do put x; a < 0; a != 0; true; false; putln; done done foo;");
        
        Symbol[] expectedSymbols = {
            Symbol.UNITSY, Symbol.IDENTSY, Symbol.SEMICOLONSY, Symbol.DOSY, Symbol.PUTSY,
            Symbol.IDENTSY, Symbol.SEMICOLONSY, Symbol.IDENTSY, Symbol.LTHSY, Symbol.NUMBERSY,
            Symbol.SEMICOLONSY, Symbol.IDENTSY, Symbol.NEQSY, Symbol.NUMBERSY, Symbol.SEMICOLONSY,
            Symbol.TRUESY, Symbol.SEMICOLONSY, Symbol.FALSESY, Symbol.SEMICOLONSY,
            Symbol.PUTLNSY, Symbol.SEMICOLONSY, Symbol.DONESY,
            Symbol.DONESY, Symbol.IDENTSY, Symbol.SEMICOLONSY
        };
        
        for (Symbol sy : expectedSymbols) {
            scanner.nextToken();
            assertEquals(sy, scanner.getCurrentToken().getSy());
        }
        
        scanner.nextToken();
        assertTrue("EOFSY expected", scanner.getCurrentToken().getSy() == Symbol.EOFSY);
        scanner.nextToken();
        assertTrue("EOFSY expected", scanner.getCurrentToken().getSy() == Symbol.EOFSY);
    }
}
