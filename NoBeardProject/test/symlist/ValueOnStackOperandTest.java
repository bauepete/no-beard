/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import error.ErrorHandler;
import error.Error;
import scanner.Scanner;
import org.junit.Ignore;
import nbm.Code;
import nbm.Nbm.Opcode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import parser.semantics.AssmCodeChecker;
import scanner.SrcStringReader;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class ValueOnStackOperandTest {

    Code toCode;
    Operand intSrcOp;
    Operand intDestOp;
    Operand charSrcOp;
    Operand charDestOp;
    ValueOnStackOperand intVosSrcOp;
    AddrOnStackOperand intAosDestOp;
    ValueOnStackOperand charVosSrcOp;
    AddrOnStackOperand charAosDestOp;

    public ValueOnStackOperandTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        toCode = new Code();
        
        intSrcOp = new ConstantOperand(Operand.OperandType.SIMPLEINT, 4, 17, 0);
        intDestOp = new VariableOperand(Operand.OperandType.SIMPLEINT, 4, 32, 0);
        charSrcOp = new ConstantOperand(Operand.OperandType.SIMPLECHAR, 4, 17, 0);
        charDestOp = new VariableOperand(Operand.OperandType.SIMPLECHAR, 4, 32, 0);
        
        intVosSrcOp = new ValueOnStackOperand(intSrcOp);
        intAosDestOp = new AddrOnStackOperand(intDestOp);
        charVosSrcOp = new ValueOnStackOperand(charSrcOp);
        charAosDestOp = new AddrOnStackOperand(charDestOp);
        
        ErrorHandler.getInstance().reset();
        Error.setScanner(new Scanner(new SrcStringReader("")));
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of emitLoadVal method, of class ValueOnStackOperand.
     */
    @Test
    @Ignore
    public void testEmitLoadVal() {
        System.out.println("emitLoadVal");
        Code toCode = null;
        ValueOnStackOperand instance = null;
        Operand expResult = null;
        Operand result = instance.emitLoadVal(toCode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of emitAssign method, of class ValueOnStackOperand.
     */
    @Test
    public void testEmitAssignToInt() {
        System.out.println("testEmitAssignToInt");
        assertTrue(intVosSrcOp.emitAssign(toCode, intAosDestOp));
        byte[] expected = {
            Opcode.STO.byteCode()
        };
        AssmCodeChecker.assertCodeEquals("Code ", expected, toCode.getByteCode());
    }

    /**
     * Test of emitAssign method, of class ValueOnStackOperand.
     */
    @Test
    public void testEmitAssignToChar() {
        System.out.println("testEmitAssignToChar");
        assertTrue(charVosSrcOp.emitAssign(toCode, charAosDestOp));
        byte[] expected = {
            Opcode.STC.byteCode()
        };
        AssmCodeChecker.assertCodeEquals("Code ", expected, toCode.getByteCode());
    }

    /**
     * Test of emitAssign method, of class ValueOnStackOperand.
     */
    @Test
    public void testEmitAssignFail() {
        System.out.println("testEmitAssignFail");
        assertFalse(intVosSrcOp.emitAssign(toCode, intDestOp));
        byte[] expected = {};
        assertEquals(0, toCode.getPc());
        AssmCodeChecker.assertCodeEquals("Code ", expected, toCode.getByteCode());
    }

    /**
     * Test of emitLoadAddr method, of class ValueOnStackOperand.
     */
    @Test
    @Ignore
    public void testEmitLoadAddr() {
        System.out.println("emitLoadAddr");
        Code toCode = null;
        ValueOnStackOperand instance = null;
        Operand expResult = null;
        Operand result = instance.emitLoadAddr(toCode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
