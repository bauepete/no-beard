/*
 * Copyright ©2015, 2016. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
 * Department of Informatics and Media Technique, HTBLA Leonding,
 * Limesstr. 12 - 14, 4060 Leonding, AUSTRIA. All Rights Reserved. Permission
 * to use, copy, modify, and distribute this software and its documentation
 * for educational, research, and not-for-profit purposes, without fee and
 * without a signed licensing agreement, is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies, modifications, and distributions. Contact the Head of
 * Informatics and Media Technique, HTBLA Leonding, Limesstr. 12 - 14,
 * 4060 Leonding, Austria, for commercial licensing opportunities.
 * 
 * IN NO EVENT SHALL HTBLA LEONDING BE LIABLE TO ANY PARTY FOR DIRECT,
 * INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST
 * PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF HTBLA LEONDING HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * HTBLA LEONDING SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE. THE SOFTWARE AND ACCOMPANYING DOCUMENTATION, IF ANY,
 * PROVIDED HEREUNDER IS PROVIDED "AS IS". HTBLA LEONDING HAS NO OBLIGATION
 * TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */
package parser;

import error.ErrorHandler;
import java.util.HashMap;
import nbm.CodeGenerator;
import nbm.Nbm.Opcode;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import scanner.Token;
import symlist.Operand;
import symlist.Operand.OperandType;
import symlist.SymListManager;
import symlist.ValueOnStackOperand;

/**
 *
 * @author peter
 */
public class ExprParser extends Parser {

    private Operand op;
    private byte ror;

    public ExprParser(Scanner scanner, SymListManager sym, CodeGenerator code, ErrorHandler e) {
        super(scanner, sym, code, e);
    }

    @Override
    public boolean parseOldStyle() {

        SimpleExpressionParser simExprP = new SimpleExpressionParser(scanner, sym, code, getErrorHandler());
        if (!simExprP.parseOldStyle()) {
            return false;
        }
        op = simExprP.getOperand();

        if (tokenIsARelop(scanner.getCurrentToken())) {
            
            if (!relOp()) {
                return false;
            }
            
            // sem
            switch (op.getType()) {
                case SIMPLEBOOL:
                case SIMPLECHAR:
                case SIMPLEINT:
                    op.emitLoadVal(code);
                    break;
                    
                case ARRAYBOOL:
                case ARRAYCHAR:
                case ARRAYINT:
                    op.emitLoadAddr(code);
                    break;
            }
            // endsem
            
            if (!simExprP.parseOldStyle()) {
                return false;
            }
            Operand op2 = simExprP.getOperand();
            // cc
            if (op.getSize() == Operand.UNDEFSIZE || op2.getSize() == Operand.UNDEFSIZE ||
                    op.getType() != op2.getType() || op.getSize() != op2.getSize()) {
                String[] tList = {op.getType().toString(), op2.getType().toString()};
                getErrorHandler().raise(new error.Error(error.Error.ErrorType.INCOMPATIBLE_TYPES, tList));
                return false;
            }
            // endcc
            
            // sem
            switch (op2.getType()) {
                case SIMPLEBOOL:
                case SIMPLECHAR:
                case SIMPLEINT:
                    op2.emitLoadVal(code);
                    code.emitOp(Opcode.REL);
                    code.emitByte(ror);
                    break;
                    
                default:
                    int line = scanner.getCurrentLine();
                    String[] tList = {
                        OperandType.SIMPLEBOOL.toString(), OperandType.SIMPLECHAR.toString(),
                        OperandType.SIMPLEINT.toString()
                    };
                    getErrorHandler().raise(new error.Error(error.Error.ErrorType.TYPES_EXPECTED, tList));
                    break;
            }
            op = new ValueOnStackOperand(Operand.OperandType.SIMPLEBOOL, 4, op.getValaddr(), op.getCurrLevel());
            // ensem
        }
        return true;
    }

    public Operand getOperand() {
        return op;
    }

    private boolean tokenIsARelop(Token currentToken) {
        return (currentToken.getSy() == Symbol.LTH
                || currentToken.getSy() == Symbol.GTH
                || currentToken.getSy() == Symbol.LEQ
                || currentToken.getSy() == Symbol.GEQ
                || currentToken.getSy() == Symbol.EQUALS
                || currentToken.getSy() == Symbol.NEQ);
    }
    
    private boolean relOp() {
        HashMap<Scanner.Symbol, Integer> rop = 
                new HashMap<Scanner.Symbol, Integer>(){{
                    put(Symbol.LTH, 0);
                    put(Symbol.LEQ, 1);
                    put(Symbol.EQUALS, 2);
                    put(Symbol.NEQ, 3);
                    put(Symbol.GEQ, 4);
                    put(Symbol.GTH, 5);
                }};
        
        if (!rop.containsKey(scanner.getCurrentToken().getSy())) {
            return false;
        }
        
        int r = rop.get(scanner.getCurrentToken().getSy());
        ror = (byte) r;
        scanner.nextToken();
        return true;
    }

    @Override
    public void parseSpecificPart() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
