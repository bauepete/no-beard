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
    protected void parseSpecificPart() {
        switch (scanner.getCurrentToken().getSy()) {
            case IDENTIFIER:
                ReferenceParser p = ParserFactory.create(ReferenceParser.class);
                parseSymbol(p);
                sem(() -> exportedOperand = p.getOperand());
                break;

            case NUMBER:
                int val = parseNumber();
                sem(() -> exportedOperand = new ConstantOperand(Operand.Type.SIMPLEINT, 4, val, 0));
                break;

            case STRING:
                parseString();
                sem(() -> {
                    Operand.Type operandType = stringLength == 1 ? Operand.Type.SIMPLECHAR : Operand.Type.ARRAYCHAR;
                    exportedOperand = new ConstantOperand(operandType, stringLength, stringAddress, 0);
                });

                break;
                
            case LPAR:
                parseExpression();
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
    
    private void parseExpression() {
        parseSymbol(Symbol.LPAR);
        OperandExportingParser p = ParserFactory.create(ExpressionParser.class);
        parseSymbol(p);
        sem(() -> exportedOperand = p.getOperand());
        parseSymbol(Symbol.RPAR);
    }
}
