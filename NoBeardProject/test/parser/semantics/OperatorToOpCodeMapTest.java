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
package parser.semantics;

import machine.InstructionSet.Instruction;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.OperatorToOpCodeMap;
import scanner.Scanner;
import scanner.Scanner.Symbol;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class OperatorToOpCodeMapTest {
    
    public OperatorToOpCodeMapTest() {
    }
    
    /**
     * Test of getOpCode method, of class OperatorToOpCodeMap.
     */
    @Test
    public void testGetOpCode() {
        System.out.println("getOpCode");
        Scanner.Symbol symbol = Symbol.MINUS;
        Instruction expResult = Instruction.SUB;
        Instruction result = OperatorToOpCodeMap.getOpCode(symbol);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testIllegalOpCode() {
        Scanner.Symbol symbol = Symbol.CHAR;
        assertEquals(Instruction.NOP, OperatorToOpCodeMap.getOpCode(symbol));
    }
    
    @Test
    public void testGetOperand() {
        assertEquals(0, OperatorToOpCodeMap.getOperand(Symbol.LTH));
        assertEquals(1, OperatorToOpCodeMap.getOperand(Symbol.LEQ));
        assertEquals(2, OperatorToOpCodeMap.getOperand(Symbol.EQUALS));
        assertEquals(3, OperatorToOpCodeMap.getOperand(Symbol.NEQ));
        assertEquals(4, OperatorToOpCodeMap.getOperand(Symbol.GEQ));
        assertEquals(5, OperatorToOpCodeMap.getOperand(Symbol.GTH));
    }
    
    @Test
    public void testGetIllegalOperand() {
        assertEquals(-1, OperatorToOpCodeMap.getOperand(Symbol.NOSY));
    }
}
