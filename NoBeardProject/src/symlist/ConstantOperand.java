/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import error.ErrorHandler;
import error.SemErr;
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
        if (destOp.getKind() != OperandKind.ADDRONSTACK) {
            errorHandler().raise(new SemErr().new IllegalOperand());
            return;
        }
        
        if (!typesOk(destOp)) {
            SemErr x = new SemErr().new BlockNameMismatch(null, null);
            errorHandler().raise(new SemErr().new IncompatibleTypes(getType().toString(), destOp.getType().toString()));
            return;
        }
        
        if (!srcKindOk()) {
            errorHandler().raise(new SemErr().new IllegalOperand());
            return;
        }
        
        
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
                
            case ARRAYCHAR:
                toCode.emitOp(Opcode.LIT);
                toCode.emitHalfWord(valaddr);
                toCode.emitOp(Opcode.LIT);
                toCode.emitHalfWord(destOp.getSize());
                toCode.emitOp(Opcode.ASSN);
                break;
                
            default:
                ErrorHandler.getInstance().raise(new SemErr().new IncompatibleTypes(getType().toString(), destOp.getType().toString()));
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
    
    private boolean typesOk(Operand destOp) {
        return (destOp.getType() != OperandType.ERRORTYPE && destOp.getType() != OperandType.VOID &&
                getType() != OperandType.ERRORTYPE && getType() != OperandType.VOID &&
                getType() == destOp.getType());
    }
    
    private boolean srcKindOk() {
        return (getKind() != OperandKind.ILLEGAL && getKind() != OperandKind.ANONYMOUSBLOCK &&
                getKind() != OperandKind.FUNCTION && getKind() != OperandKind.UNIT);
    }
}
