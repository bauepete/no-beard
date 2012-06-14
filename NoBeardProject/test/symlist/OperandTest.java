/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import symlist.Operand.OperandType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class OperandTest {

    private ConstantOperand cOp = new ConstantOperand(OperandType.SIMPLEBOOL, 4, 0, 1);
    private VariableOperand vOp = new VariableOperand(OperandType.ARRAYBOOL, 4 * 17, 0, 1);
    private ValueOnStackOperand vosOp = new ValueOnStackOperand(OperandType.SIMPLEBOOL, 4, 0, 1);
    private AddrOnStackOperand aosOp = new AddrOnStackOperand(cOp);

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
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddrOnStackConstructionByCloning() {


        AddrOnStackOperand aOp1 = new AddrOnStackOperand(cOp);
        AddrOnStackOperand aOp2 = new AddrOnStackOperand(vOp);
        AddrOnStackOperand aOp3 = new AddrOnStackOperand(aosOp);

        assertEquals(Operand.OperandKind.ADDRONSTACK, aOp1.getKind());
        assertEquals(Operand.OperandKind.ADDRONSTACK, aOp2.getKind());
        assertEquals(Operand.OperandKind.ADDRONSTACK, aOp3.getKind());
    }

    @Test
    public void testValOnStackConstructionByCloning() {
        ValueOnStackOperand vOp1 = new ValueOnStackOperand(cOp);
        ValueOnStackOperand vOp2 = new ValueOnStackOperand(vOp);
        ValueOnStackOperand vOp3 = new ValueOnStackOperand(vosOp);
        ValueOnStackOperand vOp4 = new ValueOnStackOperand(aosOp);

        assertEquals(Operand.OperandKind.VALONSTACK, vOp1.getKind());
        assertEquals(Operand.OperandKind.VALONSTACK, vOp2.getKind());
        assertEquals(Operand.OperandKind.VALONSTACK, vOp3.getKind());
        assertEquals(Operand.OperandKind.VALONSTACK, vOp4.getKind());
    }
}
