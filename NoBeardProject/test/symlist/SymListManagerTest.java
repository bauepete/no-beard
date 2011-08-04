/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import symlist.Operand.OperandKind;
import scanner.Token;
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
        n = new NameManager(new SrcStringReader(""));
        symListMgr = new SymListManager(c, n);
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

        assertTrue("Expected unit", symListMgr.getCurrProc().getType() == Operand.OperandType.UNITTYPE);
        SymListEntry unitObj = symListMgr.findObject(0);
        assertFalse("Object found ", unitObj.getKind() == OperandKind.ILLEGAL);

        symListMgr.newUnit(0);
        assertEquals("SemErr ", 1, ErrorHandler.getInstance().getCount("SemErr"));
        assertEquals("Errors count", 1, ErrorHandler.getInstance().getCount());
        assertEquals("Error ", 54, ErrorHandler.getInstance().getLastError().getErrNo());
    }

    @Test
    public void testNewVar() {
        System.err.println("newVar");

        symListMgr.newVar(1, SymListManager.ElementType.INT);

        assertEquals("datAddr expected ", 36, symListMgr.getDatAddr());

        symListMgr.newVar(2, SymListManager.ElementType.BOOL);
        assertEquals("datAddr expected ", 40, symListMgr.getDatAddr());

        symListMgr.newVar(1, SymListManager.ElementType.INT);
        assertEquals("Err Count ", 1, ErrorHandler.getInstance().getCount());
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

        assertEquals("Kind expected: ", Operand.OperandKind.BLOCK, symListMgr.findObject(0).getKind());
        assertEquals("Type expected: ", Operand.OperandType.UNITTYPE, symListMgr.findObject(0).getType());
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
        symListMgr.defineProcStart(u, 2);

        assertEquals("Start PC ", 2, symListMgr.findObject(0).getAddr());

        u = symListMgr.findObject(1);
        symListMgr.defineProcStart(u, 2);
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
