/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nbm;

import java.util.Arrays;

/**
 *
 * @author peter
 */
public class Nbm {

    public enum MachineState {

        RUN, STOP, ERROR
    }

    public enum Opcode {

        NOP, LIT, LA, LV, LC, STO, STC, ASSN, NEG, ADD, SUB, MUL, DIV, MOD,
        NOT, REL, FJMP, TJMP, JMP, PUT, INC, HALT;

        public byte byteCode() {
            return (byte) this.ordinal();
        }
    }
    /// Returned from getStackTopValue() if stack of currently running
    /// function is empty.
    public static final int STACKEMPTY = -1;

    // --------------------- Locally used data -----------------------------
    private static final int MAX_PROG = 1024;   // Size of program memory
    private static final int MAX_DATA = 1024;    // Size of data memory
    private static final int LINKAREA = 28;    // Space to store house keeping data when functions are called
    private static final int WORDSIZE = 4;     // Size of one word in bytes
    private final byte[] prog;
    private final byte[] dat;
    private int pc;             // Program counter
    private int db;             // Pointer to first byte of frame stack of currently running function
    private int db_end;         // Pointer to the last byte of frame stack (db + LINKAREA + sizeof(local variables))
    private int top;            // Pointer to the last used byte in frame stack
    private MachineState ms;
    private final Instruction[] instructionMap = {
        new NoInstr(), new Lit(), new La(), new Lv(), new Lc(), new Sto(), new Stc(), new Assn(),
        new Neg(), new Add(), new Sub(), new Mul(), new Div(), new Mod(),
        new Not(), new Rel(), new Fjmp(), new Tjmp(), new Jmp(),
        new Put(), new Inc(), new Halt()};

    // -------------------------- Instruction classes ----------------------
    /// Command
    interface Instruction {

        public void exec();
    }

    class NoInstr implements Instruction {

        @Override
        public void exec() {
            ms = MachineState.ERROR;
        }
    }

    class Lit implements Instruction {

        @Override
        public void exec() {
            int n = getProgHalfWord(pc + 1);
            pc += 3;
            push(n);

        }
    }

    class La implements Instruction {

        @Override
        public void exec() {
            byte d = prog[pc + 1];
            int a = getProgHalfWord(pc + 2);
            pc += 4;

            int base = db;
            for (int i = 0; i < d; i++) {
                base = getDatWord(base);
            }
            push(base + a);
        }
    }

    class Lv implements Instruction {

        @Override
        public void exec() {
            byte d = prog[pc + 1];
            int a = getProgHalfWord(pc + 2);
            pc += 4;

            int base = db;
            for (int i = 0; i < d; i++) {
                base = getDatWord(base);
            }
            int adr = base + a;
            push(getDatWord(adr));
        }
    }

    class Lc implements Instruction {

        @Override
        public void exec() {
            byte d = prog[pc + 1];
            int a = getProgHalfWord(pc + 2);
            pc += 4;

            int base = db;
            for (int i = 0; i < d; i++) {
                base = getDatWord(base);
            }
            // TODO: fill character with 0s
            push(getDatWord(base + a));
        }
    }

    class Sto implements Instruction {

        @Override
        public void exec() {
            pc++;
            int x = pop();
            int a = pop();
            setDatWord(a, x);
        }
    }

    class Stc implements Instruction {

        @Override
        public void exec() {
            pc++;
            int x = pop();
            int a = pop();
            // TODO: only the rightmost character may be written to dat
            setDatWord(a, x);
        }
    }

    class Assn implements Instruction {

        @Override
        public void exec() {
            pc++;
            int n = pop();
            int y = pop();
            int x = pop();
            for (int i = 0; i < n; i++) {
                dat[x + i] = dat[y + i];
            }
        }
    }

    class Neg implements Instruction {

        @Override
        public void exec() {
            pc++;
            int x = pop();
            push(-x);
        }
    }

    class Add implements Instruction {

        @Override
        public void exec() {
            pc++;

            int y = pop();
            int x = pop();
            push(x + y);
        }
    }

    class Sub implements Instruction {

        @Override
        public void exec() {
            pc++;

            int y = pop();
            int x = pop();
            push(x - y);
        }
    }

    class Mul implements Instruction {

        @Override
        public void exec() {
            pc++;

            int y = pop();
            int x = pop();
            push(x * y);
        }
    }

    class Div implements Instruction {

        @Override
        public void exec() {
            pc++;

            int y = pop();
            int x = pop();
            push(x / y);
        }
    }

    class Mod implements Instruction {

        @Override
        public void exec() {
            pc++;

            int y = pop();
            int x = pop();
            push(x % y);
        }
    }

    class Not implements Instruction {

        @Override
        public void exec() {
            int x = pop();
            push(x == 1 ? 0 : 1);
        }

    }

    class Rel implements Instruction {

