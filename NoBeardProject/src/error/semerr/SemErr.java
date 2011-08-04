/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error.semerr;

import error.Error;

/**
 *
 * @author peter
 */
public class SemErr extends Error {
    
    
    public SemErr(int errNo, String errMsg, int lineNumber) {
        super("SemErr", errNo, errMsg, lineNumber);
    }
}