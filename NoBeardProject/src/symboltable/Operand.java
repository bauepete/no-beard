/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symboltable;

import error.ErrorHandler;
import machine.CodeGenerator;
import scanner.StringManager;

/**
 *
 * @author peter
 */
public class Operand {

    public enum Kind {

        CONSTANT, VARIABLE, ARGUMENT,
        VALUEONSTACK, ADDRONSTACK,
        UNIT, FUNCTION, ANONYMOUSBLOCK,
        ILLEGAL
    }

    public enum Type {

        SIMPLEINT("int"),
        SIMPLECHAR("char"),
        SIMPLEBOOL("bool"),
        ARRAYCHAR("array of char"),
        ARRAYINT("array of int"),
        ARRAYBOOL("array of bool"),
        VOID("void"),
        ERRORTYPE("error operand type!!!");

        private final String displayName;

        Type(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    protected static SymbolTable symListManager = null;
    protected static StringManager stringManager = null;
    private static ErrorHandler errorHandler;
    protected Kind kind;
    protected Type type;
    protected int size;
    protected int valaddr;
    protected int level;
    public static final int UNDEFSIZE = -1;

    public Operand(Kind kind, Type type, int size, int valaddr, int level) {
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

    public static void setSymListManager(SymbolTable symListManager) {
        Operand.symListManager = symListManager;
    }

    public static void setStringManager(StringManager stringManager) {
        Operand.stringManager = stringManager;
    }

    public static void setErrorHandler(ErrorHandler eh) {
        errorHandler = eh;
    }

    public Kind getKind() {
        return kind;
    }

    public int getLevel() {
        return level;
    }

    public int getSize() {
        return size;
    }

    public Type getType() {
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

    public Operand emitLoadVal(CodeGenerator toCode) {
        if (getKind() == Kind.ANONYMOUSBLOCK || getKind() == Kind.FUNCTION
                || getKind() == Kind.ILLEGAL || getKind() == Kind.UNIT) {
            errorHandler().throwGeneralSemanticError("Operand can't be loaded on stack");
            return null;
        }

        if (getType() != Type.SIMPLEBOOL && getType() != Type.SIMPLECHAR && getType() != Type.SIMPLEINT) {
            String[] tList = {Type.SIMPLEBOOL.toString(), Type.SIMPLECHAR.toString(), Type.SIMPLEINT.toString()};
            errorHandler().throwTypesExpected(tList);
            return null;
        }
        return this;
    }

    public Operand emitLoadAddr(CodeGenerator toCode) {
        if (getKind() != Kind.CONSTANT && getKind() != Kind.VARIABLE
                && getKind() != Kind.ARGUMENT && getKind() != Kind.ADDRONSTACK) {
            return null;
        }
        if (getType() != Type.SIMPLEBOOL && getType() != Type.SIMPLECHAR && getType() != Type.SIMPLEINT
                && getType() != Type.ARRAYBOOL && getType() != Type.ARRAYCHAR && getType() != Type.ARRAYINT) {
            return null;
        }
        return this;
    }

    public boolean emitAssign(CodeGenerator toCode, Operand destOp) {
        if (destOp.getKind() != Kind.ADDRONSTACK) {
            errorHandler.throwGeneralSemanticError("Error in code generator: destination operand is not on stack");
            return false;
        }

        if (!typesOk(destOp)) {
            String[] tList = {Type.SIMPLEINT.toString(), Type.SIMPLEBOOL.toString(), Type.SIMPLECHAR.toString(),
                Type.ARRAYINT.toString(), Type.ARRAYBOOL.toString(), Type.ARRAYCHAR.toString()};
            errorHandler().throwTypesExpected(tList);
            return false;
        }

        if (!srcKindOk()) {
            errorHandler.throwGeneralSemanticError("Error in code generator: illegal source operand");
            return false;
        }

        if (getType() != destOp.getType()) {
            errorHandler.throwOperandsAreIncompatible(getSize(), getType().toString(), destOp.getSize(), destOp.getType().toString());
            return false;
        }
        return true;
    }

    protected ErrorHandler errorHandler() {
        return errorHandler;
    }

    private boolean typesOk(Operand destOp) {
        return (destOp.getType() != Type.ERRORTYPE && destOp.getType() != Type.VOID
                && getType() != Type.ERRORTYPE && getType() != Type.VOID);
    }

    private boolean srcKindOk() {
        return (getKind() != Kind.ILLEGAL && getKind() != Kind.ANONYMOUSBLOCK
                && getKind() != Kind.FUNCTION && getKind() != Kind.UNIT);
    }
}
