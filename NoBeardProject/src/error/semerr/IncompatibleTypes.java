/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error.semerr;

/**
 *
 * @author peter
 */
public class IncompatibleTypes extends SemErr {
    public IncompatibleTypes(String srcType, String destType, int lineNumber) {
        super(53, "Incompatible types: Expected " + srcType + " found " + destType, lineNumber);
    }
    
}
