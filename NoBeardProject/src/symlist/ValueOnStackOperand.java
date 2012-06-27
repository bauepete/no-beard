/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import nbm.Code;
import nbm.Nbm.Opcode;

/**
 *
 * @author peter
 */
public class ValueOnStackOperand extends Operand {

    public ValueOnStackOperand(OperandType type, int size, int valaddr, int level) {
        super(OperandKind.VALONSTACK, type, size, valaddr, level);
    }

    public ValueOnStackOperand(Operand op) {
        super(op);
        this.kind = OperandKind.VALONSTACK;
    }
        
    @Override
    public Operand emitLoadVal(Code toCode) {
        return new ValueOnStackOperand(this);
    }

    @Override
    public boolean emitAssign(Code toCode, Operand destOp) {
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
                
            case ARRAYCHAR:
                toCode.emitOp(Opcode.LIT);
                toCode.emitHalfWord(valaddr);
                toCode.emitOp(Opcode.ASSN);
                break;
        }
        return true;
    }

    @Override
    public Operand emitLoadAddr(Code toCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
