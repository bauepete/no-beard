/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

import error.synerr.IdentifierExpected;
import error.semerr.NameAlreadyDefined;
import error.synerr.SymbolExpected;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scanner.Scanner.Symbol;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class ErrorHandlerTest {

    private ErrorHandler eh;

    public ErrorHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        eh = ErrorHandler.getInstance();
    }

    @After
    public void tearDown() {
        eh.reset();
    }

    /**
     * Test of getInstance method, of class ErrorHandler.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        ErrorHandler h1 = ErrorHandler.getInstance();
        ErrorHandler h2 = ErrorHandler.getInstance();
        assertEquals("Expected same handler", h1, h2);
    }

    /**
     * Test of raise method, of class ErrorHandler.
     */
    @Test
    public void testRaise() {
        System.out.println("add");

        eh.raise(new NameAlreadyDefined("Sepp"));
        assertEquals("Count expected", 1, eh.getCount());
        assertEquals("SemErrs expected", 1, eh.getCount("SemErr"));

        eh.raise(new NameAlreadyDefined("Koal"));
        assertEquals("Count expected", 2, eh.getCount());
        assertEquals("SemErrs expected", 2, eh.getCount("SemErr"));

    }

    /**
     * Test of getCount method, of class ErrorHandler.
     */
    @Test
    public void testAddDifferent() {
        System.out.println("testAddDifferent");

        eh.raise(new NameAlreadyDefined("Sepp"));
        eh.raise(new SymbolExpected(Symbol.SEMICOLONSY.toString()));
        eh.raise(new IdentifierExpected());
        assertEquals("Count expected", 3, eh.getCount());
        assertEquals("SemErrs expected", 1, eh.getCount("SemErr"));
        assertEquals("SynErrs expected", 2, eh.getCount("SynErr"));

    }
    
    @Test
    public void testGetLastError() {
        System.out.println("testAddDifferent");
        eh.raise(new NameAlreadyDefined("Sepp"));
        eh.raise(new SymbolExpected(Symbol.SEMICOLONSY.toString()));
        eh.raise(new IdentifierExpected());
        assertEquals("Last error", 0, eh.getLastError().getErrNo());
        
    }
}
