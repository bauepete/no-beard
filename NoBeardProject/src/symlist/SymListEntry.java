/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import symlist.Operand.OperandKind;
import symlist.Operand.OperandType;

/**
 *
 * @author peter
 */
public class SymListEntry {

    private int name;
    private OperandKind kind;
    private OperandType type;
    private int size;
    private int addr;
    private int level;

    public SymListEntry(int name, OperandKind kind, OperandType type, int size, int addr, int level) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.size = size;
        this.addr = addr;
        this.level = level;
    }

    public OperandKind getKind() {
        return kind;
    }

    public int getLevel() {
        return level;
    }

    public int getName() {
        return name;
    }

    public OperandType getType() {
        return type;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void addSize(int size) {
        this.size += size;
    }

    public int getSize() {
        return size;
    }

    public int getAddr() {
        return addr;
    }

    public void setAddr(int addr) {
        this.addr = addr;
    }
    
    public boolean isNamedBlockEntry() {
        return (kind == OperandKind.FUNCTION || kind == OperandKind.UNIT);
    }

    public Operand createOperand() {
        Operand op = null;

        switch (kind) {
            case ANONYMOUSBLOCK:
            case FUNCTION:
            case UNIT:
                op = new UnitOperand(type, size, addr, level);
                break;
            case CONSTANT:
                op = new ConstantOperand(type, size, addr, level);
                break;

            case VARIABLE:
                op = new VariableOperand(type, size, addr, level);
                break;

            default:
                op = new IllegalOperand();
        }
        return op;
    }
}
