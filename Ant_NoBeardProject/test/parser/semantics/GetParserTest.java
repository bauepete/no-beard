/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import machine.InstructionSet.Instruction;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.GetParser;
import parser.general.GetParserTestSetup;

/**
 *
 * @author peter
 */
public class GetParserTest {
    
    public GetParserTest() {
    }

    /**
     * Test of parseSpecificPart method, of class GetParser.
     */
    @Test
    public void testGetInt() {
        byte[] expectedCode = {
            Instruction.LA.getId(), 0, 32,
            Instruction.LA.getId(), 0, 36,
            Instruction.IN.getId(), 0,
            
        };
        GetParser instance = GetParserTestSetup.getGetIntSetup();
        assertTrue(instance.parse());
        
    }
    
}
