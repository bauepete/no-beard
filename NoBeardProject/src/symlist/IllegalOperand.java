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
public class IllegalOperand extends Operand {

    public IllegalOperand() {
        super(OperandKind.ILLEGAL, OperandType.ERRORTYPE, 0, 0, 0);
    }

    @Override
    public Operand emitLoadVal(Code toCode) {
        return this;
    }

    @Override
    public boolean emitAssign(Code toCode, Operand destOp) {
        // Nothing to do
        return super.emitAssign(toCode, destOp);
    }

    @Override
    public Operand emitLoadAddr(Code toCode) {
        return this;
    }
}
