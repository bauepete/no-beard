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
        return this;
    }

    @Override
    public void emitAssign(Code toCode, Operand destOp) {
        // nothing to be done;
    }

    @Override
    public Operand emitLoadAddr(Code toCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
