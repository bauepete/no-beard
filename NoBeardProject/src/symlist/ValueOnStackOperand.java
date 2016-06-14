/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import error.Error;
import nbm.CodeGenerator;
import nbm.Nbm.Opcode;

/**
 *
 * @author peter
 */
public class ValueOnStackOperand extends Operand {

    public ValueOnStackOperand(OperandType type, int size, int valaddr, int level) {
        super(OperandKind.VALUEONSTACK, type, size, valaddr, level);
    }

    public ValueOnStackOperand(Operand op) {
        super(op);
        this.kind = OperandKind.VALUEONSTACK;
    }
        
    @Override
    public Operand emitLoadVal(CodeGenerator toCode) {
        return new ValueOnStackOperand(this);
    }

    @Override
    public boolean emitAssign(CodeGenerator toCode, Operand destOp) {
        if (!super.emitAssign(toCode, destOp)) {
            return false;
        }
        switch(destOp.getType()) {
            case SIMPLEINT:
            case SIMPLEBOOL:
                toCode.emitOp(Opcode.STO);
                break;
                
            case SIMPLECHAR:
                toCode.emitOp(Opcode.STC);
                break;
                
            default:
                String[] tList = {OperandType.SIMPLEBOOL.toString(), OperandType.SIMPLECHAR.toString(), OperandType.SIMPLEINT.toString()};
                errorHandler().raise(new Error(Error.ErrorType.TYPE_EXPECTED, tList));
                return false;
        }
        return true;
    }

    @Override
    public Operand emitLoadAddr(CodeGenerator toCode) {
        return new IllegalOperand();
    }
    
}
