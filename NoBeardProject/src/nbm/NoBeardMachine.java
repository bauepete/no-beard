/*
 * Copyright ©2011 - 2016. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
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
package nbm;

import error.ErrorHandler;
import error.SourceCodeInfo;
import java.util.Arrays;

/**
 *
 * @author peter
 */
public class NoBeardMachine implements SourceCodeInfo {

    @Override
    public int getCurrentCol() {
        return 0;
    }

    @Override
    public int getCurrentLine() {
        return pc;
    }

    error.Error getError() {
        return errorHandler.getLastError();
    }

    public enum State {

        RUN, STOP, ERROR
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
    private final error.ErrorHandler errorHandler;
    private final DataMemory dataMemory;
    private final CallStack callStack;
    private int pc;             // Program counter
    private int db;             // Pointer to first byte of stack frame of currently running function
    private int db_end;         // Pointer to the last byte of stack frame (db + LINKAREA + sizeof(local variables))
    private int top;            // Pointer to the last used byte in frame stack
    private State ms;
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
            ms = State.ERROR;
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
            ms = State.STOP;
        }
    }

    /// Constructor
    public NoBeardMachine() {
        prog = new byte[MAX_PROG];
        dat = new byte[MAX_DATA];
        errorHandler = new ErrorHandler(this);
        dataMemory = new DataMemory(MAX_DATA, errorHandler);
        callStack = new CallStack(dataMemory, 0, LINKAREA);
        ms = State.RUN;
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

    public State getState() {
        return ms;
    }
    
    public int getAddressOfFirstFrame() {
        return callStack.getFramePointer();
    }

    public void loadStringConstants(byte[] data) {
        dataMemory.storeStringConstants(data);
        if (errorHandler.getCount() == 0) {
            callStack.setCurrentFramePointer(data.length);
        } else {
            ms = State.ERROR;
        }
    }

    public boolean loadProg(int atAddr, byte[] data) {
        int i = 0;
        while ((atAddr + i) < MAX_PROG && i < data.length) {
            prog[atAddr + i] = data[i];
            i++;
        }
        return (i == data.length);
    }

    public void runProg(int startPc) {
        top = db + 28;
        pc = startPc;
        ms = State.RUN;

        System.out.println("Starting programm at pc " + startPc);

        while (ms == State.RUN) {
            execCycle();
        }
    }

    public void execCycle() {
        byte opcode = fetch();
        Instruction instr = decode(opcode);
        instr.exec();
    }

    private byte fetch() {
        return prog[pc];
    }

    private Instruction decode(byte opcode) {
        if (0 <= opcode && opcode < instructionMap.length) {
            return instructionMap[opcode];
        } else {
            ms = State.ERROR;
            return new NoInstr();
        }
    }

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

    public int getStackTopValue() {
        int rv;

        if (top > db_end) {
            rv = getDatWord(top);
        } else {
            rv = STACKEMPTY;
        }
        return rv;

    }

    private int getProgHalfWord(int atAddr) {
        return ((prog[atAddr] & 0xff) * 256 + (prog[atAddr + 1] & 0xff));
    }
}
