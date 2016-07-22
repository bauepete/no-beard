/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

import java.util.LinkedList;
import java.util.List;
import symboltable.Operand;

/**
 *
 * @author peter
 */
public class ErrorHandler {
    private final List<Error> errors;
    private final int [] errorCounts = {0, 0, 0};
    private Error lastError = null;
    private int totalCount;
    private final SourceCodeInfo sourceCodeInfo;

    public ErrorHandler(SourceCodeInfo srcCodeInfo) {
        errors = new LinkedList<>();
        this.sourceCodeInfo = srcCodeInfo;
    }
    
    public void reset() {
        totalCount = 0;
        errors.clear();
    }

    public int getCount() {
        return totalCount;
    }

    public int getCount(Error.ErrorClass errorClass) {
        return this.errorCounts[errorClass.ordinal()];
    }

    public void printSummary() {

//        for (Map.Entry<String, Integer> entry : errors.entrySet()) {
//            System.err.print(entry.getKey().toString());
//            System.err.println(entry.getValue().toString());
//        }
        System.err.println();
    }

    public void raise(Error e) {
        e.setLineNumber(sourceCodeInfo.getCurrentLine());
        errorCounts[e.getErrorClass().ordinal()]++;
        totalCount++;
        errors.add(e);
        lastError = e;
        System.err.println(e.getErrorClass()+": " + e.getLineNumber() + ": " + e.getMessage());
    }
    
    public List<Error> getAllErrors() {
        return errors;
    }
    
    public Error getLastError() {
        return lastError;
    }

     public void throwSymbolExpectedError(String expectedSymbol, String actualSymbol) {
        raise(new Error(Error.ErrorType.SYMBOL_EXPECTED, expectedSymbol, actualSymbol));
    }
    
   public void throwOperandOfKindExpected(String toString) {
        raise(new Error(Error.ErrorType.OPERAND_KIND_EXPECTED, toString));
    }

   /**
    * If a given operator requires a specific operand and the given operand
    * does not match this requirement, this error is to be thrown.
    * @param operator
    * @param requiredOperand 
    */
    public void throwOperatorOperandTypeMismatch(String operator, String requiredOperand) {
        raise(new Error(Error.ErrorType.OPERATOR_OPERAND_TYPE_MISMATCH, operator, requiredOperand));
    }

    public void throwOperandsAreIncompatible(Integer sizeOfOp1, Operand.Type typeOfOp1, Integer sizeOfOp2, Operand.Type typeOfOp2) {
        raise(new Error(Error.ErrorType.INCOMPATIBLE_TYPES, sizeOfOp1.toString(), typeOfOp1.toString(), sizeOfOp2.toString(), typeOfOp2.toString()));
    }

    public void throwVariableAlreadyDefed(String variableName) {
        raise(new Error(Error.ErrorType.NAME_ALREADY_DEFINED, variableName));
    }
}
