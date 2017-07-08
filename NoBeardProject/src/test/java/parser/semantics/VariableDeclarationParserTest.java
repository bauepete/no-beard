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
import parser.ParserFactory;
import parser.VariableDeclarationParser;
import parser.general.PutParserTestSetup;
import parser.general.VariableDeclarationParserTestSetup;
import symboltable.Operand;
import symboltable.SymbolTableEntry;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class VariableDeclarationParserTest {
    
    public VariableDeclarationParserTest() {
    }
    
    /**
     * Test simple int.
     */
    @Test
    public void testSymbolTableContentOfSimpleIntDeclaration() {
        VariableDeclarationParser instance = VariableDeclarationParserTestSetup.getSimpleIntTestSetup();
        instance.parse();
        SymbolTableEntry e = VariableDeclarationParserTestSetup.getSymListManager().findObject(0);
        assertEquals(Operand.Kind.VARIABLE, e.getKind());
        assertEquals(Operand.Type.SIMPLEINT, e.getType());
        assertEquals(4, e.getSize());
    }
    
    @Test
    public void testSymbolTableContentOfSimpleCharDeclaration() {
        VariableDeclarationParser instance = VariableDeclarationParserTestSetup.getSimpleCharTestSetup();
        instance.parse();
        SymbolTableEntry e = VariableDeclarationParserTestSetup.getSymListManager().findObject(0);
        assertEquals(Operand.Type.SIMPLECHAR, e.getType());
        assertEquals(1, e.getSize());
    }
    
    @Test
    public void testSymbolTableContentOfIntArrayDeclaration() {
        VariableDeclarationParser instance = VariableDeclarationParserTestSetup.getIntArrayTestSetup();
        instance.parse();
        SymbolTableEntry e = VariableDeclarationParserTestSetup.getSymListManager().findObject(0);
        assertEquals(Operand.Type.ARRAYINT, e.getType());
        assertEquals(42 * 4, e.getSize());
    }
    
    @Test
    public void testNameAlreadyDefined() {
        VariableDeclarationParser instance = VariableDeclarationParserTestSetup.getNameAlreadyDefinedSetup();
        assertFalse(instance.parse());
    }
    
    @Test
    public void testNegativeArraySize() {
        VariableDeclarationParser instance = VariableDeclarationParserTestSetup.getNegativeArraySizeSetup();
        assertFalse(instance.parse());
    }
    
    @Test
    public void testSimpleVariableDeclarationWithInitialization() {
        byte[] expectedCode = {
            Instruction.LA.getId(), 0, 0, 32,
            Instruction.LIT.getId(), 0, 17,
            Instruction.STO.getId()
        };
        VariableDeclarationParser instance = VariableDeclarationParserTestSetup.getDeclarationAndInitializationSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals(expectedCode, PutParserTestSetup.getCode().getByteCode());
    }
    
    @Test
    public void testStringVariableDeclarationWithInitialization() {
        byte[] expectedCode = {
            Instruction.LA.getId(), 0, 0, 32, // load address of x
            Instruction.LIT.getId(), 0, 0, // load address of string constant
            Instruction.LIT.getId(), 0, 11, // load length of string constant
            Instruction.ASSN.getId(), // bulk store characters to x
        };
        VariableDeclarationParser instance = VariableDeclarationParserTestSetup.getStringDeclarationAndInitializationSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals(expectedCode, ParserFactory.getCodeGenerator().getByteCode());
    }
}
