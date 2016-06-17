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
import nbm.Nbm;
import scanner.Scanner;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class OperatorToOpCodeMap {
    private final static HashMap<Scanner.Symbol, Nbm.Opcode> mulOpSymbolToOpCodeMap;
    static {
        mulOpSymbolToOpCodeMap = new HashMap<>();
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.TIMES, Nbm.Opcode.MUL);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.DIV, Nbm.Opcode.DIV);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.MOD, Nbm.Opcode.MOD);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.PLUS, Nbm.Opcode.ADD);
        mulOpSymbolToOpCodeMap.put(Scanner.Symbol.MINUS, Nbm.Opcode.SUB);
    }
    
    public static Nbm.Opcode getOpCode(Scanner.Symbol symbol) {
        return mulOpSymbolToOpCodeMap.get(symbol);
    }
}
