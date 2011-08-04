/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

/**
 *
 * @author peter
 */
public interface Error {

    /**
     * Errors can be separated in different classes. The ErrorHandler
     * distinguishes the error classes via the string returned by
     * getErrorClass().
     * @return String which uniquely identifies the error class to
     * which the error belongs.
     */
    public String getErrorClass();

    public String getLongName();

    /**
     * Returns the message to be printed in case of the occurrence of
     * this error.
     * @return 
     */
    public String getMessage();
    
    public int getErrNo();
}
