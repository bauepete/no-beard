/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nbm;

import error.ErrorHandler;
import error.SourceCodeInfo;
import org.junit.Ignore;
import nbm.NoBeardMachine.Opcode;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author peter
 */
public class CodeGeneratorTest {

    private CodeGenerator instance;
    private ErrorHandler errorHandler;
    
    public CodeGeneratorTest() {
    }
    
    @Before
    public void setup() {
        errorHandler = new ErrorHandler(new FakeSourceCodeInfo());
        instance = new CodeGenerator(256, errorHandler);
    }

    /**
     * Test of emitOp method, of class CodeGenerator.
     */
    @Test
    public void testEmitOp() {
        instance.emitOp(NoBeardMachine.Opcode.ADD);

        assertEquals(1, instance.getPc());
        assertEquals(NoBeardMachine.Opcode.ADD.byteCode(), instance.getCodeByte(0));
    }

    /**
     * Test of emitByte method, of class CodeGenerator.
     */
    @Test
    public void testEmitByte() {
        byte b = 1;

        instance.emitOp(Opcode.PUT);
        instance.emitByte(b);

        assertEquals(2, instance.getPc());
        assertEquals(1, instance.getCodeByte(1));
    }

    /**
     * Test of emitHalfWord method, of class CodeGenerator.
     */
    @Test
    public void testEmitHalfWord() {
        int halfWord = 42;

        instance.emitOp(Opcode.LIT);
        instance.emitHalfWord(halfWord);

        assertEquals("PC", 3, instance.getPc());
        assertEquals("1st byte", 0, instance.getCodeByte(1));
        assertEquals("2nd byte", 42, instance.getCodeByte(2));
    }

    /**
     * Test emitHalfWord by emitting max value of one byte.
     */
    @Test
    public void testEmitHalfWord255() {
        int halfWord = 255;

        instance.emitOp(Opcode.LIT);
        instance.emitHalfWord(halfWord);

        assertEquals("PC", 3, instance.getPc());
        assertEquals("1st byte", 0, instance.getCodeByte(1));
        assertEquals("2nd byte", 255, instance.getCodeByte(2) & 0xff);
    }

    /**
     * Test emitHalfWord by emitting min value of two bytes.
     */
    @Test
    public void testEmitHalfWord256() {
        int halfWord = 256;

        instance.emitOp(Opcode.LIT);
        instance.emitHalfWord(halfWord);

        assertEquals("PC", 3, instance.getPc());
        assertEquals("1st byte", 1, instance.getCodeByte(1));
        assertEquals("2nd byte", 0, instance.getCodeByte(2) & 0xff);
    }

    /**
     * Test emitHalfWord by emitting max value of two bytes.
     */
    @Test
    public void testEmitHalfWord65535() {
        int halfWord = 65535;

        instance.emitOp(Opcode.LIT);
        instance.emitHalfWord(halfWord);

        assertEquals("PC", 3, instance.getPc());
        assertEquals("1st byte", 255, instance.getCodeByte(1) & 0xff);
        assertEquals("2nd byte", 255, instance.getCodeByte(2) & 0xff);
    }

    /**
     * Test of fixup method, of class CodeGenerator.
     */
    @Test
    public void testFixup() {
        instance.emitOp(Opcode.INC);
        int fixupAddr = instance.getPc();
        instance.emitHalfWord(0); // temp value
        instance.emitOp(Opcode.LA);
        instance.emitByte((byte) 0);
        instance.emitHalfWord(2);
        instance.fixup(fixupAddr, 8);

        assertEquals("PC", 7, instance.getPc());
        assertEquals("Fixup Value", 8, instance.getCodeHalfWord(fixupAddr));
    }

    /**
     * Test getByteCode.
     */
    @Test
    public void testGetByteCode() {
        byte[] expected = {
            Opcode.LA.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 42,
            Opcode.ADD.byteCode()
        };

        instance.emitOp(Opcode.LA);
        instance.emitByte((byte) 0);
        instance.emitHalfWord(32);

        instance.emitOp(Opcode.LIT);
        instance.emitHalfWord(42);

        instance.emitOp(Opcode.ADD);

        assertCodeEquals("Prog ", expected, instance.getByteCode());
    }

    /**
     * Test of overflow of program storage.
     */
    @Test
    public void testProgramStorageOverflow() {
        CodeGenerator g = new CodeGenerator(7, errorHandler);
        g.emitOp(Opcode.LA);
        g.emitByte((byte)0);
        g.emitHalfWord(0);
        
        g.emitOp(Opcode.LIT);
        g.emitHalfWord(17);
        
        assertEquals(0, errorHandler.getCount());
        
        g.emitOp(Opcode.STO);
        assertEquals(1, errorHandler.getCount());
        assertEquals(error.Error.ErrorType.PROGRAM_MEMORY_OVERFLOW.getNumber(), errorHandler.getLastError().getNumber());
    }

    /**
     * Test of overflow when call emitHalfWord(65536).
     */
    @Ignore
    @Test
    public void testHalfwordOverflow() {
        // TODO implement testHalfwordOverflow.
        fail("Not implemented yet");
    }

    /**
     * Test of illegal address when fixup.
     */
    @Ignore
    @Test
    public void testIllegalAddressFixup() {
        // TODO implement testIllegalAddressFixup.
        fail("Not implemented yet");
    }

    private void assertCodeEquals(String msg, byte[] exp, byte[] act) {
        for (int i = 0; i < Math.min(exp.length, act.length); i++) {
            assertEquals("Byte " + i, exp[i], act[i]);
        }
    }

    private static class FakeSourceCodeInfo implements SourceCodeInfo {
        @Override
        public int getCurrentCol() {
            return 0;
        }

        @Override
        public int getCurrentLine() {
            return 1;
        }
    }
}
