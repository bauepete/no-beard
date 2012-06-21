/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import symlist.Operand.OperandType;
import scanner.Scanner;
import symlist.Operand.OperandKind;
import scanner.NameManager;
import nbm.Code;
import error.ErrorHandler;
import nbm.Nbm.Opcode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scanner.SrcStringReader;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class SymListManagerTest {

    private SymListManager symListMgr;
    private Code c;
    private NameManager n;

    public SymListManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        c = new Code();
        SrcStringReader sr = new SrcStringReader("");
        n = new NameManager(sr);
        Scanner s = new Scanner(sr);
        symListMgr = new SymListManager(c, s);
        ErrorHandler.getInstance().reset();
        
        symListMgr.newUnit(0);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of newUnit method, of class SymListManager.
     */
    @Test
    public void testNewUnit() {
        System.out.println("newUnit");

        assertEquals(Operand.OperandKind.UNIT, symListMgr.getCurrBlock().getKind());
        assertEquals(Operand.OperandType.VOID, symListMgr.getCurrBlock().getType());
        assertEquals(1, symListMgr.getCurrLevel());
        SymListEntry unitObj = symListMgr.findObject(0);
        assertEquals(unitObj.getKind(), OperandKind.UNIT);
        
        symListMgr.newUnit(1);
        assertEquals(1, symListMgr.getCurrLevel());
        assertEquals(OperandKind.UNIT, symListMgr.getCurrBlock().getKind());
        assertEquals(54, ErrorHandler.getInstance().getLastError().getErrNo());

        symListMgr.newUnit(0);
        assertEquals("SemErr ", 2, ErrorHandler.getInstance().getCount("SemErr"));
        assertEquals("Errors count", 2, ErrorHandler.getInstance().getCount());
        assertEquals("Error ", 54, ErrorHandler.getInstance().getLastError().getErrNo());
    }

    @Test
    public void testNewSimpleVar() {
        System.out.println("newSimpleVar");

        symListMgr.newVar(1, SymListManager.ElementType.INT);
        assertEquals("datAddr expected ", 36, symListMgr.getDatAddr());
        assertEquals(4, symListMgr.getCurrBlock().getSize());

        symListMgr.newVar(2, SymListManager.ElementType.BOOL);
        assertEquals("datAddr expected ", 40, symListMgr.getDatAddr());
        assertEquals(8, symListMgr.getCurrBlock().getSize());
        
        symListMgr.newVar(3, SymListManager.ElementType.CHAR);
        assertEquals(44, symListMgr.getDatAddr());
        assertEquals(9, symListMgr.getCurrBlock().getSize());
    }
    
    @Test
    public void testNewArrayVar() {
        System.out.print("testNewArrayVar");
        
        symListMgr.newVar(1, SymListManager.ElementType.INT, 10);
        assertEquals(72, symListMgr.getDatAddr());
        assertEquals(40, symListMgr.getCurrBlock().getSize());
        
        symListMgr.newVar(2, SymListManager.ElementType.BOOL, 5);
        assertEquals(92, symListMgr.getDatAddr());
        assertEquals(60, symListMgr.getCurrBlock().getSize());
        
        symListMgr.newVar(3, SymListManager.ElementType.CHAR, 15);
        assertEquals(108, symListMgr.getDatAddr());
        assertEquals(75, symListMgr.getCurrBlock().getSize());
    }
    
    @Test
    public void testNewVarFail() {
        System.out.println("testNewVarFail");
        
        symListMgr.newVar(0, SymListManager.ElementType.INT);
        assertEquals(4, symListMgr.getCurrBlock().getSize());
        assertEquals(36, symListMgr.getDatAddr());
        
        symListMgr.newVar(0, SymListManager.ElementType.BOOL);
        assertEquals(4, symListMgr.getCurrBlock().getSize());
        assertEquals(36, symListMgr.getDatAddr());
     }
    
    @Test
    public void testNewBlock() {
        System.out.println("testNewBlock");
        
        symListMgr.newVar(1, SymListManager.ElementType.INT);
        symListMgr.newBlock();
        assertEquals(2, symListMgr.getCurrLevel());
        
        symListMgr.newVar(1, SymListManager.ElementType.INT);
        assertEquals(2, symListMgr.getCurrLevel());
        assertEquals(0, ErrorHandler.getInstance().getCount());
        assertEquals(4, symListMgr.getCurrBlock().getSize());
    }
    
    @Test
    public void testNewFuncFail() {
        System.out.println("testNewFuncFail");
        
        symListMgr.newVar(1, SymListManager.ElementType.INT);
        symListMgr.newFunc(1, OperandType.SIMPLEBOOL);
        assertEquals(54, ErrorHandler.getInstance().getLastError().getErrNo());
        assertEquals(1, symListMgr.getCurrLevel());
        assertEquals(0, symListMgr.getCurrBlock().getName());
        assertEquals(OperandKind.UNIT, symListMgr.getCurrBlock().getKind());
    }

    @Test
    public void testNewFunc() {
        System.out.println("testNewFunc");
        
        symListMgr.newFunc(1, OperandType.ARRAYINT);
        assertEquals(2, symListMgr.getCurrLevel());
        assertEquals(32, symListMgr.getDatAddr());
    }
    
    @Test
    public void testEndBlock() {
        System.out.println("testEndBlock");
        
        symListMgr.newVar(0, SymListManager.ElementType.INT);
        symListMgr.newFunc(1, OperandType.VOID);
        symListMgr.newVar(0, SymListManager.ElementType.CHAR);
        symListMgr.newBlock();
        symListMgr.newVar(0, SymListManager.ElementType.INT);
        symListMgr.newVar(1, SymListManager.ElementType.INT, 5);
        
        // Test whether setup is correct
        assertEquals(0, ErrorHandler.getInstance().getCount());
        assertEquals(3, symListMgr.getCurrLevel());
        assertEquals(24, symListMgr.getCurrBlock().getSize());
        assertEquals(56, symListMgr.getDatAddr());
        
        symListMgr.endBlock();
        assertEquals(2, symListMgr.getCurrLevel());
        assertEquals(1, symListMgr.getCurrBlock().getSize());
        assertEquals(36, symListMgr.getDatAddr());
        
        symListMgr.endBlock();
        assertEquals(1, symListMgr.getCurrLevel());
        assertEquals(4, symListMgr.getCurrBlock().getSize());
        assertEquals(36, symListMgr.getDatAddr());
        
        symListMgr.endBlock();
        assertEquals(null, symListMgr.getCurrBlock());
    }
    
    @Test
    public void testAlignDatAddrTo4() {
        System.out.println("alignDatAddrTo4");

        symListMgr.newVar(2, SymListManager.ElementType.CHAR);
        assertEquals("datAddr expected ", 36, symListMgr.getDatAddr());
    }

    @Test
    public void testGetObject() {
        System.out.println("testGetObject");

        symListMgr.newVar(1, SymListManager.ElementType.CHAR);
        symListMgr.newVar(2, SymListManager.ElementType.INT);

        assertEquals("Kind expected: ", Operand.OperandKind.UNIT, symListMgr.findObject(0).getKind());
        assertEquals("Type expected: ", Operand.OperandType.VOID, symListMgr.findObject(0).getType());
        assertEquals("Size expected: ", 5, symListMgr.findObject(0).getSize());

        assertEquals("Kind expected: ", Operand.OperandKind.VARIABLE, symListMgr.findObject(1).getKind());
        assertEquals("Type expected: ", Operand.OperandType.SIMPLECHAR, symListMgr.findObject(1).getType());
        assertEquals("Size expected: ", 1, symListMgr.findObject(1).getSize());
        assertEquals("Address expected: ", 32, symListMgr.findObject(1).getAddr());


        assertEquals("Kind expected: ", Operand.OperandKind.VARIABLE, symListMgr.findObject(2).getKind());
        assertEquals("Type expected: ", Operand.OperandType.SIMPLEINT, symListMgr.findObject(2).getType());
        assertEquals("Size expected: ", 4, symListMgr.findObject(2).getSize());
        assertEquals("Address expected: ", 36, symListMgr.findObject(2).getAddr());
        
        assertEquals("Kind ", Operand.OperandKind.ILLEGAL, symListMgr.findObject(4).getKind());
    }

    @Test()
    public void testDefineProcStart() {
        System.out.println("defineProcStart");

        symListMgr.newVar(1, SymListManager.ElementType.INT);

        SymListEntry u = symListMgr.findObject(0);
        symListMgr.defineFuncStart(u, 2);

        assertEquals("Start PC ", 2, symListMgr.findObject(0).getAddr());

        u = symListMgr.findObject(1);
        symListMgr.defineFuncStart(u, 2);
        assertEquals("SemErr ", 1, ErrorHandler.getInstance().getCount("SemErr"));
        assertEquals("Addr of var ", 32, u.getAddr());
    }
    
    @Test
    public void testFixINC() {
        System.out.println("fixINC");
        
        symListMgr.newVar(1, SymListManager.ElementType.CHAR);
        symListMgr.newVar(2, SymListManager.ElementType.INT);
        
        c.emitOp(Opcode.INC);
        int incAddr = c.getPc();
        c.emitHalfWord(0);
        symListMgr.fixINC(incAddr, symListMgr.findObject(0));
        
        assertEquals("INC size ", 5, c.getCodeHalfWord(incAddr));
    }
}
