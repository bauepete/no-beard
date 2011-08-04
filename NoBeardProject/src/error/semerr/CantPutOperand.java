/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error.semerr;

/**
 *
 * @author peter
 */
public class CantPutOperand extends SemErr {
    public CantPutOperand(int lineNumber) {
        super(51, "Can't put this operand", lineNumber);
    }
}
