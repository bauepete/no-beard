/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nbm;

/**
 *
 * @author peter
 */
public class Nbm {

    public enum MachineState {

        RUN, STOP, ERROR
    }

    public enum Opcode {

        LIT, LA, LV, LC, STO, STC, NEG, ADD, SUB, MUL, DIV, MOD, PUT, INC, HALT, TRAP;

        public byte byteCode() {
            return (byte) this.ordinal();
        }
    }
    /// Returned from getStackTopValue() if stack of currently running
    /// function is empty.
    public static final int STACKEMPTY = -1;
    // --------------------- Locally used data -----------------------------
    private static final int MAXPROG = 1024;   // Size of program memory
    private static final int MAXDAT = 1024;    // Size of data memory
    private static final int LINKAREA = 28;    // Space to store house keeping data when functions are called
    private static final int WORDSIZE = 4;     // Size of one word in bytes
    private byte[] prog;
    private byte[] dat;
    private int pc;             // Program counter
    private int db;             // Pointer to first byte of frame stack of currently running function
    private int db_end;         // Pointer to the last byte of frame stack (db + LINKAREA + sizeof(local variables))
    private int top;            // Pointer to the last used byte in frame stack
    private MachineState ms;

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
            int x = pop();
            int a = pop();
            // TODO: only the rightmost character may be written to dat
            setDatWord(a, x);
        }
    }

    class Neg implements Instruction {

        @Override
        public void exec() {
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

    class Put implements Instruction {

        @Override
        public void exec() {
            byte s = prog[pc + 1];
            pc += 2;
            
            int w;
            switch (s) {
                case 0:
                    w = pop();
                    int x = pop();
                    System.out.print(x);
                    break;
                    
                case 1:
                    w = pop();
                    int c = pop();
                    System.out.print(c);
            }
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
        prog = new byte[MAXPROG];
        dat = new byte[MAXDAT];
        ms = MachineState.RUN;
        db = 0;
        top = db + LINKAREA;
        db_end = top;
    }

    // ------------------- Setters and Getters ---------------------
    public static int getMAXDAT() {
        return MAXDAT;
    }

    public static int getMAXPROG() {
        return MAXPROG;
    }

    public MachineState getState() {
        return ms;
    }

    public boolean loadDat(int atAddr, byte[] data) {
        int i = 0;
        while ((atAddr + i) < MAXDAT && i < data.length) {
            dat[atAddr + i] = data[i];
            i++;
        }
        db = atAddr + i;
        return (i == data.length);
    }

    public boolean loadProg(int atAddr, byte[] data) {
        int i = 0;
        while ((atAddr + i) < MAXPROG && i < data.length) {
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
        int rv = 0;

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
        Instruction i;
        Opcode o = Opcode.values()[opcode];

        switch (o) {
            case LIT:
                i = new Lit();
                break;
                
            case LA:
                i = new La();
                break;
                
            case LV:
                i = new Lv();
                break;
                
            case LC:
                i = new Lc();
                break;
                
            case STO:
                i = new Sto();
                break;
                
            case STC:
                i = new Stc();
                break;
                
            case NEG:
                i = new Neg();
                break;
                
            case ADD:
                i = new Add();
                break;
                
            case SUB:
                i = new Sub();
                break;
                
            case MUL:
                i = new Mul();
                break;
                
            case DIV:
                i = new Div();
                break;
                
            case MOD:
                i = new Mod();
                break;

            case PUT:
                i = new Put();
                break;

            case INC:
                i = new Inc();
                break;
                
            case HALT:
                i = new Halt();
                break;

            default:
                ms = MachineState.ERROR;
                i = new NoInstr();
        }

        return i;
    }
}
