/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error.scanerr;

/**
 *
 * @author peter
 */
public class InvalidString extends ScanErr {
    public InvalidString(int errNo, int lineNumber) {
        super(0, "Unclosed string literal", lineNumber);
    }
    
}
