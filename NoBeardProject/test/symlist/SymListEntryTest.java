/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import symlist.Operand.OperandKind;
import symlist.Operand.OperandType;

/**
 *
 * @author peter
 */
public class SymListEntryTest {
    
    public SymListEntryTest() {
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
    public void testCreateOperandFromConst() {
        System.out.println("testCreateOperandFromUnit");
        SymListEntry instance = new SymListEntry(0, OperandKind.CONSTANT, OperandType.SIMPLEBOOL, 4, 32, 1);
        Operand expResult = new ConstantOperand(OperandType.SIMPLEBOOL, 0, 0, 0);
        Operand result = instance.createOperand();
        assertEquals("Class ", expResult.getClass(), result.getClass());
        assertEquals("Kind ", expResult.getKind(), result.getKind());
        assertEquals("Type ", expResult.getType(), result.getType());
    }

    @Test
    public void testCreateOperandFromVar() {
        System.out.println("testCreateOperandFromVar");
        SymListEntry instance = new SymListEntry(0, OperandKind.VARIABLE, OperandType.SIMPLECHAR, 1, 36, 1);
        Operand expResult = new VariableOperand(OperandType.SIMPLECHAR, 1, 32, 1);
        Operand result = instance.createOperand();
        assertEquals("Class ", expResult.getClass(), result.getClass());
        assertEquals("Kind ", expResult.getKind(), result.getKind());
        assertEquals("Type ", expResult.getType(), result.getType());
    }

    @Test
    public void testCreateOperandFromUnit() {
        System.out.println("testCreateOperandFromUnit");
        SymListEntry instance = new SymListEntry(0, OperandKind.BLOCK, OperandType.UNITTYPE, 0, 0, 0);
        Operand expResult = new BlockOperand(OperandType.UNITTYPE, 0, 0, 0);
        Operand result = instance.createOperand();
        assertEquals("Class ", expResult.getClass(), result.getClass());
        assertEquals("Kind ", expResult.getKind(), result.getKind());
        assertEquals("Type ", expResult.getType(), result.getType());
    }
}
