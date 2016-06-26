/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import error.Error;
import error.Error.ErrorType;
import error.ErrorHandler;
import symlist.Operand.Kind;
import symlist.Operand.Type;
import java.util.ListIterator;
import java.util.Stack;
import nbm.CodeGenerator;
import scanner.NameManager;
import scanner.Scanner;

/**
 *
 * @author peter
 */
public class SymListManager {

    public enum ElementType {

        INT, CHAR, BOOL, ARRCHAR
    }

    private final int NONAME = -1;
    private final Stack<SymListEntry> symListStack;
    private final Stack<SymListEntry> blockStack;
    private final Stack<Integer> datAddrStack;
    private SymListEntry currBlock;
    private int currLevel;
    private int datAddr;
    private final CodeGenerator code;
    private final Scanner scanner;
    private final ErrorHandler errorHandler;

    public SymListManager(CodeGenerator code, Scanner scanner, ErrorHandler errorHandler) {
        //symList = new HashMap<Integer, SymListEntry>();
        symListStack = new Stack<SymListEntry>();
        blockStack = new Stack<SymListEntry>();
        datAddrStack = new Stack();
        this.code = code;
        this.scanner = scanner;
        this.errorHandler = errorHandler;
    }

    /**
     * Looks up a name in the symbol list and returns the SymListEntry with this
     * name. Lookup starts at top of the stack and the first node found matching
     * the name is returned. In case name is not found a node of kind
     * Operand.ILLEGAL is returned.
     *
     * @param name The name to be looked up.
     * @return The symbol node matching the name or an illegal symbol node.
     */
    public SymListEntry findObject(int name) {
        ListIterator<SymListEntry> i = symListStack.listIterator(symListStack.size());

        while (i.hasPrevious()) {
            SymListEntry node = i.previous();
            if (node.getName() == name) {
                return node;
            }
        }
        return new SymListEntry(name, Kind.ILLEGAL, Type.ERRORTYPE, 0, 0, 0);
    }

    public int getCurrLevel() {
        return currLevel;
    }

    public SymListEntry getCurrBlock() {
        return currBlock;
    }

    public int getDatAddr() {
        return datAddr;
    }

    public boolean newFunc(int name, Type operandType) {
        if (doesNameExistInCurrentScope(name)) {
            raiseNameAlreadyDefined(name);
            return false;
        }
        SymListEntry func = new SymListEntry(name, Kind.FUNCTION, operandType, 0, 0, currLevel);
        addBlockNode(func);
        return true;
    }

    /**
     * Creates a unit node and adds it to the symbol list.
     *
     * @param name Name of the unit.
     * @return false if name is already declared in the current scope or the
     * address would exceed MAXDATA.
     */
    public boolean newUnit(int name) {
        if (doesNameExistInCurrentScope(name)) {
            raiseNameAlreadyDefined(name);
            return false;
        }

        if (getCurrLevel() > 0) {
            errorHandler.raise(new Error(ErrorType.NO_NESTED_MODULES));
            return false;
        }

        SymListEntry proc = new SymListEntry(name, Kind.UNIT, Type.VOID, 0, 0, currLevel);
        addBlockNode(proc);
        return true;
    }

    private boolean doesNameExistInCurrentScope(int name) {
        return (findObjectInCurrentScope(name).getType() != Type.ERRORTYPE);
    }

    private SymListEntry findObjectInCurrentScope(int name) {
        ListIterator<SymListEntry> i = symListStack.listIterator(symListStack.size());

        while (i.hasPrevious()) {
            SymListEntry node = i.previous();
            if (node.getLevel() != getCurrLevel()) {
                return new SymListEntry(0, Kind.ILLEGAL, Type.ERRORTYPE, 0, 0, 0);
            }
            if (node.getName() == name) {
                return node;
            }
        }
        return new SymListEntry(0, Kind.ILLEGAL, Type.ERRORTYPE, 0, 0, 0);
    }

    private void addBlockNode(SymListEntry node) {
        symListStack.push(node);
        if (currBlock != null) {
            blockStack.push(currBlock);
        }
        currBlock = node;
        datAddrStack.push(datAddr);
        datAddr = 32;
        currLevel++;

    }

