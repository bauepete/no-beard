/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error.scanerr;

/**
 *
 * @author peter
 */
public class ScanErr extends error.Error{
    ScanErr(int errNo, String errMsg, int lineNumber) {
        super("ScanErr", errNo, errMsg, lineNumber);
    }
    
}
