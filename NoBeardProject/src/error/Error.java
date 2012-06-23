/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

import scanner.Scanner;

/**
 *
 * @author peter
 */
public abstract class Error {

    private static Scanner scanner;
    private String errClass;
    private int errNo;
    private String errMsg;
    private int lineNumber;
    
    public static void setScanner(Scanner scanner) {
        Error.scanner = scanner;
    }
    
    public Error(String errClass, int errNo, String errMsg, int lineNumber) {
        this.errClass = errClass;
        this.errNo = errNo;
        this.errMsg = errMsg;
        this.lineNumber = lineNumber;
    }
    
    public Error(String errClass, int errNo, String errMsg) {
        this.errClass = errClass;
        this.errNo = errNo;
        this.errMsg = errMsg;
        this.lineNumber = getScanner().getCurrentLine();
    }
    
    /**
     * Errors can be separated in different classes. The ErrorHandler
     * distinguishes the error classes via the string returned by
     * getErrorClass().
     * @return String which uniquely identifies the error class to
     * which the error belongs.
     */
    public String getErrorClass() {
        return errClass;
    }

    /**
     * Returns the message to be printed in case of the occurrence of
     * this error.
     * @return 
     */
    public String getMessage() {
        return (errMsg);
    }
    
    public int getErrNo() {
        return errNo;
    }

    public int getLineNumber() {
        return lineNumber;
    }
    
    protected void setMessage(String msg) {
        this.errMsg = msg;
    }
    
    private Scanner getScanner() {
        if (scanner == null) {
            throw new NullPointerException("Error not initialized properly. Initial setScanner missing.");
        } else {
            return scanner;
        }
    }
    
}
