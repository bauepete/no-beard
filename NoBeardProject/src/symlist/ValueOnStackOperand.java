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
    }
        
    @Override
    public Operand emitLoadVal(Code toCode) {
        return new ValueOnStackOperand(this);
    }

    @Override
    public void emitAssign(Code toCode, Operand destOp) {
        switch(destOp.getType()) {
            case SIMPLEINT:
            case SIMPLEBOOL:
                toCode.emitOp(Opcode.STO);
                break;
                
            case SIMPLECHAR:
                //TODO: Implement STC
                //toCode.emitOp(Opcode.STC);
        }
    }

    @Override
    public Operand emitLoadAddr(Code toCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
