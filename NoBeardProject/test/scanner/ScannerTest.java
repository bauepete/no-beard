/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import org.junit.Ignore;
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
        System.out.println("testCommentLine");
        
        Scanner s = new Scanner(new SrcStringReader("# This is a comment line"));

        s.nextToken();

        assertTrue("EOFSY expected", s.getCurrentToken().getSy() == Symbol.EOFSY);
    }

    @Test
    public void testSimpleSymbols() {
        System.out.println("testSimpleSymbols");
        
        Scanner s = new Scanner(new SrcStringReader("();=+-*/%,"));

        s.nextToken();
        assertTrue("LPARSY expected", s.getCurrentToken().getSy() == Symbol.LPARSY);

        s.nextToken();
        assertTrue("RPARSY expected", s.getCurrentToken().getSy() == Symbol.RPARSY);

        s.nextToken();
        assertTrue("SEMICOLON expected", s.getCurrentToken().getSy() == Symbol.SEMICOLONSY);

        s.nextToken();
        assertTrue("ASSIGNSY expected", s.getCurrentToken().getSy() == Symbol.ASSIGNSY);

        s.nextToken();
        assertTrue("PLUSSY expected", s.getCurrentToken().getSy() == Symbol.PLUSSY);

        s.nextToken();
        assertTrue("MINUSSY expected", s.getCurrentToken().getSy() == Symbol.MINUSSY);

        s.nextToken();
        assertTrue("DIVSY expected", s.getCurrentToken().getSy() == Symbol.TIMESSY);

        s.nextToken();
        assertTrue("TIMESSY expected", s.getCurrentToken().getSy() == Symbol.DIVSY);

        s.nextToken();
        assertTrue("MODSY expected", s.getCurrentToken().getSy() == Symbol.MODSY);

        s.nextToken();
        assertTrue("COLONSY expected", s.getCurrentToken().getSy() == Symbol.COLONSY);
    }

    @Test
    public void testNumber() {
        System.out.println("testNumber");
        
        SrcReader sr = new SrcStringReader("42;");
        Scanner s = new Scanner(sr);
        
        s.nextToken();
        assertTrue("NUMBERSY expected", s.getCurrentToken().getSy() == Symbol.NUMBERSY);
        assertEquals("Value", 42, s.getCurrentToken().getValue());
        assertEquals("Expected next char", ';', sr.getCurrentChar());
    }
    
    @Test
    public void testName() {
        System.out.println("testName");
        
        SrcReader sr = new SrcStringReader("aVar; anotherVar;"); 
        Scanner s = new Scanner(sr);
        
        s.nextToken();
        assertTrue("IDENTSY expected", s.getCurrentToken().getSy() == Symbol.IDENTSY);
        int spix1 = s.getCurrentToken().getValue();
        
        s.nextToken();
        assertTrue("SEMICOLONSY expected", s.getCurrentToken().getSy() == Symbol.SEMICOLONSY);
        
        s.nextToken();
        assertTrue("IDENTSY expected", s.getCurrentToken().getSy() == Symbol.IDENTSY);
        int spix2 = s.getCurrentToken().getValue();
        
        assertTrue("Expected spix1 != spix2", spix1 != spix2);
        assertEquals("Expected next char", ';', sr.getCurrentChar());
    }
    
    @Test
    public void testSameName() {
        System.out.println("testSameName");
        
        Scanner s = new Scanner(new SrcStringReader("aVar; aVar2; aVar"));
        
        s.nextToken();
        assertTrue("IDENTSY expected", s.getCurrentToken().getSy() == Symbol.IDENTSY);
        int spix1 = s.getCurrentToken().getValue();
        
        s.nextToken();
        assertTrue("SEMICOLONSY expected", s.getCurrentToken().getSy() == Symbol.SEMICOLONSY);
        
        s.nextToken();
        assertTrue("IDENTSY expected", s.getCurrentToken().getSy() == Symbol.IDENTSY);
        int spix2 = s.getCurrentToken().getValue();

        s.nextToken();
         assertTrue("SEMICOLONSY expected", s.getCurrentToken().getSy() == Symbol.SEMICOLONSY);
        
        s.nextToken();
        assertTrue("IDENTSY expected", s.getCurrentToken().getSy() == Symbol.IDENTSY);
        int spix3 = s.getCurrentToken().getValue();
        
        assertEquals("Expected spix1 = spix2", spix1, spix3);

    }
    
    @Test
    public void testString() {
        System.out.println("testString");
        
        Scanner s = new Scanner(new SrcStringReader("'a string' \"another string\""));
        s.nextToken();
        assertEquals("Sy ", Symbol.STRINGSY, s.getCurrentToken().getSy());
        assertEquals("Start addr ", 0, s.getStringAddress());
        assertEquals("Length ", 8, s.getStringLength());
        
        s.nextToken();
        assertEquals("Sy ", Symbol.STRINGSY, s.getCurrentToken().getSy());
        assertEquals("Start addr ", 8, s.getStringAddress());
        assertEquals("Length ", 14, s.getStringLength());
    }
    
    @Test
    public void testKeywords() {
        System.out.println("testKeywords");
        
        Scanner s = new Scanner(new SrcStringReader("unit foo; do put x; done done foo;"));
        
        s.nextToken();
        assertTrue("UNITSY expected", s.getCurrentToken().getSy() == Symbol.UNITSY);
        s.nextToken();
        assertTrue("IDENTSY expected", s.getCurrentToken().getSy() == Symbol.IDENTSY);
        s.nextToken();
        assertTrue("LPARSY expected", s.getCurrentToken().getSy() == Symbol.SEMICOLONSY);
        s.nextToken();
        assertTrue("DOSY expected", s.getCurrentToken().getSy() == Symbol.DOSY);
        s.nextToken();
        assertTrue("PUTSY expected", s.getCurrentToken().getSy() == Symbol.PUTSY);
        s.nextToken();
        assertTrue("IDENTSY expected", s.getCurrentToken().getSy() == Symbol.IDENTSY);
        s.nextToken();
        assertTrue("SEMICOLONSY expected", s.getCurrentToken().getSy() == Symbol.SEMICOLONSY);
        s.nextToken();
        assertTrue("DONESY expected", s.getCurrentToken().getSy() == Symbol.DONESY);
        s.nextToken();
        assertTrue("RPARSY expected", s.getCurrentToken().getSy() == Symbol.DONESY);
        s.nextToken();
        assertTrue("RPARSY expected", s.getCurrentToken().getSy() == Symbol.IDENTSY);
        s.nextToken();
        assertTrue("RPARSY expected", s.getCurrentToken().getSy() == Symbol.SEMICOLONSY);
        s.nextToken();
        assertTrue("EOFSY expected", s.getCurrentToken().getSy() == Symbol.EOFSY);
        s.nextToken();
        assertTrue("EOFSY expected", s.getCurrentToken().getSy() == Symbol.EOFSY);
    }
    
    @Ignore
    @Test
    public void testNumberOverflow() {
        System.out.println("testNumberOverflow");
        // TODO Implement testNumberOverflow
    }
}
