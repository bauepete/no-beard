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
import java.util.HashMap;
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
    private HashMap<Integer, SymListEntry> symList;
    private Stack s;
    private SymListEntry currProc;
    private int currLevel;
    private int datAddr;
    private Code code;
    private Scanner scanner;
    private NameManager nameManager;

    public SymListManager(Code code, Scanner scanner) {
        symList = new HashMap<Integer, SymListEntry>();
        s = new Stack();
        this.code = code;
        this.scanner = scanner;
        this.nameManager = scanner.getNameManager();
    }

    public SymListEntry findObject(int name) {
        if (symList.containsKey(name)) {
            return symList.get(name);
        }
        else {
            return new SymListEntry(name, OperandKind.ILLEGAL, OperandType.ERRORTYPE, 0, 0, 0);
        }
    }

    public int getCurrLevel() {
        return currLevel;
    }

    public SymListEntry getCurrProc() {
        return currProc;
    }

    public int getLength() {
        return symList.size();
    }

    public int getDatAddr() {
        return datAddr;
    }

    /**
     * Creates a unit node.
     * @param name
     * @return false if name is already declared in the current scope or
     * the address would exceed MAXDATA.
     */
    public boolean newUnit(int name) {
        if (symList.containsKey(name)) {
            ErrorHandler.getInstance().raise(new NameAlreadyDefined(nameManager.getStringName(name), scanner.getCurrentLine()));
            return false;
        }

        SymListEntry proc = new SymListEntry(name, OperandKind.BLOCK, OperandType.UNITTYPE, 0, 0, currLevel);
        symList.put(name, proc);
        s.push(currProc);
        currProc = proc;
        s.push(datAddr);
        datAddr = 32;
        currLevel++;
        return true;
    }

    public boolean newVar(int name, ElementType t, int maxInd) {
        if (symList.containsKey(name)) {
            ErrorHandler.getInstance().raise(new NameAlreadyDefined(nameManager.getStringName(name), scanner.getCurrentLine()));
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
        symList.put(name, var);
        currProc.addSize(var.getSize());
        datAddr += var.getSize();
        alignDatAddrTo4();
        return true;
    }
    
    public boolean newVar(int name, ElementType t) {
        return (newVar(name, t, 1));
    }

    public void defineProcStart(SymListEntry procObj, int startPc) {
        if (procObj.getType() == OperandType.UNITTYPE) {
            procObj.setAddr(startPc);
        } else {
            ErrorHandler.getInstance().raise(new SemErr(99, "Tried to define proc Start on a type different than unit", scanner.getCurrentLine()));
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

    private void alignDatAddrTo4() {
        int r = datAddr % 4;
        if (r == 0) {
            return;
        } else {
            datAddr += (4 - r);
        }
    }
}
