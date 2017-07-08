/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symboltable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import symboltable.Operand.Kind;
import symboltable.Operand.Type;

/**
 *
 * @author peter
 */
public class SymbolTableEntryTest {
    
    public SymbolTableEntryTest() {
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
        
        SymbolTableEntry unitE = new SymbolTableEntry(0, Kind.UNIT, Type.VOID, 4, 0, 0);
        SymbolTableEntry blockE = new SymbolTableEntry(1, Kind.ANONYMOUSBLOCK, Type.VOID, 4, 0, 0);
        SymbolTableEntry funcE = new SymbolTableEntry(2, Kind.FUNCTION, Type.SIMPLEBOOL, 8, 0, 0);
        SymbolTableEntry constE = new SymbolTableEntry(3, Kind.CONSTANT, Type.SIMPLECHAR, 1, 0, 0);
        SymbolTableEntry constV = new SymbolTableEntry(4, Kind.VARIABLE, Type.SIMPLEINT, 4, 0, 0);
        
        assertTrue(unitE.isNamedBlockEntry());
        assertFalse(blockE.isNamedBlockEntry());
        assertTrue(funcE.isNamedBlockEntry());
        assertFalse(constE.isNamedBlockEntry());
        assertFalse(constV.isNamedBlockEntry());
    }
    
    @Test
    public void testCreateOperandFromConst() {
        System.out.println("testCreateOperandFromUnit");
        SymbolTableEntry instance = new SymbolTableEntry(0, Kind.CONSTANT, Type.SIMPLEBOOL, 4, 32, 1);
        Operand expResult = new ConstantOperand(Type.SIMPLEBOOL, 0, 0, 0);
        Operand result = instance.createOperand();
        assertEquals("Class ", expResult.getClass(), result.getClass());
        assertEquals("Kind ", expResult.getKind(), result.getKind());
        assertEquals("Type ", expResult.getType(), result.getType());
    }

    @Test
    public void testCreateOperandFromVar() {
        System.out.println("testCreateOperandFromVar");
        SymbolTableEntry instance = new SymbolTableEntry(0, Kind.VARIABLE, Type.SIMPLECHAR, 1, 36, 1);
        Operand expResult = new VariableOperand(Type.SIMPLECHAR, 1, 32, 1);
        Operand result = instance.createOperand();
        assertEquals("Class ", expResult.getClass(), result.getClass());
        assertEquals("Kind ", expResult.getKind(), result.getKind());
        assertEquals("Type ", expResult.getType(), result.getType());
    }

    @Test
    public void testCreateOperandFromUnit() {
        System.out.println("testCreateOperandFromUnit");
        SymbolTableEntry instance = new SymbolTableEntry(0, Kind.UNIT, Type.VOID, 0, 0, 0);
        Operand expResult = new UnitOperand(Type.VOID, 0, 0, 0);
        Operand result = instance.createOperand();
        assertEquals("Class ", expResult.getClass(), result.getClass());
        assertEquals("Kind ", expResult.getKind(), result.getKind());
        assertEquals("Type ", expResult.getType(), result.getType());
    }
}
