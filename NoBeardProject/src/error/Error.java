/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

/**
 *
 * @author peter
 */
public abstract class Error {

    private String errClass;
    private int errNo;
    private String errMsg;
    private int lineNumber;
    
    public Error(String errClass, int errNo, String errMsg, int lineNumber) {
        this.errClass = errClass;
        this.errNo = errNo;
        this.errMsg = errMsg;
        this.lineNumber = lineNumber;
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
    
}
