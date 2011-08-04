/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import nbm.Code;

/**
 *
 * @author peter
 */
public abstract class Operand {

    public enum OperandKind {

        CONSTANT, VARIABLE, ARGUMENT,
        VALONSTACK, ADDRONSTACK,
        BLOCK, ILLEGAL
    }

    public enum OperandType {

        SIMPLEINT, SIMPLECHAR, SIMPLEBOOL, UNITTYPE, ERRORTYPE
    }
    protected static SymListManager symListManager = null;
    protected OperandKind kind;
    protected OperandType type;
    protected int size;
    protected int valaddr;
    protected int level;

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
            throw new NullPointerException("Operand not initialized properly.");

        } else {
            return symListManager.getCurrLevel();
        }
    }
    

    public abstract Operand emitLoadVal(Code toCode);

    public abstract Operand emitLoadAddr(Code toCode);

    public abstract void emitAssign(Code toCode, Operand destOp);
}
