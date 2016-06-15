/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import compiler.NbCompiler;
import error.ErrorHandler;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nbm.CodeGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.NoBeardParser;
import scanner.Scanner;
import scanner.SrcFileReader;
import scanner.SrcReader;
import scanner.SrcStringReader;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class NoBeardParserTest {
    
    private NbCompiler compiler;
    private CodeGenerator c;
    private SymListManager sym;
    private Scanner s;
    private NoBeardParser parser;
    private ErrorHandler errorHandler;
    
    public NoBeardParserTest() {
    }

    @Before
    public void setUp() {
        c = new CodeGenerator(256);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of parseOldStyle method, of class NoBeardParser.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        
        setupTest(new SrcStringReader("unit foo; do int x = 3; done foo;"));
        boolean parsingSuccessful = parser.parseOldStyle();
        assertTrue("True expected", parsingSuccessful);
        assertEquals(parsingSuccessful, parser.parsingWasSuccessful());
        
        setupTest(new SrcStringReader("unit bah; do int x = 3; put (x); done bah;"));
        assertTrue("True expected", parser.parseOldStyle());
        
        setupTest(new SrcStringReader("unit rsch; do int x = 3; int y = 1; put (x + y); done rsch;"));
        assertTrue("True expected", parser.parseOldStyle());
    }
    
    private void setupTest(SrcReader sr) {
        compiler = new NbCompiler(sr);
        errorHandler = compiler.getErrorHandler();
        s = compiler.getScanner();
        sym = compiler.getSymListManager();
        c = compiler.getCode();
        parser = compiler.getParser();
    }

    @Test
    public void testParseEmpty() {
        System.out.println("parse");
        
        setupTest(new SrcStringReader("unit foo; do done foo;"));

        assertTrue("True expected", parser.parseOldStyle());
    }
    
    @Test
    public void testUnitExpected() {
        System.out.println("testUnitExpected");

        setupTest(new SrcStringReader("unti foo; do put x; done foo;"));

        boolean parsingWasSuccessFul = parser.parseOldStyle();
        assertFalse("False expected", parsingWasSuccessFul);
        assertEquals(parsingWasSuccessFul, parser.parsingWasSuccessful());
        assertEquals("Error count expected: ", 1, errorHandler.getCount());
        assertEquals("unit expected but found identifier", errorHandler.getAllErrors().get(0).getMessage());
    }

    @Test
    public void testUnitIdentifierExpected() {
        System.out.println("testUnitIdentifierExpected");

        setupTest(new SrcStringReader("unit; do put x; done foo;"));
        
        assertFalse("False expected", parser.parseOldStyle());
        assertEquals("Last error", error.Error.ErrorType.SYMBOL_EXPECTED.getNumber(), errorHandler.getLastError().getNumber());
    }

    @Test
    public void testParseSmallest() {
        System.out.println("testParseSmallest");

        try {
            setupTest(new SrcFileReader("SamplePrograms/Smallest.nb"));

            assertTrue("True expected", parser.parseOldStyle());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NoBeardParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
