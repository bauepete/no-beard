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
import nbm.Nbm.Opcode;
import symlist.Operand.OperandType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import parser.semantics.AssmCodeChecker;
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
}
