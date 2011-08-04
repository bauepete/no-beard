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
    public NameAlreadyDefined(String name) {
        super("Name " + name + " already defined", 54);
    }
}
