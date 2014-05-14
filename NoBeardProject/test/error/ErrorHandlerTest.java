/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

import error.Error.ErrorType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scanner.Scanner.Symbol;
import static org.junit.Assert.*;
import scanner.SrcStringReader;

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
        eh = new ErrorHandler(new SrcStringReader(""));
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void testRaiseOneError() {
        eh.raise(new Error(ErrorType.INTEGER_OVERFLOW));
        assertEquals("Count", 1, eh.getCount());
        assertEquals(1, eh.getLastError().getLineNumber());
    }

    @Test
    public void testRaiseMoreErrorsOfSameClass() {
        //TODO: move to setup
        int lineNumber0 = 17;
        int lineNumber1 = 42;
        eh.raise(new Error(ErrorType.INTEGER_OVERFLOW));
        eh.raise(new Error(ErrorType.INVALID_STRING));
        assertEquals(2, eh.getCount());
        assertEquals(2, eh.getCount(Error.ErrorClass.LEXICAL));
        assertEquals(0, eh.getCount(Error.ErrorClass.SYNTAX));
        assertEquals(0, eh.getCount(Error.ErrorClass.SEMANTICAL));
    }

    /**
     * Test of raise method, of class ErrorHandler.
     */
    @Test
    public void testRaiseGeneral() {
        eh.raise(new Error(ErrorType.NAME_ALREADY_DEFINED, "Sepp"));
        assertEquals("Count expected", 1, eh.getCount());
        assertEquals("SemErrs expected", 0, eh.getCount(Error.ErrorClass.LEXICAL));
        assertEquals("SemErrs expected", 0, eh.getCount(Error.ErrorClass.SYNTAX));
        assertEquals("SemErrs expected", 1, eh.getCount(Error.ErrorClass.SEMANTICAL));

        eh.raise(new Error(ErrorType.NAME_ALREADY_DEFINED, "Koal"));
        assertEquals("Count expected", 2, eh.getCount());
        assertEquals("SemErrs expected", 0, eh.getCount(Error.ErrorClass.LEXICAL));
        assertEquals("SemErrs expected", 0, eh.getCount(Error.ErrorClass.SYNTAX));
        assertEquals("SemErrs expected", 2, eh.getCount(Error.ErrorClass.SEMANTICAL));

        eh.raise(new Error(ErrorType.SYMBOL_EXPECTED, "Semicolon"));
        assertEquals("Count expected", 3, eh.getCount());
        assertEquals("SemErrs expected", 0, eh.getCount(Error.ErrorClass.LEXICAL));
        assertEquals("SemErrs expected", 1, eh.getCount(Error.ErrorClass.SYNTAX));
        assertEquals("SemErrs expected", 2, eh.getCount(Error.ErrorClass.SEMANTICAL));

    }

    @Test
    public void testGetLastError() {
        eh.raise(new Error(ErrorType.NAME_ALREADY_DEFINED, "Sepp"));
        eh.raise(new Error(ErrorType.SYMBOL_EXPECTED, Symbol.SEMICOLONSY.toString()));
        eh.raise(new Error(ErrorType.IDENTIFIER_EXPECTED));
        assertEquals("Last error", 22, eh.getLastError().getNumber());

    }
}
