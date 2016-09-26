/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symboltable;

import symboltable.Operand;
import symboltable.AddrOnStackOperand;
import symboltable.ConstantOperand;
import symboltable.ValueOnStackOperand;
import symboltable.VariableOperand;
import scanner.Scanner;
import error.ErrorHandler;
import error.Error;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import machine.CodeGenerator;
import symboltable.Operand.Type;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import io.SourceFileReader;
import static org.junit.Assert.*;
import io.SourceReader;
import scanner.NameManagerForCompiler;

/**
 *
 * @author peter
 */
public class OperandTest {

    private final ConstantOperand cOp = new ConstantOperand(Type.SIMPLEINT, 4, 42, 0);
    private final VariableOperand vOp = new VariableOperand(Type.ARRAYBOOL, 4 * 17, 0, 1);
    private final ValueOnStackOperand vosOp = new ValueOnStackOperand(Type.SIMPLEBOOL, 4, 0, 1);
    private final AddrOnStackOperand aosOp = new AddrOnStackOperand(cOp);

    private CodeGenerator c;
    private ErrorHandler errorHandler;
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
        c = new CodeGenerator(256);
        SourceReader sourceReader = null;
        try {
            sourceReader = new SourceFileReader("SamplePrograms/NoBeardPrograms/Smallest.nb");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OperandTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        errorHandler = new ErrorHandler(sourceReader);
        scanner = new Scanner(sourceReader, errorHandler, new NameManagerForCompiler(sourceReader));

        Operand.setStringManager(scanner.getStringManager());
        Operand.setErrorHandler(errorHandler);
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

        assertEquals(Operand.Kind.ADDRONSTACK, aOp1.getKind());
        assertEquals(Operand.Kind.ADDRONSTACK, aOp2.getKind());
        assertEquals(Operand.Kind.ADDRONSTACK, aOp3.getKind());
    }

    @Test
    public void testValOnStackConstructionByCloning() {
        System.out.println("testValOnStackConstructionByCloning");

        ValueOnStackOperand vOp1 = new ValueOnStackOperand(cOp);
        ValueOnStackOperand vOp2 = new ValueOnStackOperand(vOp);
        ValueOnStackOperand vOp3 = new ValueOnStackOperand(vosOp);
        ValueOnStackOperand vOp4 = new ValueOnStackOperand(aosOp);

        assertEquals(Operand.Kind.VALUEONSTACK, vOp1.getKind());
        assertEquals(Operand.Kind.VALUEONSTACK, vOp2.getKind());
        assertEquals(Operand.Kind.VALUEONSTACK, vOp3.getKind());
        assertEquals(Operand.Kind.VALUEONSTACK, vOp4.getKind());
    }

    @Test
    public void testAssignIncompatibleTypes() {
        System.out.println("testAssignIncompatibleTypes");

        Operand src = new ConstantOperand(Type.SIMPLEINT, 4, 42, 0);
        Operand destAosV = new AddrOnStackOperand(new VariableOperand(Type.SIMPLECHAR, 1, 0, 0));

        assertFalse(src.emitAssign(c, destAosV));
        assertEquals(0, c.getPc());
        assertEquals(53, errorHandler.getLastError().getNumber());
    }

    @Test
    public void testAssignInvalidDestKind() {
        System.out.println("testAssignInvalidDestKind");

        Operand src = new ConstantOperand(Type.ARRAYBOOL, 32, 32, 0);
        assertFalse("AddrOnStackOp expected", src.emitAssign(c, vOp));
        assertEquals(0, c.getPc());
        assertEquals(Error.ErrorType.GENERAL_SEM_ERROR.getNumber(), errorHandler.getLastError().getNumber());
    }

    @Test
    public void testAssignInvalidType() {
        System.out.println("testAssignInvalidType");

        Operand src = new ConstantOperand(Type.VOID, 0, 0, 0);
        assertFalse("Invalid type ", src.emitAssign(c, new AddrOnStackOperand(vOp)));
        assertEquals(Error.ErrorType.TYPES_EXPECTED.getNumber(), errorHandler.getLastError().getNumber());
    }

    @Test
    public void testAssignInvalidSrcKind() {
        System.out.println("testAssignInvalidSrcKind");
        Operand src = new Operand(Operand.Kind.UNIT, Type.SIMPLEBOOL, 0, 0, 0);
        assertFalse("Invalid src kind ", src.emitAssign(c, new AddrOnStackOperand(vOp)));
        assertEquals(Error.ErrorType.GENERAL_SEM_ERROR.getNumber(), errorHandler.getLastError().getNumber());
    }

    @Test
    public void testLoadValInvalidKind() {
        System.out.println("testLoadValInvalidKind");
        Operand op = new Operand(Operand.Kind.UNIT, Type.VOID, 0, 0, 0);
        assertNull("Invalid Kind ", op.emitLoadVal(c));
        assertEquals(Error.ErrorType.GENERAL_SEM_ERROR.getNumber(), errorHandler.getLastError().getNumber());
    }

    @Test
    public void testLoadValInvalidType() {
        System.out.println("testLoadValInvalidType");
        Operand op = new Operand(Operand.Kind.ADDRONSTACK, Type.VOID, 0, 0, 0);
        assertNull("Invalid Type ", op.emitLoadVal(c));
        assertEquals(Error.ErrorType.TYPES_EXPECTED.getNumber(), errorHandler.getLastError().getNumber());
    }

    @Test
    public void testLoadAddrInvalidKind() {
        System.out.println("testLoadAddrInvalidKind");
        Operand op = new Operand(Operand.Kind.UNIT, Type.VOID, 0, 0, 0);
        assertNull("Invalid Kind ", op.emitLoadVal(c));
        assertEquals(Error.ErrorType.GENERAL_SEM_ERROR.getNumber(), errorHandler.getLastError().getNumber());
    }

    @Test
    public void testLoadAddrInvalidType() {
        System.out.println("testLoadAddrInvalidType");
        Operand op = new Operand(Operand.Kind.ADDRONSTACK, Type.VOID, 0, 0, 0);
        assertNull("Invalid Type ", op.emitLoadVal(c));
        assertEquals(Error.ErrorType.TYPES_EXPECTED.getNumber(), errorHandler.getLastError().getNumber());
    }
}
