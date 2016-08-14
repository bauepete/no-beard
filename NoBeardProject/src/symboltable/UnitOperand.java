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
public class UnitOperand extends Operand {
    UnitOperand(Type opType, int size, int valaddr, int level) {
        super(Kind.UNIT, opType, size, valaddr, level);
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
