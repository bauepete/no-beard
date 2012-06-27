/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import scanner.Scanner;
import error.ErrorHandler;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nbm.Code;
import symlist.Operand.OperandType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scanner.SrcFileReader;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class OperandTest {

    private ConstantOperand cOp = new ConstantOperand(OperandType.SIMPLEINT, 4, 42, 0);
    private VariableOperand vOp = new VariableOperand(OperandType.ARRAYBOOL, 4 * 17, 0, 1);
    private ValueOnStackOperand vosOp = new ValueOnStackOperand(OperandType.SIMPLEBOOL, 4, 0, 1);
    private AddrOnStackOperand aosOp = new AddrOnStackOperand(cOp);
    
    private Code c;
    private Scanner scanner;
    
    public OperandTest() {
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
        try {
            scanner = new Scanner(new SrcFileReader("SamplePrograms/Smallest.nb"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OperandTest.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        Operand.setStringManager(scanner.getStringManager());
        ErrorHandler.getInstance().reset();
        error.Error.setScanner(scanner);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddrOnStackConstructionByCloning() {
        System.out.println("testAddrOnStackConstructionByCloning");

        AddrOnStackOperand aOp1 = new AddrOnStackOperand(cOp);
        AddrOnStackOperand aOp2 = new AddrOnStackOperand(vOp);
        AddrOnStackOperand aOp3 = new AddrOnStackOperand(aosOp);

        assertEquals(Operand.OperandKind.ADDRONSTACK, aOp1.getKind());
        assertEquals(Operand.OperandKind.ADDRONSTACK, aOp2.getKind());
        assertEquals(Operand.OperandKind.ADDRONSTACK, aOp3.getKind());
    }

    @Test
    public void testValOnStackConstructionByCloning() {
        System.out.println("testValOnStackConstructionByCloning");
        
        ValueOnStackOperand vOp1 = new ValueOnStackOperand(cOp);
        ValueOnStackOperand vOp2 = new ValueOnStackOperand(vOp);
        ValueOnStackOperand vOp3 = new ValueOnStackOperand(vosOp);
        ValueOnStackOperand vOp4 = new ValueOnStackOperand(aosOp);

        assertEquals(Operand.OperandKind.VALONSTACK, vOp1.getKind());
        assertEquals(Operand.OperandKind.VALONSTACK, vOp2.getKind());
        assertEquals(Operand.OperandKind.VALONSTACK, vOp3.getKind());
        assertEquals(Operand.OperandKind.VALONSTACK, vOp4.getKind());
    }
    
    @Test
    public void testAssignIncompatibleTypes() {
        System.out.println("testAssignIncompatibleTypes");

        Operand src = new ConstantOperand(OperandType.SIMPLEINT, 4, 42, 0);
        Operand destAosV = new AddrOnStackOperand(new VariableOperand(OperandType.SIMPLECHAR, 1, 0, 0));
        
        assertFalse(src.emitAssign(c, destAosV));
        assertEquals(0, c.getPc());
        assertEquals(53, ErrorHandler.getInstance().getLastError().getErrNo());
    }
    
    @Test
    public void testAssignInvalidDestKind() {
        System.out.println("testAssignInvalidDestKind");
        
        Operand src = new ConstantOperand(OperandType.ARRAYBOOL, 32, 32, 0);
        assertFalse("AddrOnStackOp expected", src.emitAssign(c, vOp));
        assertEquals(0, c.getPc());
        assertEquals(52, ErrorHandler.getInstance().getLastError().getErrNo());
    }
    
    @Test
    public void testAssignInvalidType() {
        System.out.println("testAssignInvalidType");
        
        Operand src = new ConstantOperand(OperandType.VOID, 0, 0, 0);
        assertFalse("Invalid type ", src.emitAssign(c, new AddrOnStackOperand(vOp)));
        assertEquals(55, ErrorHandler.getInstance().getLastError().getErrNo());
    }
    
    @Test
    public void testAssignInvalidSrcKind() {
        System.out.println("testAssignInvalidSrcKind");
        Operand src = new Operand(Operand.OperandKind.UNIT, OperandType.SIMPLEBOOL, 0, 0, 0);
        assertFalse("Invalid src kind ", src.emitAssign(c, new AddrOnStackOperand(vOp)));
        assertEquals(52, ErrorHandler.getInstance().getLastError().getErrNo());
    }
    
    
    @Test
    public void testLoadValInvalidKind() {
        System.out.println("testLoadValInvalidKind");
        Operand op = new Operand(Operand.OperandKind.UNIT, OperandType.VOID, 0, 0, 0);
        assertNull("Invalid Kind ", op.emitLoadVal(c));
        assertEquals(52, ErrorHandler.getInstance().getLastError().getErrNo());
    }
    
    @Test
    public void testLoadValInvalidType() {
        System.out.println("testLoadValInvalidType");
        Operand op = new Operand(Operand.OperandKind.ADDRONSTACK, OperandType.VOID, 0, 0, 0);
        assertNull("Invalid Type ", op.emitLoadVal(c));
        assertEquals(55, ErrorHandler.getInstance().getLastError().getErrNo());
    }
    
    @Test
    public void testLoadAddrInvalidKind() {
        System.out.println("testLoadAddrInvalidKind");
        Operand op = new Operand(Operand.OperandKind.UNIT, OperandType.VOID, 0, 0, 0);
        assertNull("Invalid Kind ", op.emitLoadVal(c));
        assertEquals(52, ErrorHandler.getInstance().getLastError().getErrNo());
    }
    
    @Test
    public void testLoadAddrInvalidType() {
        System.out.println("testLoadAddrInvalidType");
        Operand op = new Operand(Operand.OperandKind.ADDRONSTACK, OperandType.VOID, 0, 0, 0);
        assertNull("Invalid Type ", op.emitLoadVal(c));
        assertEquals(55, ErrorHandler.getInstance().getLastError().getErrNo());
    }
}
