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
public class AddrOnStackOperand extends Operand {
    public AddrOnStackOperand(Operand op) {
        super(op);
        this.kind = OperandKind.ADDRONSTACK;
    }

    @Override
    public Operand emitLoadVal(Code toCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Operand emitLoadAddr(Code toCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void emitAssign(Code toCode, Operand destOp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
