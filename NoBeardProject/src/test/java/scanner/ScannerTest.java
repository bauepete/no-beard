/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import io.SourceStringReader;
import io.SourceReader;
import error.ErrorHandler;
import scanner.Scanner.Symbol;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class ScannerTest {
    private SourceReader sourceReader;
    private ErrorHandler errorHandler;
    private Scanner scanner;

    public ScannerTest() {
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

        assertTrue("EOFSY expected", scanner.getCurrentToken().getSymbol() == Symbol.EOFSY);
    }
    
    private void setupReaderAndErrorHandler(String srcLine) {
        sourceReader = new SourceStringReader(srcLine);
        errorHandler = new ErrorHandler(sourceReader);
        scanner = new Scanner(sourceReader, errorHandler, new NameManagerForCompiler(sourceReader));
    }
    
    @Test
    public void testComment() {
        setupReaderAndErrorHandler("# This is a comment line \n if");
        scanner.nextToken();

        assertTrue("IFSY expected", scanner.getCurrentToken().getSymbol() == Symbol.IF);
    }

    @Test
    public void testSimpleSymbols() {
        System.out.println("testSimpleSymbols");
        
        setupReaderAndErrorHandler("();=+-*/%,");

        scanner.nextToken();
        assertTrue("LPARSY expected", scanner.getCurrentToken().getSymbol() == Symbol.LPAR);

        scanner.nextToken();
        assertTrue("RPARSY expected", scanner.getCurrentToken().getSymbol() == Symbol.RPAR);

        scanner.nextToken();
        assertTrue("SEMICOLON expected", scanner.getCurrentToken().getSymbol() == Symbol.SEMICOLON);

        scanner.nextToken();
        assertTrue("ASSIGNSY expected", scanner.getCurrentToken().getSymbol() == Symbol.ASSIGN);

        scanner.nextToken();
        assertTrue("PLUSSY expected", scanner.getCurrentToken().getSymbol() == Symbol.PLUS);

        scanner.nextToken();
        assertTrue("MINUSSY expected", scanner.getCurrentToken().getSymbol() == Symbol.MINUS);

        scanner.nextToken();
        assertTrue("TIMESSY expected", scanner.getCurrentToken().getSymbol() == Symbol.TIMES);

        scanner.nextToken();
        assertTrue("DIVSY expected", scanner.getCurrentToken().getSymbol() == Symbol.DIV);

        scanner.nextToken();
        assertTrue("MODSY expected", scanner.getCurrentToken().getSymbol() == Symbol.MOD);

        scanner.nextToken();
        assertTrue("COMMASY expected", scanner.getCurrentToken().getSymbol() == Symbol.COMMA);
    }
    
    @Test
    public void testGetCurrentTokenKeepsUnchanged() {
        setupReaderAndErrorHandler("(;");
        scanner.nextToken();
        Token firstToken = scanner.getCurrentToken();
        scanner.nextToken();
        Token secondToken = scanner.getCurrentToken();
        assertTrue(firstToken != secondToken);
    }

    @Test
    public void testNumber() {
        System.out.println("testNumber");
        
        setupReaderAndErrorHandler("42;");
        
        scanner.nextToken();
        assertTrue("NUMBERSY expected", scanner.getCurrentToken().getSymbol() == Symbol.NUMBER);
        assertEquals("Value", 42, scanner.getCurrentToken().getValue());
        assertEquals("Expected next char", ';', sourceReader.getCurrentChar());
    }
    
    @Test
    public void testName() {
        setupReaderAndErrorHandler("aVar; anotherVar;");
        
        scanner.nextToken();
        assertTrue("IDENTSY expected", scanner.getCurrentToken().getSymbol() == Symbol.IDENTIFIER);
        int spix1 = scanner.getCurrentToken().getValue();
        
        scanner.nextToken();
        assertTrue("SEMICOLONSY expected", scanner.getCurrentToken().getSymbol() == Symbol.SEMICOLON);
        
        scanner.nextToken();
        assertTrue("IDENTSY expected", scanner.getCurrentToken().getSymbol() == Symbol.IDENTIFIER);
        int spix2 = scanner.getCurrentToken().getValue();
        
        assertTrue("Expected spix1 != spix2", spix1 != spix2);
        assertEquals("Expected next char", ';', sourceReader.getCurrentChar());
    }
    
    @Test
    public void testSameName() {
        System.out.println("testSameName");
        
        setupReaderAndErrorHandler("aVar; aVar2; aVar");
        
        scanner.nextToken();
        assertTrue("IDENTSY expected", scanner.getCurrentToken().getSymbol() == Symbol.IDENTIFIER);
        int spix1 = scanner.getCurrentToken().getValue();
        
        scanner.nextToken();
        assertTrue("SEMICOLONSY expected", scanner.getCurrentToken().getSymbol() == Symbol.SEMICOLON);
        
        scanner.nextToken();
        assertTrue("IDENTSY expected", scanner.getCurrentToken().getSymbol() == Symbol.IDENTIFIER);
        int spix2 = scanner.getCurrentToken().getValue();

        scanner.nextToken();
         assertTrue("SEMICOLONSY expected", scanner.getCurrentToken().getSymbol() == Symbol.SEMICOLON);
        
        scanner.nextToken();
        assertTrue("IDENTSY expected", scanner.getCurrentToken().getSymbol() == Symbol.IDENTIFIER);
        int spix3 = scanner.getCurrentToken().getValue();
        
        assertEquals("Expected spix1 = spix2", spix1, spix3);

    }
    
    @Test
    public void testString() {
        System.out.println("testString");
        
        setupReaderAndErrorHandler("'a string' \"another string\"");
        scanner.nextToken();
        StringToken st = (StringToken)scanner.getCurrentToken();
        assertEquals("Sy ", Symbol.STRING, st.getSymbol());
        assertEquals("Start addr ", 0, st.getAddress());
        assertEquals("Length ", 8, st.getLength());
        
        scanner.nextToken();
        st = (StringToken)scanner.getCurrentToken();
        assertEquals("Sy ", Symbol.STRING, st.getSymbol());
        assertEquals("Start addr ", 8, st.getAddress());
        assertEquals("Length ", 14, st.getLength());
    }
    
    @Test
    public void testKeywords() {
        System.out.println("testKeywords");
        
        setupReaderAndErrorHandler("unit foo; do put x; a < 0; a != 0; true; false; putln; done done foo;");
        
        Symbol[] expectedSymbols = {
            Symbol.UNIT, Symbol.IDENTIFIER, Symbol.SEMICOLON, Symbol.DO, Symbol.PUT,
            Symbol.IDENTIFIER, Symbol.SEMICOLON, Symbol.IDENTIFIER, Symbol.LTH, Symbol.NUMBER,
            Symbol.SEMICOLON, Symbol.IDENTIFIER, Symbol.NEQ, Symbol.NUMBER, Symbol.SEMICOLON,
            Symbol.TRUE, Symbol.SEMICOLON, Symbol.FALSE, Symbol.SEMICOLON,
            Symbol.PUTLN, Symbol.SEMICOLON, Symbol.DONE,
            Symbol.DONE, Symbol.IDENTIFIER, Symbol.SEMICOLON
        };
        
        for (Symbol sy : expectedSymbols) {
            scanner.nextToken();
            assertEquals(sy, scanner.getCurrentToken().getSymbol());
        }
        
        scanner.nextToken();
        assertTrue("EOFSY expected", scanner.getCurrentToken().getSymbol() == Symbol.EOFSY);
        scanner.nextToken();
        assertTrue("EOFSY expected", scanner.getCurrentToken().getSymbol() == Symbol.EOFSY);
    }
}
