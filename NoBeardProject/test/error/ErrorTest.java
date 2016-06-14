/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class ErrorTest {
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testSimpleErrors() {
        Error error = new Error(Error.ErrorType.INVALID_STRING);
        assertEquals(Error.ErrorClass.LEXICAL, error.getErrorClass());
        assertEquals(2, error.getNumber());
        assertEquals("Non terminated string constant", error.getMessage());
    }
    
    @Test
    public void testErrorsWithOneParameter() {
        Error error = new Error(Error.ErrorType.NAME_ALREADY_DEFINED, "foo");
        assertEquals("Identifier foo already defined", error.getMessage());
    }
    
    @Test
    public void testErrorsWithList() {
        String[] typeList = {"simple bool", "simple char", "simple int"};
        Error error = new Error(Error.ErrorType.TYPES_EXPECTED, typeList);
        assertEquals("Types simple bool, simple char or simple int expected", error.getMessage());
    }
    
    @Test
    public void testErrorWithVariableParameterList() {
        Error error = new Error(Error.ErrorType.SYMBOL_EXPECTED, "unit", "Identifier");
        assertEquals("unit expected but found Identifier", error.getMessage());
    }
}
