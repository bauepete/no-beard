/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error.semerr;

/**
 *
 * @author peter
 */
public class TypeExpected extends SemErr {
    public TypeExpected(String type) {
        super("Operand of type " + type + " expected", 55);
    }
}
