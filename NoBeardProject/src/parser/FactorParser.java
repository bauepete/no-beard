/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import nbm.CodeGenerator;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import scanner.StringToken;
import symlist.ConstantOperand;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class FactorParser extends OperandExportingParser {
    
    private int stringLength;
    private int stringAddress;

    public FactorParser(Scanner s, SymListManager sym, CodeGenerator c, ErrorHandler e) {
        super();
    }

    FactorParser() {

    }

    @Override
    public boolean parseOldStyle() {
        return true;
    }

    @Override
    public void parseSpecificPart() {
        switch (scanner.getCurrentToken().getSy()) {
            case IDENTIFIER:
                ReferenceParser p = ParserFactory.create(ReferenceParser.class);
                parseSymbol(p);
                sem(() -> exportedOperand = p.getOperand());
                break;

            case NUMBER:
                int val = parseNumber();
                sem(() -> exportedOperand = new ConstantOperand(Operand.OperandType.SIMPLEINT, 4, val, 0));
                break;

            case STRING:
                parseString();
                sem(() -> {
                    Operand.OperandType operandType = stringLength == 1 ? Operand.OperandType.SIMPLECHAR : Operand.OperandType.ARRAYCHAR;
                    exportedOperand = new ConstantOperand(operandType, stringLength, stringAddress, 0);
                });

                break;

            default:
                throwSymbolExpected("Identifier, number, true, false, not, or '('", scanner.getCurrentToken().getSy().toString());
        }
    }
    
    private void parseString() {
        parseSymbol(Symbol.STRING);
        StringToken st = (StringToken)getLastParsedToken();
        stringLength = st.getLength();
        stringAddress = st.getAddress();
    }
}
