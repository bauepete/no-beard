/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

import compiler.SourceCodeInfo;
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
        errors = new LinkedList<Error>();
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
    /**
     * @deprecated
     * @param errorClass
     * @return 
     */
    public int getCount(String errorClass) {
//        if (errors.containsKey(errorClass)) {
//            return errors.get(errorClass);
//        } else {
//            return 0;
//        }
        return 0;
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
        lastError = e;
        System.err.println(e.getErrorClass()+": " + e.getLineNumber() + ": " + e.getMessage());
    }
    
    public Error getLastError() {
        return lastError;
    }
}
