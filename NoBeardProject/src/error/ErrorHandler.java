/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

import java.util.LinkedList;
import java.util.List;

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

    public void throwSymbolExpectedError(String expectedSymbol, String actualSymbol) {
        errors.add(new Error(Error.ErrorType.SYMBOL_EXPECTED, expectedSymbol, actualSymbol));
        totalCount++;
    }
    
    public void raise(Error e) {
        e.setLineNumber(sourceCodeInfo.getCurrentLine());
        errorCounts[e.getErrorClass().ordinal()]++;
        totalCount++;
        lastError = e;
        System.err.println(e.getErrorClass()+": " + e.getLineNumber() + ": " + e.getMessage());
    }
    
    public List<Error> getAllErrors() {
        return errors;
    }
    
    public Error getLastError() {
        return lastError;
    }
}
