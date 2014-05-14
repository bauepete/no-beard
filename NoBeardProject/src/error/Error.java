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
public class Error {

    public enum ErrorClass {

        LEXICAL, SYNTAX, SEMANTICAL
    }

    public enum ErrorType {

        INTEGER_OVERFLOW(1, ErrorClass.LEXICAL, "Integer overflow"),
        INVALID_STRING(2, ErrorClass.LEXICAL, "Non terminated string constant"),
        
        SYMBOL_EXPECTED(21, ErrorClass.SYNTAX, "%s expected"),
        IDENTIFIER_EXPECTED(22, ErrorClass.SYNTAX, "Identifier expected"),
        
        GENERAL_SYN_ERROR(49, ErrorClass.SYNTAX, "General syntax error: %s"),
        
        // TODO: following message should be replaced by "Block %s1 can't can't end with name %s2"
        BLOCK_NAME_MISSMATCH(50, ErrorClass.SEMANTICAL, "Block name start differs from block name end: %s"),
        OPERAND_KIND_EXPECTED(52, ErrorClass.SEMANTICAL, "%s expected"),
        // TODO: following message should be replaced by "%s can't be converted to %s"
        INCOMPATIBLE_TYPES(53, ErrorClass.SEMANTICAL, "The following types are not compatible: %s"),
        NAME_ALREADY_DEFINED(54, ErrorClass.SEMANTICAL, "%s already defined"),
        TYPE_EXPECTED(55, ErrorClass.SEMANTICAL, "Type %s expected"),
        POSITIVE_ARRAY_SIZE_EXPECTED(56, ErrorClass.SEMANTICAL, "Array size spezifier has to be a positive integer."),
        NO_NESTED_MODULES(57, ErrorClass.SEMANTICAL, "Nested modules are not allowed"),
        GENERAL_SEM_ERROR(99, ErrorClass.SEMANTICAL, "General sem error: %s");

        private final int errorNumber;
        private final ErrorClass errorClass;
        private final String errorMessage;

        ErrorType(int errorNumber, ErrorClass errorClass, String errorMessage) {
            this.errorNumber = errorNumber;
            this.errorClass = errorClass;
            this.errorMessage = errorMessage;
        }

        public Error.ErrorClass getErrorClass() {
            return errorClass;
        }

        public int getNumber() {
            return errorNumber;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    private ErrorType errorType;

    private static Scanner scanner;
    private String errClass;
    private int errNo;
    private String errMsg;
    private int lineNumber;
    private String[] parameters;

    /**
     * @deprecated @param scanner
     */
    public static void setScanner(Scanner scanner) {
        Error.scanner = scanner;
    }

    public Error(ErrorType errorType) {
        this.errorType = errorType;
    }

    public Error(ErrorType errorType, String parameter) {
        this.errorType = errorType;
        this.parameters = new String[]{parameter};
    }

    public Error(ErrorType errorType, String[] parameters) {
        this.errorType = errorType;
        this.parameters = parameters;
    }
    
    void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * @deprecated
     * @param errClass
     * @param errNo
     * @param errMsg
     * @param lineNumber 
     */
    public Error(String errClass, int errNo, String errMsg, int lineNumber) {
        this.errClass = errClass;
        this.errNo = errNo;
        this.errMsg = errMsg;
        this.lineNumber = lineNumber;
    }

    /**
     * @deprecated
     * @param errClass
     * @param errNo
     * @param errMsg 
     */
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
     *
     * @return String which uniquely identifies the error class to which the
     * error belongs.
     */
    public ErrorClass getErrorClass() {
        return errorType.getErrorClass();
    }

    /**
     * Returns the message to be printed in case of the occurrence of this
     * error.
     *
     * @return
     */
    public String getMessage() {
        String msg = errorType.getErrorMessage();
        if (msg.contains("%s")) {
            return compileErrorMessage(msg);
        } else {
            return errorType.getErrorMessage();
        }
    }

    private String compileErrorMessage(String msg) {
            StringBuilder sb = new StringBuilder(parameters[0]);
            for (int i = 1; i < parameters.length; i++) {
                if (i == parameters.length - 1)
                    sb.append(" or ");
                else
                    sb.append(", ");
                sb.append(parameters[i]);
            }
                
            return msg.replaceAll("%s", sb.toString());
    }

    /**
     * @deprecated @return
     */
    public int getErrNo() {
        return errNo;
    }

    public int getNumber() {
        return errorType.getNumber();
    }

    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * @deprecated 
     * @param msg 
     */
    protected void setMessage(String msg) {
        this.errMsg = msg;
    }

    /**
     * @deprecated 
     * @return 
     */
    private Scanner getScanner() {
        if (scanner == null) {
            throw new NullPointerException("Error not initialized properly. Initial setScanner missing.");
        } else {
            return scanner;
        }
    }

}
