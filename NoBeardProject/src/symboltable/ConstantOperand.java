/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symboltable;

import error.Error.ErrorType;
import nbm.CodeGenerator;
import nbm.Nbm.Opcode;
import symboltable.Operand.Kind;
import symboltable.Operand.Type;

/**
 *
 * @author peter
 */
public class ConstantOperand extends Operand {
    
    public ConstantOperand(Type type, int size, int valaddr, int level) {
        super(Kind.CONSTANT, type, size, valaddr, level);
    }
    
    public ConstantOperand(Operand op) {
        super(op);
    }
    
    @Override
    public Operand emitLoadVal(CodeGenerator toCode) {
        if (super.emitLoadVal(toCode) == null) {
            return new IllegalOperand();
        }
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
    public boolean emitAssign(CodeGenerator toCode, Operand destOp) {
        if (!super.emitAssign(toCode, destOp)) {
            return false;
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
                String[] tList = {Type.SIMPLEBOOL.toString(), Type.SIMPLECHAR.toString(),
                    Type.SIMPLEINT.toString(), Type.ARRAYCHAR.toString()};
                errorHandler().throwTypesExpected(tList);
                return false;
        }
        return true;
    }

    @Override
    public Operand emitLoadAddr(CodeGenerator toCode) {
        if (super.emitLoadAddr(toCode) == null) {
            return new IllegalOperand();
        }
        
        Operand returnedOp;
        
        if (getType() == Type.SIMPLECHAR || getType() == Type.ARRAYCHAR) {
            toCode.emitOp(Opcode.LIT);
            toCode.emitHalfWord(valaddr);
            returnedOp = new AddrOnStackOperand(this);
        } else {
            String[] tList = {Type.SIMPLECHAR.toString(), Type.ARRAYCHAR.toString()};
            errorHandler().throwTypesExpected(tList);
            returnedOp = new IllegalOperand();
        }
        return returnedOp;
    }
}
