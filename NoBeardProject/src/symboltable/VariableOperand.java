/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symboltable;

import nbm.CodeGenerator;
import nbm.ControlUnit.Opcode;
import symboltable.Operand.Kind;
import symboltable.Operand.Type;

/**
 *
 * @author peter
 */
public class VariableOperand extends Operand {

    public VariableOperand(Type type, int size, int valaddr, int level) {
        super(Kind.VARIABLE, type, size, valaddr, level);
    }

    public VariableOperand(Operand op) {
        super(op);
    }

    @Override
    public Operand emitLoadVal(CodeGenerator toCode) {
        switch (type) {
            case SIMPLEINT:
            case SIMPLEBOOL:
                toCode.emit(Opcode.LV);
                toCode.emit((byte) (getCurrLevel() - getLevel()));
                toCode.emit(valaddr);
                break;

            case SIMPLECHAR:
                toCode.emit(Opcode.LC);
                toCode.emit((byte) (getCurrLevel() - getLevel()));
                toCode.emit(valaddr);
                break;
        }
        return (new ValueOnStackOperand(this));
    }

    @Override
    public boolean emitAssign(CodeGenerator toCode, Operand destOp) {
        switch (destOp.getType()) {
            case SIMPLEINT:
            case SIMPLEBOOL:
                toCode.emit(Opcode.LV);
                toCode.emit((byte) (getCurrLevel() - getLevel()));
                toCode.emit(valaddr);
                toCode.emit(Opcode.STO);
                break;

            case SIMPLECHAR:
                //toCode.emit(Opcode.LC);
                // TODO: Implement assign for char
                break;
        }
        return true;
    }

    @Override
    public Operand emitLoadAddr(CodeGenerator toCode) {
        toCode.emit(Opcode.LA);
        toCode.emit((byte) (getCurrLevel() - getLevel()));
        toCode.emit(valaddr);

        return (new AddrOnStackOperand(this));
    }
}
