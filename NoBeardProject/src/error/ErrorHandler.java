/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author peter
 */
public class ErrorHandler {

    private static ErrorHandler instance = null;
    private HashMap<String, Integer> errors;
    private Error lastError = null;
    private int totalCount;

    protected ErrorHandler() {
        errors = new HashMap<String, Integer>();
    }

    public static ErrorHandler getInstance() {
        if (instance == null) {
            instance = new ErrorHandler();
        }
        return instance;
    }
    
    public void reset() {
        totalCount = 0;
        errors.clear();
    }

    public int getCount() {
        return totalCount;
    }

    public int getCount(String errorClass) {
        if (errors.containsKey(errorClass)) {
            return errors.get(errorClass);
        } else {
            return 0;
        }
    }

    public void printSummary() {

        for (Map.Entry<String, Integer> entry : errors.entrySet()) {
            System.err.print(entry.getKey().toString());
            System.err.println(entry.getValue().toString());
        }
        System.err.println();
    }

    public void raise(Error e) {
        int count = 0;

        if (errors.containsKey(e.getErrorClass())) {
            count = errors.get(e.getErrorClass());
        }
        count++;
        totalCount++;
        errors.put(e.getErrorClass(), count);
        lastError = e;
        System.err.println(e.getErrorClass()+": " + e.getErrNo() + ": " + e.getMessage());
    }
    
    public Error getLastError() {
        return lastError;
    }
}
