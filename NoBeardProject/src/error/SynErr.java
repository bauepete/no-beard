/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

/**
 *
 * @author peter
 */
public class SynErr extends Error {

    public SynErr() {
        super("SynErr", 49, "General syntax error");
    }

    public SynErr(int errNo, String msg) {
        super("SynErr", errNo, msg);
    }

    /* -------------------------------------------------------------------- */
    /* ----------------------- Concrete syntax errors --------------------- */
    /* -------------------------------------------------------------------- */
    public class IdentifierExpected extends SynErr {

        public IdentifierExpected() {
            super(20, "Identifier expected");
        }
    }

    public class StatementExpected extends SynErr {

        public StatementExpected() {
            super(21, "Is not a statement");
        }
    }

    public class SymbolExpected extends SynErr {

        public SymbolExpected(String sy) {
            super(22, sy.toString() + " expected");
        }
        
        public SymbolExpected(String[] sy) {
            super(22, "");
            StringBuilder sb = new StringBuilder();
            sb.append("Symbols ");
            for (int i = 0; i < sy.length - 1; i++) {
                sb.append(sy[i]);
                sb.append(" or ");
            }
            sb.append(sy[sy.length - 1]);
            sb.append(" epxected");
            setMessage(sb.toString());
        }
    }
}