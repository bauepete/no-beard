/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import compiler.NoBeardCompiler;
import machine.CodeGenerator;
import machine.InstructionSet.Instruction;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.NoBeardParser;
import parser.Parser;
import parser.ParserFactory;
import parser.general.NoBeardParserTestSetup;

/**
 *
 * @author peter
 */
public class NoBeardParserTest {

    private NoBeardCompiler comp;
    private CodeGenerator code;
    private NoBeardParser p;

    public NoBeardParserTest() {
    }

    @Test
    public void testParseEmpty() {
        byte[] expected = {
            Instruction.INC.getId(), 0, 0,
            Instruction.HALT.getId()
        };

        Parser instance = NoBeardParserTestSetup.getEmptyProgramSetup();
        assertTrue("True expected", instance.parse());
        assertCodeEquals("Code ", expected, ParserFactory.getCodeGenerator().getByteCode());
    }

    /**
     * Test that block identifiers do not match at begin and end.
     */
    @Test
    public void testBlockIdentMismatch() {
        Parser instance = NoBeardParserTestSetup.getBlockIdentifierNameMismatch();

        assertFalse("False expected", instance.parse());
        assertEquals(error.Error.ErrorType.BLOCK_NAME_MISSMATCH.getNumber(), ParserFactory.getErrorHandler().getLastError().getNumber());
    }

    private void assertCodeEquals(String msg, byte[] exp, byte[] act) {
        AssemblerCodeChecker.assertCodeEquals(msg, exp, act);
    }
}
