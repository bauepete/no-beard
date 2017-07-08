/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symboltable;

import symboltable.Operand.Kind;
import scanner.Scanner;
import parser.semantics.AssemblerCodeChecker;
import machine.InstructionSet.Instruction;
import error.ErrorHandler;
import error.Error.ErrorType;
import symboltable.Operand.Type;
import machine.CodeGenerator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import io.SourceStringReader;
import static org.junit.Assert.*;
import io.SourceReader;
import scanner.NameManagerForCompiler;

/**
 *
 * @author peter
 */
public class ConstantOperandTest {
    private CodeGenerator c;
    private Scanner scanner;
    private ErrorHandler errorHandler;
    private final ConstantOperand intOp = new ConstantOperand(Type.SIMPLEINT, 4, 42, 0);
    private final ConstantOperand charOp = new ConstantOperand(Type.SIMPLECHAR, 1, 0, 0);
    private final ConstantOperand strOp = new ConstantOperand(Type.ARRAYCHAR, 10, 0, 0);
    
    public ConstantOperandTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        SourceReader sourceReader = new SourceStringReader("unit A; do done A;");
        errorHandler = new ErrorHandler(sourceReader);
        scanner = new Scanner(sourceReader, errorHandler, new NameManagerForCompiler(sourceReader));
        c = new CodeGenerator(256);
        Operand.setStringManager(scanner.getStringManager());
        Operand.setErrorHandler(errorHandler);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of emitLoadVal method, of class ConstantOperand.
     */
    @Test
    public void testEmitLoadVal() {
        System.out.println("emitLoadVal");
        
        byte[] expInt = {
            Instruction.LIT.getId(), 0, 42
        };
        Operand rOp = intOp.emitLoadVal(c);
        assertEquals(Kind.VALUEONSTACK, rOp.getKind());
        AssemblerCodeChecker.assertCodeEquals("Code ", expInt, c.getByteCode());
        
        byte[] expChar = {
            Instruction.LIT.getId(), 0, 42
        };
        rOp = charOp.emitLoadVal(c);
        assertEquals(Kind.VALUEONSTACK, rOp.getKind());
        AssemblerCodeChecker.assertCodeEquals("Code ", expChar, c.getByteCode());
    }

    /**
     * Test of emitAssign method, of class ConstantOperand.
     */
    @Test
    public void testEmitAssignToSimpleInt() {
        System.out.println("testEmitAssignToSimpleInt");
        
        Operand destV = new VariableOperand(Type.SIMPLEINT, 4, 32, 0);
        Operand destAos = new AddrOnStackOperand(destV);
        
        byte[] exp = {
            Instruction.LIT.getId(), 0, 42,
            Instruction.STO.getId()
        };
        
        intOp.emitAssign(c, destAos);
        AssemblerCodeChecker.assertCodeEquals("Code ", exp, c.getByteCode());
    }

    /**
     * Test of emitAssign method, of class ConstantOperand.
     */
    @Test
    public void testEmitAssignToSimpleChar() {
        System.out.println("testEmitAssignToSimpleChar");
        
        Operand destV = new VariableOperand(Type.SIMPLECHAR, 1, 32, 0);
        Operand destAos = new AddrOnStackOperand(destV);
        
        byte[] exp = {
            Instruction.LIT.getId(), 0, 0,
            Instruction.STC.getId()
        };
        charOp.emitAssign(c, destAos);
        AssemblerCodeChecker.assertCodeEquals("Code ", exp, c.getByteCode());
    }

    /**
     * Test of emitAssign method, of class ConstantOperand.
     */
    @Test
    public void testEmitAssignToArrayChar() {
        System.out.println("testEmitAssignToArrayChar");
        
        Operand destV = new VariableOperand(Type.ARRAYCHAR, 10, 32, 0);
        Operand destAos = new AddrOnStackOperand(destV);
        
        byte[] exp = {
            Instruction.LIT.getId(), 0, 0,
            Instruction.LIT.getId(), 0, 10,
            Instruction.ASSN.getId()
        };
        strOp.emitAssign(c, destAos);
        AssemblerCodeChecker.assertCodeEquals("Code ", exp, c.getByteCode());
    }

    /**
     * Test of emitAssign method, of class ConstantOperand.
     */
    @Test
    public void testEmitAssignToOtherArray() {
        System.out.println("testEmitAssignToOtherArray");
        
        Operand srcArray = new ConstantOperand(Type.ARRAYBOOL, 10, 0, 0);
        Operand destV = new VariableOperand(Type.ARRAYBOOL, 10, 32, 0);
        Operand destAos = new AddrOnStackOperand(destV);
        
        assertFalse(srcArray.emitAssign(c, destAos));
        assertEquals(0, c.getPc());
        assertEquals(ErrorType.TYPES_EXPECTED.getNumber(), errorHandler.getLastError().getNumber());
    }

    /**
     * Test of emitLoadAddr method, of class ConstantOperand.
     */
    @Test
    public void testEmitLoadAddr() {
        System.out.println("emitLoadAddr");
        byte[] expected = {
            Instruction.LIT.getId(), 0, 0
        };
        
        Operand rv = charOp.emitLoadAddr(c);
        assertEquals(Kind.ADDRONSTACK, rv.getKind());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, c.getByteCode());
    }

    /**
     * Test of emitLoadAddr method, of class ConstantOperand.
     */
    @Test
    public void testEmitLoadAddrToInvalidType() {
        System.out.println("testEmitLoadAddrToInvalidType");
        
        Operand rv = intOp.emitLoadAddr(c);
        assertEquals(Kind.ILLEGAL, rv.getKind());
        assertEquals(0, c.getPc());
    }
}
