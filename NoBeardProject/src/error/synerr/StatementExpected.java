/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error.synerr;

/**
 *
 * @author peter
 */
public class StatementExpected extends SynErr {
    public StatementExpected(int lineNumber) {
        super(21, "Is not a statement", lineNumber);
    }
    
}
