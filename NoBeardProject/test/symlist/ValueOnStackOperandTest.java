/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import symlist.Operand.OperandKind;
import error.ErrorHandler;
import error.Error;
import scanner.Scanner;
import nbm.Code;
import nbm.Nbm.Opcode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import parser.semantics.AssemblerCodeChecker;
import scanner.SrcStringReader;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class ValueOnStackOperandTest {

    Code toCode;
    ErrorHandler errorHandler;
    
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
        errorHandler = new ErrorHandler(new SrcStringReader(""));
        
        intSrcOp = new ConstantOperand(Operand.OperandType.SIMPLEINT, 4, 17, 0);
        intDestOp = new VariableOperand(Operand.OperandType.SIMPLEINT, 4, 32, 0);
        charSrcOp = new ConstantOperand(Operand.OperandType.SIMPLECHAR, 4, 17, 0);
        charDestOp = new VariableOperand(Operand.OperandType.SIMPLECHAR, 4, 32, 0);
        
        intVosSrcOp = new ValueOnStackOperand(intSrcOp);
        intAosDestOp = new AddrOnStackOperand(intDestOp);
        charVosSrcOp = new ValueOnStackOperand(charSrcOp);
        charAosDestOp = new AddrOnStackOperand(charDestOp);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of emitLoadVal method, of class ValueOnStackOperand.
     */
    @Test
    public void testEmitLoadVal() {
        System.out.println("emitLoadVal");
        Operand op = new ValueOnStackOperand(Operand.OperandType.SIMPLEBOOL, 4, 32, 0);
        assertEquals(OperandKind.VALUEONSTACK, op.emitLoadVal(toCode).getKind());
        assertEquals(0, toCode.getPc());
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
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, toCode.getByteCode());
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
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, toCode.getByteCode());
    }

    /**
     * Test of emitAssign method, of class ValueOnStackOperand.
     */
    @Test
    public void testEmitAssignToArrInt() {
        System.out.println("testEmitAssignToArrInt");
        
        Operand.setErrorHandler(errorHandler);
        Operand srcOp = new ValueOnStackOperand(Operand.OperandType.ARRAYINT, 40, 0, 0);
        Operand destOp = new AddrOnStackOperand(new VariableOperand(Operand.OperandType.ARRAYINT, 40, 0, 0));
        assertFalse(srcOp.emitAssign(toCode, destOp));
        assertEquals(0, toCode.getPc());
        assertEquals(Error.ErrorType.TYPE_EXPECTED.getNumber(), errorHandler.getLastError().getNumber());
    }

    /**
     * Test of emitLoadAddr method, of class ValueOnStackOperand.
     */
    @Test
    public void testEmitLoadAddr() {
        System.out.println("emitLoadAddr");
        
        Operand op = new ValueOnStackOperand(Operand.OperandType.SIMPLEBOOL, 4, 32, 0);
        assertEquals(OperandKind.ILLEGAL, op.emitLoadAddr(toCode).getKind());
    }
}
