/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

/**
 *
 * @author peter
 */
public class SemErr extends Error {

    public SemErr() {
        super("SemErr", 99, "General semantic error");
    }

    public SemErr(int errNo, String errMsg) {
        super("SemErr", errNo, errMsg);
    }

    public class BlockNameMismatch extends SemErr {

        public BlockNameMismatch(String name1, String name2) {
            super(50, "Block " + name1 + " ends with name " + name2);
        }
    }

    public class CantPutOperand extends SemErr {

        public CantPutOperand() {
            super(51, "Can't put this operand");
        }
    }

    public class IllegalOperand extends SemErr {

        public IllegalOperand() {
            super(52, "Illegal operand");
        }
    }

    public class IncompatibleTypes extends SemErr {

        public IncompatibleTypes(String srcType, String destType) {
            super(53, "Incompatible types: Expected " + srcType + " found " + destType);
        }
    }

    public class NameAlreadyDefined extends SemErr {

        public NameAlreadyDefined(String name) {
            super(54, "Name " + name + " already defined");
        }
    }

    public class TypeExpected extends SemErr {

        public TypeExpected(String type) {
            super(55, "Operand of type " + type + " expected");
        }
        
        public TypeExpected(String[] types) {
            super(55, "");
            StringBuilder sb = new StringBuilder();
            sb.append("Operand of type ");
            for (int i = 0; i < types.length - 1; i++) {
                sb.append(types[i]);
                sb.append(" or ");
            }
            sb.append(types[types.length - 1]);
            sb.append(" expected");
            setMessage(sb.toString());
        }
    }

    public class IndexExpected extends SemErr {

        public IndexExpected() {
            super(56, "Index greater than 0 expected");
        }
    }
    
    public class IllegalFunctionStart extends SemErr {
        public IllegalFunctionStart() {
            super(57, "Illegal function start");
        }
    }
}