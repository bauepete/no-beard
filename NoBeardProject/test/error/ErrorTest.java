/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class ErrorTest {

    @Test
    public void testErrorsWithoutFormatString() {
        Error error = new Error(Error.ErrorType.INTEGER_OVERFLOW);
        checkErrorObject(error, 1, Error.ErrorClass.LEXICAL, "Integer overflow");

        error = new Error(Error.ErrorType.INVALID_STRING);
        checkErrorObject(error, 2, Error.ErrorClass.LEXICAL, "Non terminated string constant");
        
        error = new Error(Error.ErrorType.POSITIVE_ARRAY_SIZE_EXPECTED);
        checkErrorObject(error, 56, Error.ErrorClass.SEMANTICAL, "Array size must be a positive integer");        
    }

    private void checkErrorObject(Error error, int errorNumber, Error.ErrorClass errorClass, String errorMessage) {
        assertEquals(errorClass, error.getErrorClass());
        assertEquals(errorNumber, error.getNumber());
        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    public void testErrorsWithOneParameter() {
        Error error = new Error(Error.ErrorType.OPERAND_KIND_EXPECTED, "Variable");
        checkErrorObject(error, 52, Error.ErrorClass.SEMANTICAL, "Variable expected");
        
        error = new Error(Error.ErrorType.NAME_ALREADY_DEFINED, "foo");
        checkErrorObject(error, 54, Error.ErrorClass.SEMANTICAL, "Identifier foo is already defined");
    }

    @Test
    public void testErrorsWithTwoParameters() {
        Error error = new Error(Error.ErrorType.SYMBOL_EXPECTED, "foo", "bar");
        checkErrorObject(error, 21, Error.ErrorClass.SYNTAX, "foo expected but found bar");
        
        error = new Error(Error.ErrorType.OPERATOR_OPERAND_TYPE_MISMATCH, "&&", "bool");
        checkErrorObject(error, 58, Error.ErrorClass.SEMANTICAL, "Operator && requires a bool operand");
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
