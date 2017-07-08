/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symboltable;


/**
 *
 * @author peter
 */
public class SymbolTableEntry {

    private final int name;
    private final Operand.Kind kind;
    private final Operand.Type type;
    private int size;
    private int addr;
    private final int level;

    public SymbolTableEntry(int name, Operand.Kind kind, Operand.Type type, int size, int addr, int level) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.size = size;
        this.addr = addr;
        this.level = level;
    }

    public Operand.Kind getKind() {
        return kind;
    }

    public int getLevel() {
        return level;
    }

    public int getName() {
        return name;
    }

    public Operand.Type getType() {
        return type;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void addSize(int size) {
        this.size += size;
    }

    public int getSize() {
        return size;
    }

    public int getAddr() {
        return addr;
    }

    public void setAddr(int addr) {
        this.addr = addr;
    }
    
    public boolean isNamedBlockEntry() {
        return (kind == Operand.Kind.FUNCTION || kind == Operand.Kind.UNIT);
    }

    public Operand createOperand() {
        Operand op;

        switch (kind) {
            case ANONYMOUSBLOCK:
            case FUNCTION:
            case UNIT:
                op = new UnitOperand(type, size, addr, level);
                break;
            case CONSTANT:
                op = new ConstantOperand(type, size, addr, level);
                break;

            case VARIABLE:
                op = new VariableOperand(type, size, addr, level);
                break;

            default:
                op = new IllegalOperand();
        }
        return op;
    }
}
