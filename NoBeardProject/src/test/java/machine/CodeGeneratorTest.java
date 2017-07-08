/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package machine;

import error.ErrorHandler;
import error.SourceCodeInfo;
import machine.InstructionSet.Instruction;
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
    
    private final int MEMORY_SIZE = 256;

    public CodeGeneratorTest() {
    }

    @Before
    public void setup() {
        errorHandler = new ErrorHandler(new FakeSourceCodeInfo());
        instance = new CodeGenerator(MEMORY_SIZE, errorHandler);
    }

    /**
     * Test of emit method, of class CodeGenerator.
     */
    @Test
    public void testEmitOp() {
        instance.emit(Instruction.ADD);

        assertEquals(1, instance.getPc());
        assertEquals(Instruction.ADD.getId(), instance.getCodeByte(0));
    }

    /**
     * Test of emit method, of class CodeGenerator.
     */
    @Test
    public void testEmitByte() {
        byte b = 1;

        instance.emit(Instruction.OUT);
        instance.emit(b);

        assertEquals(2, instance.getPc());
        assertEquals(1, instance.getCodeByte(1));
    }
    
    @Test
    public void testEmitByte255() {
        instance.emit((byte)255);
        assertEquals(255, Byte.toUnsignedInt(instance.getCodeByte(0)));
    }
    
    @Test
    public void testEmitByte256() {
        instance.emit((byte) 256);
        assertEquals(0, instance.getCodeByte(0));
    }

    /**
     * Test of emit method, of class CodeGenerator.
     */
    @Test
    public void testEmitHalfWord() {
        int halfWord = 42;

        instance.emit(Instruction.LIT);
        instance.emit(halfWord);

        assertEquals("PC", 3, instance.getPc());
        assertEquals("1st byte", 0, instance.getCodeByte(1));
        assertEquals("2nd byte", 42, instance.getCodeByte(2));
    }

    /**
     * Test emit by emitting max value of one byte.
     */
    @Test
    public void testEmitHalfWord255() {
        int halfWord = 255;

        instance.emit(Instruction.LIT);
        instance.emit(halfWord);

        assertEquals("PC", 3, instance.getPc());
        assertEquals("1st byte", 0, instance.getCodeByte(1));
        assertEquals("2nd byte", 255, instance.getCodeByte(2) & 0xff);
    }

    /**
     * Test emit by emitting min value of two bytes.
     */
    @Test
    public void testEmitHalfWord256() {
        int halfWord = 256;

        instance.emit(Instruction.LIT);
        instance.emit(halfWord);

        assertEquals("PC", 3, instance.getPc());
        assertEquals("1st byte", 1, instance.getCodeByte(1));
        assertEquals("2nd byte", 0, instance.getCodeByte(2) & 0xff);
    }

    /**
     * Test emit by emitting max value of two bytes.
     */
    @Test
    public void testEmitHalfWord65535() {
        int halfWord = 65535;

        instance.emit(Instruction.LIT);
        instance.emit(halfWord);

        assertEquals("PC", 3, instance.getPc());
        assertEquals("1st byte", 255, instance.getCodeByte(1) & 0xff);
        assertEquals("2nd byte", 255, instance.getCodeByte(2) & 0xff);
    }

    /**
     * Test of overflow when call emit(65536).
     */
    @Test
    public void testHalfwordOverflow() {
        instance.emit(65536);
        assertEquals(0, instance.getCodeByte(0));
    }

    /**
     * Test of fixup method, of class CodeGenerator.
     */
    @Test
    public void testFixup() {
        instance.emit(Instruction.INC);
        int fixupAddr = instance.getPc();
        instance.emit(0); // temp value
        instance.emit(Instruction.LA);
        instance.emit((byte) 0);
        instance.emit(2);
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
            Instruction.LA.getId(), 0, 0, 32,
            Instruction.LIT.getId(), 0, 42,
            Instruction.ADD.getId()
        };

        instance.emit(Instruction.LA);
        instance.emit((byte) 0);
        instance.emit(32);

        instance.emit(Instruction.LIT);
        instance.emit(42);

        instance.emit(Instruction.ADD);

        assertArrayEquals(expected, instance.getByteCode());
    }

    /**
     * Test of overflow of program storage.
     */
    @Test
    public void testProgramStorageOverflow() {
        CodeGenerator g = new CodeGenerator(7, errorHandler);
        g.emit(Instruction.LA);
        g.emit((byte) 0);
        g.emit(0);

        g.emit(Instruction.LIT);
        g.emit(17);

        assertEquals(0, errorHandler.getCount());

        g.emit(Instruction.STO);
        assertEquals(1, errorHandler.getCount());
        assertEquals(error.Error.ErrorType.PROGRAM_MEMORY_OVERFLOW.getNumber(), errorHandler.getLastError().getNumber());
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
