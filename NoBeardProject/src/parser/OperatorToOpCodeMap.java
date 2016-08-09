/*
 * Copyright ©2015. Created by P. Bauer (p.bauer@htl-leonding.ac.at), Department
 * of Informatics and Media Technique, HTBLA Leonding, Limesstr. 12 - 14,
 * 4060 Leonding, AUSTRIA. All Rights Reserved. Permission to use, copy, modify,
 * and distribute this software and its documentation for educational,
 * research, and not-for-profit purposes, without fee and without a signed
 * licensing agreement, is hereby granted, provided that the above copyright
 * notice, this paragraph and the following two paragraphs appear in all
 * copies, modifications, and distributions. Contact the Head of Informatics,
 * Media Technique and Design, HTBLA Leonding, Limesstr. 12 - 14, 4060 Leonding,
 * Austria, for commercial licensing opportunities.
 * 
 * IN NO EVENT SHALL HTBLA LEONDING BE  LIABLE TO ANY PARTY FOR DIRECT,
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

import java.util.HashMap;
import nbm.ControlUnit.Opcode;
import scanner.Scanner;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class OperatorToOpCodeMap {
    private final static HashMap<Scanner.Symbol, Opcode> mulOpSymbolToOpCodeMap;
    static {
        mulOpSymbolToOpCodeMap = new HashMap<>();
        // Relational operators
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.LTH, Opcode.REL);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.LEQ, Opcode.REL);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.EQUALS, Opcode.REL);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.NEQ, Opcode.REL);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.GEQ, Opcode.REL);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.GTH, Opcode.REL);
        // And operators
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.PLUS, Opcode.ADD);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.MINUS, Opcode.SUB);
        
        // Multiplication operators
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.TIMES, Opcode.MUL);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.DIV, Opcode.DIV);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.MOD, Opcode.MOD);
    }
    
    public static Opcode getOpCode(Scanner.Symbol symbol) {
        if (mulOpSymbolToOpCodeMap.containsKey(symbol))
            return mulOpSymbolToOpCodeMap.get(symbol);
        else
            return Opcode.NOP;
    }

    public static byte getOperand(Scanner.Symbol symbol) {
        switch (symbol) {
            case LTH:
                return 0;
            case LEQ:
                return 1;
            case EQUALS:
                return 2;
            case NEQ:
                return 3;
            case GEQ:
                return 4;
            case GTH:
                return 5;
        }
        return -1;
    }
}
