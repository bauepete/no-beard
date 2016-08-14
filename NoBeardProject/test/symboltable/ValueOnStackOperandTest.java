/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symboltable;

import symboltable.Operand.Kind;
import error.ErrorHandler;
import error.Error;
import machine.CodeGenerator;
import machine.InstructionSet.Instruction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import parser.semantics.AssemblerCodeChecker;
import io.SourceStringReader;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class ValueOnStackOperandTest {

    CodeGenerator toCode;
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
        toCode = new CodeGenerator(256);
        errorHandler = new ErrorHandler(new SourceStringReader(""));
        
        intSrcOp = new ConstantOperand(Operand.Type.SIMPLEINT, 4, 17, 0);
        intDestOp = new VariableOperand(Operand.Type.SIMPLEINT, 4, 32, 0);
        charSrcOp = new ConstantOperand(Operand.Type.SIMPLECHAR, 4, 17, 0);
        charDestOp = new VariableOperand(Operand.Type.SIMPLECHAR, 4, 32, 0);
        
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
        Operand op = new ValueOnStackOperand(Operand.Type.SIMPLEBOOL, 4, 32, 0);
        assertEquals(Kind.VALUEONSTACK, op.emitLoadVal(toCode).getKind());
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
            Instruction.STO.getId()
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
            Instruction.STC.getId()
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
        Operand srcOp = new ValueOnStackOperand(Operand.Type.ARRAYINT, 40, 0, 0);
        Operand destOp = new AddrOnStackOperand(new VariableOperand(Operand.Type.ARRAYINT, 40, 0, 0));
        assertFalse(srcOp.emitAssign(toCode, destOp));
        assertEquals(0, toCode.getPc());
        assertEquals(Error.ErrorType.TYPES_EXPECTED.getNumber(), errorHandler.getLastError().getNumber());
    }

    /**
     * Test of emitLoadAddr method, of class ValueOnStackOperand.
     */
    @Test
    public void testEmitLoadAddr() {
        System.out.println("emitLoadAddr");
        
        Operand op = new ValueOnStackOperand(Operand.Type.SIMPLEBOOL, 4, 32, 0);
        assertEquals(Kind.ILLEGAL, op.emitLoadAddr(toCode).getKind());
    }
}
