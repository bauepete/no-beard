/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import nbm.CodeGenerator;
import nbm.Nbm.Opcode;
import symlist.Operand.OperandKind;
import symlist.Operand.OperandType;

/**
 *
 * @author peter
 */
public class VariableOperand extends Operand {

    public VariableOperand(OperandType type, int size, int valaddr, int level) {
        super(OperandKind.VARIABLE, type, size, valaddr, level);
    }

    public VariableOperand(Operand op) {
        super(op);
    }

    @Override
    public Operand emitLoadVal(CodeGenerator toCode) {
        switch (type) {
            case SIMPLEINT:
            case SIMPLEBOOL:
                toCode.emitOp(Opcode.LV);
                toCode.emitByte((byte) (getCurrLevel() - getLevel()));
                toCode.emitHalfWord(valaddr);
                break;

            case SIMPLECHAR:
                toCode.emitOp(Opcode.LC);
                toCode.emitByte((byte) (getCurrLevel() - getLevel()));
                toCode.emitHalfWord(valaddr);
                break;
        }
        return (new ValueOnStackOperand(this));
    }

    @Override
    public boolean emitAssign(CodeGenerator toCode, Operand destOp) {
        switch (destOp.getType()) {
            case SIMPLEINT:
            case SIMPLEBOOL:
                toCode.emitOp(Opcode.LV);
                toCode.emitByte((byte) (getCurrLevel() - getLevel()));
                toCode.emitHalfWord(valaddr);
                toCode.emitOp(Opcode.STO);
                break;

            case SIMPLECHAR:
                //toCode.emitOp(Opcode.LC);
                // TODO: Implement assign for char
                break;
        }
        return true;
    }

    @Override
    public Operand emitLoadAddr(CodeGenerator toCode) {
        toCode.emitOp(Opcode.LA);
        toCode.emitByte((byte) (getCurrLevel() - getLevel()));
        toCode.emitHalfWord(valaddr);

        return (new AddrOnStackOperand(this));
    }
}
