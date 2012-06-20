/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import error.ErrorHandler;
import error.semerr.NameAlreadyDefined;
import error.semerr.SemErr;
import symlist.Operand.OperandKind;
import symlist.Operand.OperandType;
import java.util.ListIterator;
import java.util.Stack;
import nbm.Code;
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
    private Stack<SymListEntry> symList;
    private Stack datAddrStack;
    private SymListEntry currBlock;
    private int currLevel;
    private int datAddr;
    private Code code;
    private Scanner scanner;

    public SymListManager(Code code, Scanner scanner) {
        //symList = new HashMap<Integer, SymListEntry>();
        symList = new Stack<SymListEntry>();
        datAddrStack = new Stack();
        this.code = code;
        this.scanner = scanner;
    }

    public SymListEntry findObject(int name) {
        ListIterator<SymListEntry> i = symList.listIterator(symList.size());
        
        while (i.hasPrevious()) {
            SymListEntry node = i.previous();
            if (node.getName() == name) {
                return node;
            }
        }
        return new SymListEntry(name, OperandKind.ILLEGAL, OperandType.ERRORTYPE, 0, 0, 0);
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

    /**
     * Creates a unit node and adds it to the symbol list.
     * @param name Name of the unit.
     * @return false if name is already declared in the current scope or
     * the address would exceed MAXDATA.
     */
    public boolean newUnit(int name) {
        if (doesNameExist(name)) {
            ErrorHandler.getInstance().raise(new NameAlreadyDefined(nameManager().getStringName(name), scanner.getCurrentLine()));
            return false;
        }

        SymListEntry proc = new SymListEntry(name, OperandKind.UNIT, OperandType.VOID, 0, 0, currLevel);
        addBlockNode(proc);
        return true;
    }

    /**
     * Creates an anonymous block node and adds it to the symbol list.
     */
    public void newBlock() {
        SymListEntry block = new SymListEntry(NONAME, OperandKind.ANONYMOUSBLOCK, OperandType.VOID, 0, 0, currLevel);
        addBlockNode(block);
    }
    
    /**
     * Creates a variable node and adds it to the symbol list.
     * @param name The name of the variable.
     * @param t Type of the variable (INT, CHAR, ..., ARRAYINT, ...)
     * @param maxInd Size of array. 1 in case of simple variable.
     * @return true if creating and adding the var node was successful,
     * false otherwise.
     */
    public boolean newVar(int name, ElementType t, int maxInd) {
        if (doesNameExist(name)) {
            ErrorHandler.getInstance().raise(new NameAlreadyDefined(nameManager().getStringName(name), scanner.getCurrentLine()));
            return false;
        }

        OperandType opType = OperandType.ERRORTYPE;
        int opSize = 0;

        switch (t) {
            case INT:
                opSize = 4;
                if (maxInd > 1) {
                    opType = OperandType.ARRAYINT;
                }
                else {
                    maxInd = 1;
                    opType = OperandType.SIMPLEINT;
                }
                break;

            case CHAR:
                opSize = 1;
                if (maxInd > 1) {
                    opType = OperandType.ARRAYCHAR;
                }
                else {
                    maxInd = 1;
                    opType = OperandType.SIMPLECHAR;
                }
                break;

            case BOOL:
                opSize = 4;
                if (maxInd > 1) {
                    opType = OperandType.ARRAYBOOL;
                }
                else {
                    maxInd = 1;
                    opType = OperandType.SIMPLEBOOL;
                }
                break;
        }
        
        SymListEntry var = new SymListEntry(name, OperandKind.VARIABLE, opType, opSize * maxInd, datAddr, currLevel);
        symList.push(var);
        currBlock.addSize(var.getSize());
        datAddr += var.getSize();
        alignDatAddrTo4();
        return true;
    }
    
    /**
     * Creates a variable node and adds it to the symbol list. This call is
     * equivalent to newVar(name, t, 1).
     * @param name
     * @param t
     * @return 
     */
    public boolean newVar(int name, ElementType t) {
        return (newVar(name, t, 1));
    }

    public void defineFuncStart(SymListEntry funcObj, int startPc) {
        if (funcObj.isNamedBlockEntry()) {
            funcObj.setAddr(startPc);
        } else {
            ErrorHandler.getInstance().raise(new SemErr(99, "Tried to define function start on a type different than unit", scanner.getCurrentLine()));
        }
    }

    /**
     * Stores the size of a block object at a specific address into the program
     * memory.
     * @param atAddr Address where to store the size.
     * @param blockObj Object whose size to be stored.
     */
    public void fixINC(int atAddr, SymListEntry blockObj) {
        code.fixup(atAddr, blockObj.getSize());
    }

    private boolean doesNameExist(int name) {
        return (findObject(name).getType() != OperandType.ERRORTYPE);
    }
    
    private void addBlockNode(SymListEntry node) {
        symList.push(node);
        currBlock = node;
        datAddrStack.push(datAddr);
        datAddr = 32;
        currLevel++;
        
    }
    
    private void alignDatAddrTo4() {
        int r = datAddr % 4;
        if (r == 0) {
            return;
        } else {
            datAddr += (4 - r);
        }
    }
    
    private NameManager nameManager() {
        return scanner.getNameManager();
    }
}