    /**
     * Creates an anonymous block node and adds it to the symbol list.
     */
    public void newBlock() {
        SymListEntry block = new SymListEntry(NONAME, Kind.ANONYMOUSBLOCK, Type.VOID, 0, 0, currLevel);
        addBlockNode(block);
    }

    /**
     * Creates a variable node and adds it to the symbol list.
     *
     * @param name The name of the variable.
     * @param t Type of the variable (INT, CHAR, ..., ARRAYINT, ...)
     * @param maxInd Size of array. 1 in case of simple variable.
     * @return true if creating and adding the var node was successful, false
     * otherwise.
     */
    public boolean newVar(int name, ElementType t, int maxInd) {
        if (doesNameExistInCurrentScope(name)) {
            raiseNameAlreadyDefined(name);
            return false;
        }

        Type opType = Type.ERRORTYPE;
        int opSize = 0;

        switch (t) {
            case INT:
                opSize = 4;
                if (maxInd > 1) {
                    opType = Type.ARRAYINT;
                } else {
                    maxInd = 1;
                    opType = Type.SIMPLEINT;
                }
                break;

            case CHAR:
                opSize = 1;
                if (maxInd > 1) {
                    opType = Type.ARRAYCHAR;
                } else {
                    maxInd = 1;
                    opType = Type.SIMPLECHAR;
                }
                break;

            case BOOL:
                opSize = 4;
                if (maxInd > 1) {
                    opType = Type.ARRAYBOOL;
                } else {
                    maxInd = 1;
                    opType = Type.SIMPLEBOOL;
                }
                break;
        }

        SymListEntry var = new SymListEntry(name, Kind.VARIABLE, opType, opSize * maxInd, datAddr, currLevel);
        symListStack.push(var);
        currBlock.addSize(var.getSize());
        datAddr += var.getSize();
        alignDatAddrTo4();
        return true;
    }

    /**
     * Creates a variable node and adds it to the symbol list. This call is
     * equivalent to newVar(name, t, 1).
     *
     * @param name
     * @param t
     * @return
     */
    public boolean newVar(int name, ElementType t) {
        return (newVar(name, t, 1));
    }

    /**
     * Defines the start address of a function or unit kind node.
     *
     * @param funcObj The object which start address to be defined.
     * @param startPc The start address of funcObj.
     */
    public void defineFuncStart(SymListEntry funcObj, int startPc) {
        if (funcObj.isNamedBlockEntry()) {
            funcObj.setAddr(startPc);
        } else {
            // TODO: this is not finished yet.
            // errorHandler.raise(new Error(ErrorType.GENERAL_SEM_ERROR, "STRANGE!!!"));
        }
    }

    /**
     * Stores the size of a block object at a specific address into the program
     * memory.
     *
     * @param atAddr Address where to store the size.
     * @param blockObj Object whose size to be stored.
     */
    public void fixINC(int atAddr, SymListEntry blockObj) {
        code.fixup(atAddr, blockObj.getSize());
    }

    /**
     * Closes the scope of the current named (e.g., unit or function) or
     * anonymous block. All local objects of the block are deleted.
     */
    public void endBlock() {
        while (!symListStack.empty() && symListStack.peek().getLevel() == getCurrLevel()) {
            symListStack.pop();
        }
        
        if (symListStack.peek().getKind() == Kind.ANONYMOUSBLOCK)
            symListStack.pop();
        
        if (blockStack.empty()) {
            currBlock = null;
        } else {
            currBlock = blockStack.pop();
        }
        datAddr = datAddrStack.pop();
        currLevel--;
    }

    private void alignDatAddrTo4() {
        int r = datAddr % 4;
        if (r != 0) {
            datAddr += (4 - r);
        }
    }

    private NameManager nameManager() {
        return scanner.getNameManager();
    }

    private void raiseNameAlreadyDefined(int name) {
        errorHandler.raise(new Error(ErrorType.NAME_ALREADY_DEFINED, nameManager().getStringName(name)));
    }
}
