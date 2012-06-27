/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import error.ErrorHandler;
import error.SemErr;
import nbm.Code;
import scanner.StringManager;

/**
 *
 * @author peter
 */
public class Operand {

    public enum OperandKind {

        CONSTANT, VARIABLE, ARGUMENT,
        VALONSTACK, ADDRONSTACK,
        UNIT, FUNCTION, ANONYMOUSBLOCK,
        ILLEGAL
    }

    public enum OperandType {

        SIMPLEINT, SIMPLECHAR, SIMPLEBOOL,
        ARRAYCHAR, ARRAYINT, ARRAYBOOL,
        VOID, ERRORTYPE
    }
    protected static SymListManager symListManager = null;
    protected static StringManager stringManager = null;
    protected OperandKind kind;
    protected OperandType type;
    protected int size;
    protected int valaddr;
    protected int level;
    public static final int UNDEFSIZE = -1;

    public Operand(OperandKind kind, OperandType type, int size, int valaddr, int level) {
        this.kind = kind;
        this.type = type;
        this.size = size;
        this.valaddr = valaddr;
        this.level = level;
    }

    public Operand(Operand op) {
        this.kind = op.getKind();
        this.type = op.getType();
        this.size = op.getSize();
        this.valaddr = op.getValaddr();
        this.level = op.getLevel();
    }

    public static void setSymListManager(SymListManager symListManager) {
        Operand.symListManager = symListManager;
    }

    public static void setStringManager(StringManager stringManager) {
        Operand.stringManager = stringManager;
    }

    public OperandKind getKind() {
        return kind;
    }

    public int getLevel() {
        return level;
    }

    public int getSize() {
        return size;
    }

    public OperandType getType() {
        return type;
    }

    public int getValaddr() {
        return valaddr;
    }

    public int getCurrLevel() {
        if (symListManager == null) {
            throw new NullPointerException("Operand not initialized properly. SymListManager missing.");

        } else {
            return symListManager.getCurrLevel();
        }
    }

    public char getStringStorage(int charAt) {
        if (stringManager == null) {
            throw new NullPointerException("Operand not initialized properly. StringManager missing.");
        }
        return (stringManager.getCharAt(charAt));
    }

    public Operand emitLoadVal(Code toCode) {
        if (getKind() == OperandKind.ANONYMOUSBLOCK || getKind() == OperandKind.FUNCTION ||
                getKind() == OperandKind.ILLEGAL || getKind() == OperandKind.UNIT) {
            ErrorHandler.getInstance().raise(new SemErr().new IllegalOperand());
            return null;
        }
        
        if (getType() != OperandType.SIMPLEBOOL && getType() != OperandType.SIMPLECHAR && getType() != OperandType.SIMPLEINT) {
            String[] tList = {OperandType.SIMPLEBOOL.toString(), OperandType.SIMPLECHAR.toString(), OperandType.SIMPLEINT.toString()};
            ErrorHandler.getInstance().raise(new SemErr().new TypeExpected(tList));
            return null;
        }
        return this;
    }

    public Operand emitLoadAddr(Code toCode) {
        if (getKind() != OperandKind.CONSTANT && getKind() != OperandKind.VARIABLE &&
                getKind() != OperandKind.ARGUMENT && getKind() != OperandKind.ADDRONSTACK) {
            return null;
        }
        if (getType() != OperandType.SIMPLEBOOL && getType() != OperandType.SIMPLECHAR && getType() != OperandType.SIMPLEINT &&
                getType() != OperandType.ARRAYBOOL && getType() != OperandType.ARRAYCHAR && getType() != OperandType.ARRAYINT) {
            return null;
        }
        return this;
    }

    public boolean emitAssign(Code toCode, Operand destOp) {
        if (destOp.getKind() != OperandKind.ADDRONSTACK) {
            errorHandler().raise(new SemErr().new IllegalOperand());
            return false;
        }

        if (!typesOk(destOp)) {
            String[] tList = {OperandType.SIMPLEINT.toString(), OperandType.SIMPLEBOOL.toString(), OperandType.SIMPLECHAR.toString(),
                              OperandType.ARRAYINT.toString(), OperandType.ARRAYBOOL.toString(), OperandType.ARRAYCHAR.toString()};
            errorHandler().raise(new SemErr().new TypeExpected(tList));
            return false;
        }

        if (!srcKindOk()) {
            errorHandler().raise(new SemErr().new IllegalOperand());
            return false;
        }
        
        if (getType() != destOp.getType()) {
            errorHandler().raise(new SemErr().new IncompatibleTypes(getType().toString(), destOp.getType().toString()));
            return false;
        }
        return true;
    }

    protected ErrorHandler errorHandler() {
        return ErrorHandler.getInstance();
    }

    private boolean typesOk(Operand destOp) {
        return (destOp.getType() != OperandType.ERRORTYPE && destOp.getType() != OperandType.VOID
                && getType() != OperandType.ERRORTYPE && getType() != OperandType.VOID);
    }

    private boolean srcKindOk() {
        return (getKind() != OperandKind.ILLEGAL && getKind() != OperandKind.ANONYMOUSBLOCK
                && getKind() != OperandKind.FUNCTION && getKind() != OperandKind.UNIT);
    }
}
