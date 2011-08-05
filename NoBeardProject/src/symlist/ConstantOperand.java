/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import error.ErrorHandler;
import error.semerr.SemErr;
import nbm.Code;
import nbm.Nbm.Opcode;
import symlist.Operand.OperandKind;
import symlist.Operand.OperandType;

/**
 *
 * @author peter
 */
public class ConstantOperand extends Operand {
    
    public ConstantOperand(OperandType type, int size, int valaddr, int level) {
        super(OperandKind.CONSTANT, type, size, valaddr, level);
    }
    
    public ConstantOperand(Operand op) {
        super(op);
    }
    
    @Override
    public Operand emitLoadVal(Code toCode) {
        switch(type) {
            case SIMPLEINT:
            case SIMPLEBOOL:
                toCode.emitOp(Opcode.LIT);
                toCode.emitHalfWord(valaddr);
                break;
                
            case SIMPLECHAR:
                toCode.emitOp(Opcode.LIT);
                toCode.emitHalfWord(getStringStorage(valaddr));
                break;
        }
    return (new ValueOnStackOperand(this));
    }

    @Override
    public void emitAssign(Code toCode, Operand destOp) {
        switch (destOp.getType()) {
            case SIMPLEINT:
            case SIMPLEBOOL:
                toCode.emitOp(Opcode.LIT);
                toCode.emitHalfWord(valaddr);
                toCode.emitOp(Opcode.STO);
                break;
                
            case SIMPLECHAR:
                toCode.emitOp(Opcode.LIT);
                toCode.emitHalfWord(getStringStorage(valaddr));
                toCode.emitOp(Opcode.STC);
                break;
                
            default:
                ErrorHandler.getInstance().raise(new SemErr(99, "Can't assign to given type" + destOp.getType().toString(), 99));
        }
    }

    @Override
    public Operand emitLoadAddr(Code toCode) {
        if (getType() == OperandType.SIMPLECHAR || getType() == OperandType.ARRAYCHAR) {
            toCode.emitOp(Opcode.LIT);
            toCode.emitHalfWord(valaddr);
        }
        
        return (new AddrOnStackOperand(this));
    }
}