        @Override
        public void exec() {
            byte r = prog[pc + 1];
            pc += 2;

            int y = pop();
            int x = pop();

            switch (r) {
                case 0:
                    push(x < y ? 1 : 0);
                    break;

                case 1:
                    push(x <= y ? 1 : 0);
                    break;

                case 2:
                    push(x == y ? 1 : 0);
                    break;

                case 3:
                    push(x != y ? 1 : 0);
                    break;

                case 4:
                    push(x >= y ? 1 : 0);
                    break;

                case 5:
                    push(x > y ? 1 : 0);
                    break;
            }
        }

    }

    class Fjmp implements Instruction {

        @Override
        public void exec() {
            int b = getProgHalfWord(pc + 1);
            pc += 3;

            int x = pop();
            if (x == 0) {
                pc = b;
            }
        }

    }

    class Tjmp implements Instruction {

        @Override
        public void exec() {
            int b = getProgHalfWord(pc + 1);
            pc += 3;

            int x = pop();
            if (x == 1) {
                pc = b;
            }
        }

    }

    class Jmp implements Instruction {

        @Override
        public void exec() {
            int b = getProgHalfWord(pc + 1);
            pc = b;
        }

    }

    class Put implements Instruction {

        @Override
        public void exec() {
            byte s = prog[pc + 1];
            pc += 2;

            switch (s) {
                case 0:
                    outputInt();
                    break;

                case 1:
                    outputChar();
                    break;

                case 2:
                    outputString();
                    break;

                case 3:
                    System.out.println();
                    break;
            }
        }

        private void outputInt() {
            int width = pop();
            if (width == 0) width = 1;
            int x = pop();
            String formatString = "%" + width + "d";
            System.out.printf(formatString, x);
        }

        private void outputChar() {
            int width = pop();
            int c = pop();
            System.out.print((char) c);
            outputBlanks(width - 1);
        }

        private void outputBlanks(int number) {
            for (int i = 0; i < number; i++) {
                System.out.print(" ");
            }
        }

        private void outputString() {
            int width = pop();
            int stringLength = pop();
            int address = pop();
            System.out.print(new String(getDat(address, stringLength)));
            outputBlanks(width - stringLength);
        }
    }

    class Inc implements Instruction {

        @Override
        public void exec() {
            int n = getProgHalfWord(pc + 1);
            pc += 3;

            top += n;
            db_end = top;
        }
    }

    class Halt implements Instruction {

        @Override
        public void exec() {
            pc++;
            ms = MachineState.STOP;
        }
    }

    /// Constructor
    public Nbm() {
        prog = new byte[MAX_PROG];
        dat = new byte[MAX_DATA];
        ms = MachineState.RUN;
        db = 0;
        top = db + LINKAREA;
        db_end = top;
    }

    // ------------------- Setters and Getters ---------------------
    public static int getMAXDAT() {
        return MAX_DATA;
    }

    public static int getMAXPROG() {
        return MAX_PROG;
    }

    public MachineState getState() {
        return ms;
    }

    public boolean loadDat(int atAddr, byte[] data) {
        int i = 0;
        while ((atAddr + i) < MAX_DATA && i < data.length) {
            dat[atAddr + i] = data[i];
            i++;
        }
        db = atAddr + i;
        return (i == data.length);
    }

    public boolean loadProg(int atAddr, byte[] data) {
        int i = 0;
        while ((atAddr + i) < MAX_PROG && i < data.length) {
            prog[atAddr + i] = data[i];
            i++;
        }
        return (i == data.length);
    }

    public void execCycle() {
        byte opcode = fetch();
        Instruction instr = decode(opcode);
        instr.exec();
    }

    public int getStackTopValue() {
        int rv;

        if (top > db_end) {
            rv = getDatWord(top);
        } else {
            rv = STACKEMPTY;
        }
        return rv;

    }

    public void runProg(int startPc) {
        top = db + 28;
        pc = startPc;
        ms = MachineState.RUN;

        System.out.println("Starting programm at pc " + startPc);

        while (ms == MachineState.RUN) {
            execCycle();
        }
    }

    // -------------------------- private methods ---------------------------
    private int getDatWord(int atAddr) {
        int rv = 0;
        for (int i = 3; i >= 0; i--) {
            rv *= 256;
            rv += (dat[atAddr + i] & 0xff);
        }
        return rv;

    }

    private byte[] getDat(int startAddr, int len) {
        return (Arrays.copyOfRange(dat, startAddr, startAddr + len));
    }

    private void setDatWord(int atAddr, int word) {
        for (int i = 0; i < 4; i++) {
            dat[atAddr + i] = (byte) (word % 256);
            word /= 256;

        }
    }

    private void push(int word) {
        setDatWord(top + 4, word);
        top += 4;

    }

    private int pop() {
        int word = getStackTopValue();
        top -= 4;
        return word;
    }

    private byte fetch() {
        return prog[pc];
    }

    private int getProgHalfWord(int atAddr) {
        return ((prog[atAddr] & 0xff) * 256 + (prog[atAddr + 1] & 0xff));
    }

    private Instruction decode(byte opcode) {
        if (0 <= opcode && opcode < instructionMap.length) {
            return instructionMap[opcode];
        } else {
            ms = MachineState.ERROR;
            return new NoInstr();
        }
    }
}
