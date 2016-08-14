/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symboltable;

import machine.CodeGenerator;

/**
 *
 * @author peter
 */
public class AddrOnStackOperand extends Operand {
    public AddrOnStackOperand(Operand op) {
        super(op);
        this.kind = Kind.ADDRONSTACK;
    }

    @Override
    public Operand emitLoadVal(CodeGenerator toCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Operand emitLoadAddr(CodeGenerator toCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean emitAssign(CodeGenerator toCode, Operand destOp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
