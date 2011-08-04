/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error.semerr;

/**
 *
 * @author peter
 */
public class NameAlreadyDefined extends SemErr {
    public NameAlreadyDefined(String name, int lineNumber) {
        super(54, "Name " + name + " already defined", lineNumber);
    }
}
