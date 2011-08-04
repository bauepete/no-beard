/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error.synerr;

/**
 *
 * @author peter
 */
public class SymbolExpected extends SynErr {
    
    public SymbolExpected(String sy) {
        super(sy.toString() + " expected", 2);
    }
}
