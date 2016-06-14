/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import nbm.CodeGenerator;
import nbm.Nbm.Opcode;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symlist.Operand;
import symlist.Operand.OperandType;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class IfStatParser extends Parser {

    public IfStatParser(Scanner scanner, SymListManager sym, CodeGenerator code, ErrorHandler e) {
        super(scanner, sym, code, e);
    }

    @Override
    public boolean parse() {
        if (!tokenIsA(Symbol.IF)) {
            return false;
        }
        ExprParser exprP = new ExprParser(scanner, sym, code, getErrorHandler());
        if (!exprP.parse()) {
            return false;
        }
        Operand op = exprP.getOperand();
        // cc
        if (!operandIsA(op, OperandType.SIMPLEBOOL)) {
            return false;
        }
        // sem
        op.emitLoadVal(code);
        code.emitOp(Opcode.FJMP);
        code.emitHalfWord(0);
        int jumpAddr = code.getPc() - 2;
        
        sym.newBlock();
        // endsem
        
        BlockParser blockP = new BlockParser(scanner, sym, code, sym.getCurrBlock(), getErrorHandler());
        if (!blockP.parse()) {
            return false;
        }
        // sem
        sym.endBlock();
        // endsem
        
        if (scanner.getCurrentToken().getSy() == Symbol.ELSE) {
            if (!tokenIsA(Symbol.ELSE)) {
                return false;
            }
            
            // sem
            code.emitOp(Opcode.JMP);
            code.emitHalfWord(0);
            code.fixup(jumpAddr, code.getPc());
            jumpAddr = code.getPc() - 2;
            
            sym.newBlock();
            // endsem
            
            if (!blockP.parse()) {
                return false;
            }
            
            // sem
            sym.endBlock();
        }
        code.fixup(jumpAddr, code.getPc());
        return true;
    }
}
