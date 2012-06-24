/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symlist;

import symlist.Operand.OperandKind;
import scanner.Scanner;
import parser.semantics.AssmCodeChecker;
import nbm.Nbm.Opcode;
import error.ErrorHandler;
import error.Error;
import symlist.Operand.OperandType;
import nbm.Code;
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
public class ConstantOperandTest {
    private Code c;
    private Scanner scanner;
    private ConstantOperand intOp = new ConstantOperand(OperandType.SIMPLEINT, 4, 42, 0);
    private ConstantOperand charOp = new ConstantOperand(OperandType.SIMPLECHAR, 1, 0, 0);
    private ConstantOperand strOp = new ConstantOperand(OperandType.ARRAYCHAR, 10, 0, 0);
    
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
        ErrorHandler.getInstance().reset();
        scanner = new Scanner(new SrcStringReader("unit A; do done A;"));
        c = new Code();
        Error.setScanner(scanner);
        Operand.setStringManager(scanner.getStringManager());
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
            Opcode.LIT.byteCode(), 0, 42
        };
        Operand rOp = intOp.emitLoadVal(c);
        assertEquals(OperandKind.VALONSTACK, rOp.getKind());
        AssmCodeChecker.assertCodeEquals("Code ", expInt, c.getByteCode());
        
        byte[] expChar = {
            Opcode.LIT.byteCode(), 0, 0
        };
        rOp = charOp.emitLoadVal(c);
        assertEquals(OperandKind.VALONSTACK, rOp.getKind());
        AssmCodeChecker.assertCodeEquals("Code ", expInt, c.getByteCode());
    }

    /**
     * Test of emitAssign method, of class ConstantOperand.
     */
    @Test
    public void testEmitAssignToSimpleInt() {
        System.out.println("testEmitAssignToSimpleInt");
        
        Operand destV = new VariableOperand(OperandType.SIMPLEINT, 4, 32, 0);
        Operand destAos = new AddrOnStackOperand(destV);
        
        byte[] exp = {
            Opcode.LIT.byteCode(), 0, 42,
            Opcode.STO.byteCode()
        };
        
        intOp.emitAssign(c, destAos);
        AssmCodeChecker.assertCodeEquals("Code ", exp, c.getByteCode());
    }

    /**
     * Test of emitAssign method, of class ConstantOperand.
     */
    @Test
    public void testEmitAssignToSimpleChar() {
        System.out.println("testEmitAssignToSimpleChar");
        
        Operand destV = new VariableOperand(OperandType.SIMPLECHAR, 1, 32, 0);
        Operand destAos = new AddrOnStackOperand(destV);
        
        byte[] exp = {
            Opcode.LIT.byteCode(), 0, 0,
            Opcode.STC.byteCode()
        };
        charOp.emitAssign(c, destAos);
        AssmCodeChecker.assertCodeEquals("Code ", exp, c.getByteCode());
    }

    /**
     * Test of emitAssign method, of class ConstantOperand.
     */
    @Test
    public void testEmitAssignToArrayChar() {
        System.out.println("testEmitAssignToArrayChar");
        
        Operand destV = new VariableOperand(OperandType.ARRAYCHAR, 10, 32, 0);
        Operand destAos = new AddrOnStackOperand(destV);
        
        byte[] exp = {
            Opcode.LIT.byteCode(), 0, 0,
            Opcode.LIT.byteCode(), 0, 10,
            Opcode.ASSN.byteCode()
        };
        strOp.emitAssign(c, destAos);
        AssmCodeChecker.assertCodeEquals("Code ", exp, c.getByteCode());
    }

    /**
     * Test of emitAssign method, of class ConstantOperand.
     */
    @Test
    public void testAssignFails() {
        System.out.println("testAssignFails");
        
        Operand destV = new VariableOperand(OperandType.SIMPLEINT, 4, 0, 0);
        intOp.emitAssign(c, destV);
        assertEquals(0, c.getPc());
        assertEquals(52, ErrorHandler.getInstance().getLastError().getErrNo());
        
        Operand destCharV = new VariableOperand(OperandType.SIMPLECHAR, 1, 0, 0);
        Operand destAosV = new AddrOnStackOperand(destCharV);
        
        intOp.emitAssign(c, destAosV);
        assertEquals(0, c.getPc());
        assertEquals(53, ErrorHandler.getInstance().getLastError().getErrNo());
    }

    /**
     * Test of emitLoadAddr method, of class ConstantOperand.
     */
    @Test
    public void testEmitLoadAddr() {
        System.out.println("emitLoadAddr");
        byte[] expected = {
            Opcode.LIT.byteCode(), 0, 0
        };
        
        Operand rv = charOp.emitLoadAddr(c);
        assertEquals(OperandKind.ADDRONSTACK, rv.getKind());
        AssmCodeChecker.assertCodeEquals("Code ", expected, c.getByteCode());
    }
}
