/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nbm;

import org.junit.Ignore;
import nbm.Nbm.Opcode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class CodeTest {

    public CodeTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of emitOp method, of class Code.
     */
    @Test
    public void testEmitOp() {
        System.out.println("testEmitOp");
        Code instance = new Code(256);

        instance.emitOp(Nbm.Opcode.ADD);

        assertEquals(1, instance.getPc());
        assertEquals(Nbm.Opcode.ADD.byteCode(), instance.getCodeByte(0));
    }

    /**
     * Test of emitByte method, of class Code.
     */
    @Test
    public void testEmitByte() {
        System.out.println("emitByte");
        byte b = 1;
        Code instance = new Code(256);

        instance.emitOp(Opcode.PUT);
        instance.emitByte(b);

        assertEquals(2, instance.getPc());
        assertEquals(1, instance.getCodeByte(1));
    }

    /**
     * Test of emitHalfWord method, of class Code.
     */
    @Test
    public void testEmitHalfWord() {
        System.out.println("emitHalfWord");
        int halfWord = 42;
        Code instance = new Code(256);

        instance.emitOp(Opcode.LIT);
        instance.emitHalfWord(halfWord);

        assertEquals("PC", 3, instance.getPc());
        assertEquals("1st byte", 0, instance.getCodeByte(1));
        assertEquals("2nd byte", 42, instance.getCodeByte(2));
    }

    /**
     * Test of emitHalfWord method, of class Code.
     */
    @Test
    public void testEmitHalfWord255() {
        System.out.println("emitHalfWord255");
        int halfWord = 255;
        Code instance = new Code(256);

        instance.emitOp(Opcode.LIT);
        instance.emitHalfWord(halfWord);

        assertEquals("PC", 3, instance.getPc());
        assertEquals("1st byte", 0, instance.getCodeByte(1));
        assertEquals("2nd byte", 255, instance.getCodeByte(2) & 0xff);
    }

    /**
     * Test of emitHalfWord method, of class Code.
     */
    @Test
    public void testEmitHalfWord256() {
        System.out.println("emitHalfWord256");
        int halfWord = 256;
        Code instance = new Code(256);

        instance.emitOp(Opcode.LIT);
        instance.emitHalfWord(halfWord);

        assertEquals("PC", 3, instance.getPc());
        assertEquals("1st byte", 1, instance.getCodeByte(1));
        assertEquals("2nd byte", 0, instance.getCodeByte(2) & 0xff);
    }

    /**
     * Test of emitHalfWord method, of class Code.
     */
    @Test
    public void testEmitHalfWord65535() {
        System.out.println("emitHalfWord65535");
        int halfWord = 65535;
        Code instance = new Code(256);

        instance.emitOp(Opcode.LIT);
        instance.emitHalfWord(halfWord);

        assertEquals("PC", 3, instance.getPc());
        assertEquals("1st byte", 255, instance.getCodeByte(1) & 0xff);
        assertEquals("2nd byte", 255, instance.getCodeByte(2) & 0xff);
    }

    /**
     * Test of fixup method, of class Code.
     */
    @Test
    public void testFixup() {
        System.out.println("fixup");
        int atAddr = 0;
        int halfWord = 0;
        Code instance = new Code(256);

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
     * Test of overflow of program storage.
     */
    @Test
    public void testGetByteCode() {
        System.out.println("getByteCode");

        Code instance = new Code(256);
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
    @Ignore
    @Test
    public void testProgramStorageOverflow() {
        // TODO implement case where program storage would be exceeded.
        fail("Not implemented yet");
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
}
