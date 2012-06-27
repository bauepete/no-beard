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
public class UnitOperand extends Operand {
    UnitOperand(OperandType opType, int size, int valaddr, int level) {
        super(OperandKind.UNIT, opType, size, valaddr, level);
    }

    @Override
    public Operand emitLoadVal(Code toCode) {
        // nothing to be done;
        return new IllegalOperand();
    }

    @Override
    public boolean emitAssign(Code toCode, Operand destOp) {
        // nothing to be done;
        return super.emitAssign(toCode, destOp);
    }

    @Override
    public Operand emitLoadAddr(Code toCode) {
        return new IllegalOperand();
    }
    
}
