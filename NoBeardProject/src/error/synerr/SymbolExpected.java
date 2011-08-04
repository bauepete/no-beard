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
    
    public SymbolExpected(String sy, int lineNumber) {
        super(22, sy.toString() + " expected", lineNumber);
    }
    
    public SymbolExpected(String sy1, String sy2, int lineNumber) {
        super(22, sy1.toString() + " or " + sy2.toString() + " expected", lineNumber);
    }
    
    public SymbolExpected(String sy1, String sy2, String sy3, int lineNumber) {
        super(22, sy1.toString() + ", " + sy2.toString() + " or " + sy3.toString() + " expected", lineNumber);
    }
}
