/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author peter
 */
public class ErrorHandler {

    private final List<Error> errors;
    private final int[] errorCounts = {0, 0, 0, 0};
    private final SourceCodeInfo sourceCodeInfo;

    public ErrorHandler(SourceCodeInfo srcCodeInfo) {
        errors = new ArrayList<>();
        this.sourceCodeInfo = srcCodeInfo;
    }

    public int getCount() {
        return errors.size();
    }

    public int getCount(Error.ErrorClass errorClass) {
        return this.errorCounts[errorClass.ordinal()];
    }

    void raise(Error e) {
        e.setLineNumber(sourceCodeInfo.getCurrentLine());
        errorCounts[e.getErrorClass().ordinal()]++;
        errors.add(e);
        System.err.println(e.getErrorClass() + ": " + e.getLineNumber() + ": " + e.getMessage());
    }

    public List<Error> getAllErrors() {
        return errors;
    }

    public Error getLastError() {
        return errors.get(errors.size() - 1);
    }

    public void throwIntegerOverflow() {
        raise(new Error(Error.ErrorType.INTEGER_OVERFLOW));
    }

    public void throwInvalidString() {
        raise(new Error(error.Error.ErrorType.INVALID_STRING));
    }

    public void throwFileNotFound(String sourceFilePath) {
        raise(new Error(Error.ErrorType.FILE_NOT_FOUND, sourceFilePath));
    }

    public void throwSymbolExpectedError(String expectedSymbol, String actualSymbol) {
        raise(new Error(Error.ErrorType.SYMBOL_EXPECTED, expectedSymbol, actualSymbol));
    }
    
    public void throwStatementExpected(String unexpectedSymbol) {
        raise(new Error(Error.ErrorType.STATEMENT_EXPECTED, unexpectedSymbol));
    }

    public void throwBlockNameMismatch(String name, String name1) {
        raise(new Error(Error.ErrorType.BLOCK_NAME_MISSMATCH, name, name1));
    }

    public void throwNameUndefined(String name) {
        raise(new Error(Error.ErrorType.NAME_UNDEFINED, name));
    }

    public void throwOperandOfKindExpected(String toString) {
        raise(new Error(Error.ErrorType.OPERAND_KIND_EXPECTED, toString));
    }

    public void throwOperandsAreIncompatible(Integer sizeOfOp1, String typeOfOp1, Integer sizeOfOp2, String typeOfOp2) {
        raise(new Error(Error.ErrorType.INCOMPATIBLE_TYPES, sizeOfOp1.toString(), typeOfOp1, sizeOfOp2.toString(), typeOfOp2));
    }

    public void throwNameAlreadyDefined(String variableName) {
        raise(new Error(Error.ErrorType.NAME_ALREADY_DEFINED, variableName));
    }

    public void throwTypesExpected(String[] requestedTypes) {
        raise(new error.Error(Error.ErrorType.TYPES_EXPECTED, requestedTypes));
    }

    public void throwPositiveArraySizeExpected() {
        raise(new Error(Error.ErrorType.POSITIVE_ARRAY_SIZE_EXPECTED));
    }

    public void throwNoNestedModules() {
        raise(new Error(Error.ErrorType.NO_NESTED_MODULES));
    }

    /**
     * If a given operator requires a specific operand and the given operand
     * does not match this requirement, this error is to be thrown.
     *
     * @param operator
     * @param requiredOperand
     */
    public void throwOperatorOperandTypeMismatch(String operator, String requiredOperand) {
        raise(new Error(Error.ErrorType.OPERATOR_OPERAND_TYPE_MISMATCH, operator, requiredOperand));
    }

    public void throwGeneralSemanticError(String message) {
        raise(new error.Error(error.Error.ErrorType.GENERAL_SEM_ERROR, message));
    }

    public void throwProgramMemoryOverflow() {
        raise(new error.Error(Error.ErrorType.PROGRAM_MEMORY_OVERFLOW));
    }

    /**
     * Throws an error indicating that the address is invalid for the given
     * request on the data memory.
     * @param atAddress 
     */
    public void throwDataAddressError(String atAddress) {
        raise(new error.Error(Error.ErrorType.DATA_ADDRESS_ERROR, atAddress));
    }

    public void throwProgramAddressError(String atAddress) {
        raise(new error.Error(Error.ErrorType.PROGRAM_ADDRESS_ERROR, atAddress));
    }

    public void throwDivisionByZero() {
        raise(new error.Error(Error.ErrorType.DIVISION_BY_ZERO));
    }

    public void throwOperandRangeError() {
        raise(new error.Error(Error.ErrorType.OPERAND_RANGE_ERROR));
    }
}
