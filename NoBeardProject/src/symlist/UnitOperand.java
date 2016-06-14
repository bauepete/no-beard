/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import nbm.CodeGenerator;

/**
 *
 * @author peter
 */
public class UnitOperand extends Operand {
    UnitOperand(OperandType opType, int size, int valaddr, int level) {
        super(OperandKind.UNIT, opType, size, valaddr, level);
    }

    @Override
    public Operand emitLoadVal(CodeGenerator toCode) {
        // nothing to be done;
        return new IllegalOperand();
    }

    @Override
    public boolean emitAssign(CodeGenerator toCode, Operand destOp) {
        // nothing to be done;
        return super.emitAssign(toCode, destOp);
    }

    @Override
    public Operand emitLoadAddr(CodeGenerator toCode) {
        return new IllegalOperand();
    }
    
}
