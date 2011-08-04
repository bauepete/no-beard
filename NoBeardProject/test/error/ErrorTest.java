/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

import scanner.Scanner.Symbol;
import error.semerr.*;
import error.synerr.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class ErrorTest {
    private SynErr identExp;
    private SynErr statExp;
    private SynErr symExp;
    private SemErr blockNameMism;
    private SemErr cantPutOp;
    private SemErr illegalOp;
    private SemErr incompTypes;
    private SemErr nameAlrDefd;
    private SemErr typeExp;
    
    public ErrorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        identExp = new IdentifierExpected(1);
        statExp = new StatementExpected(2);
        symExp = new SymbolExpected(Symbol.ASSIGNSY.toString(), 3);
        blockNameMism = new BlockNameMismatch("Sepp", "Fraunz", 4);
        cantPutOp = new CantPutOperand(5);
        illegalOp = new IllegalOperand(6);
        incompTypes = new IncompatibleTypes("int", "bool", 7);
        nameAlrDefd = new NameAlreadyDefined("Fritz", 8);
        typeExp = new TypeExpected("int", 9);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getErrorClass method, of class Error.
     */
    @Test
    public void testGetErrorClass() {
        System.out.println("SynErrs");
        Error e = identExp;
        assertEquals("Msg ", "SynErr: 1: Identifier expected", e.getMessage());
        assertEquals("ErrNo ", 20, e.getErrNo());

        e = statExp;
        assertEquals("Msg ", "SynErr: 2: Is not a statement", e.getMessage());
        assertEquals("ErrNo ", 21, e.getErrNo());

        e = symExp;
        assertEquals("Msg ", "SynErr: 3: assign expected", e.getMessage());
        assertEquals("ErrNo ", 22, e.getErrNo());
    }

    /**
     * Test of getMessage method, of class Error.
     */
    @Test
    public void testGetMessage() {
         System.out.println("testSemErrors");

        Error e = blockNameMism;
        assertEquals("Msg ", "SemErr: 4: Block Sepp ends with name Fraunz", e.getMessage());
        assertEquals("ErrNo ", 50, e.getErrNo());

        e = cantPutOp;
        assertEquals("Msg ", "SemErr: 5: Can't put this operand", e.getMessage());
        assertEquals("ErrNo ", 51, e.getErrNo());

        e = illegalOp;
        assertEquals("Msg ", "SemErr: 6: Illegal operand", e.getMessage());
        assertEquals("ErrNo ", 52, e.getErrNo());

        e = incompTypes;
        assertEquals("Msg ", "SemErr: 7: Incompatible types: Expected int found bool", e.getMessage());
        assertEquals("ErrNo ", 53, e.getErrNo());

        e = nameAlrDefd;
        assertEquals("Msg ", "SemErr: 8: Name Fritz already defined", e.getMessage());
        assertEquals("ErrNo ", 54, e.getErrNo());

        e = typeExp;
        assertEquals("Msg ", "SemErr: 9: Operand of type int expected", e.getMessage());
        assertEquals("ErrNo ", 55, e.getErrNo());
    }
}
