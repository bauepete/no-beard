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
import machine.InstructionSet.Instruction;
import scanner.Scanner;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class OperatorToOpCodeMap {
    private final static HashMap<Scanner.Symbol, Instruction> mulOpSymbolToOpCodeMap;
    static {
        mulOpSymbolToOpCodeMap = new HashMap<>();
        // Relational operators
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.LTH, Instruction.REL);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.LEQ, Instruction.REL);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.EQUALS, Instruction.REL);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.NEQ, Instruction.REL);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.GEQ, Instruction.REL);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.GTH, Instruction.REL);
        // And operators
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.PLUS, Instruction.ADD);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.MINUS, Instruction.SUB);
        
        // Multiplication operators
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.TIMES, Instruction.MUL);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.DIV, Instruction.DIV);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.MOD, Instruction.MOD);
    }
    
    public static Instruction getOpCode(Scanner.Symbol symbol) {
        if (mulOpSymbolToOpCodeMap.containsKey(symbol))
            return mulOpSymbolToOpCodeMap.get(symbol);
        else
            return Instruction.NOP;
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
