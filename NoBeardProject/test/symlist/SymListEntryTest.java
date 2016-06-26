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
import symlist.Operand.Kind;
import symlist.Operand.Type;

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
    public void testIsNamedBlockEntry() {
        System.out.println("testIsNamedBlockEntry");
        
        SymListEntry unitE = new SymListEntry(0, Kind.UNIT, Type.VOID, 4, 0, 0);
        SymListEntry blockE = new SymListEntry(1, Kind.ANONYMOUSBLOCK, Type.VOID, 4, 0, 0);
        SymListEntry funcE = new SymListEntry(2, Kind.FUNCTION, Type.SIMPLEBOOL, 8, 0, 0);
        SymListEntry constE = new SymListEntry(3, Kind.CONSTANT, Type.SIMPLECHAR, 1, 0, 0);
        SymListEntry constV = new SymListEntry(4, Kind.VARIABLE, Type.SIMPLEINT, 4, 0, 0);
        
        assertTrue(unitE.isNamedBlockEntry());
        assertFalse(blockE.isNamedBlockEntry());
        assertTrue(funcE.isNamedBlockEntry());
        assertFalse(constE.isNamedBlockEntry());
        assertFalse(constV.isNamedBlockEntry());
    }
    
    @Test
    public void testCreateOperandFromConst() {
        System.out.println("testCreateOperandFromUnit");
        SymListEntry instance = new SymListEntry(0, Kind.CONSTANT, Type.SIMPLEBOOL, 4, 32, 1);
        Operand expResult = new ConstantOperand(Type.SIMPLEBOOL, 0, 0, 0);
        Operand result = instance.createOperand();
        assertEquals("Class ", expResult.getClass(), result.getClass());
        assertEquals("Kind ", expResult.getKind(), result.getKind());
        assertEquals("Type ", expResult.getType(), result.getType());
    }

    @Test
    public void testCreateOperandFromVar() {
        System.out.println("testCreateOperandFromVar");
        SymListEntry instance = new SymListEntry(0, Kind.VARIABLE, Type.SIMPLECHAR, 1, 36, 1);
        Operand expResult = new VariableOperand(Type.SIMPLECHAR, 1, 32, 1);
        Operand result = instance.createOperand();
        assertEquals("Class ", expResult.getClass(), result.getClass());
        assertEquals("Kind ", expResult.getKind(), result.getKind());
        assertEquals("Type ", expResult.getType(), result.getType());
    }

    @Test
    public void testCreateOperandFromUnit() {
        System.out.println("testCreateOperandFromUnit");
        SymListEntry instance = new SymListEntry(0, Kind.UNIT, Type.VOID, 0, 0, 0);
        Operand expResult = new UnitOperand(Type.VOID, 0, 0, 0);
        Operand result = instance.createOperand();
        assertEquals("Class ", expResult.getClass(), result.getClass());
        assertEquals("Kind ", expResult.getKind(), result.getKind());
        assertEquals("Type ", expResult.getType(), result.getType());
    }
}
