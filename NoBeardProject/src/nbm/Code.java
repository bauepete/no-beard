/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nbm;

/**
 *
 * @author peter
 */
public class Code {
    
    private byte[] prog;
    private int pc;
    
    public Code() {
        prog = new byte[Nbm.getMAXPROG()];
    }
    
    public int getPc() {
        return pc;
    }
    
    public byte getCodeByte(int atAddr) {
        return prog[atAddr];
    }
    
    public int getCodeHalfWord(int atAddr) {
        return prog[atAddr] * 256 + prog[atAddr + 1];
    }
    
    public void emitOp(Nbm.Opcode op) {
        prog[pc] = op.byteCode();
        pc++;
    }
    
    public void emitByte(byte b) {
        prog[pc] = b;
        pc++;
        
    }
    
    public void emitHalfWord(int halfWord) {
        prog[pc] = (byte)(halfWord / 256);
        prog[pc + 1] = (byte)(halfWord % 256);
        pc += 2;
    }
    
    /** Writes a half word at a specific address into the program memory.
     * 
     * @param atAddr Address in program memory where to write
     * @param halfWord The half word to be written.
     */
    public void fixup(int atAddr, int halfWord) {
        prog[atAddr] = (byte)(halfWord / 256);
        prog[atAddr + 1] = (byte)(halfWord % 256);
    }
    
    public byte[] getByteCode() {
        return prog;
    }

}
