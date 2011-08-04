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
    public void emitAssign(Code toCode, Operand destOp) {
        // Nothing to do
    }

    @Override
    public Operand emitLoadAddr(Code toCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
