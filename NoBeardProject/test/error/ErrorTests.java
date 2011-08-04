/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

import error.synerr.StatementExpected;
import error.synerr.SynErr;
import error.synerr.IdentifierExpected;
import error.semerr.BlockNameMismatch;
import error.semerr.CantPutOperand;
import error.semerr.IllegalOperand;
import error.semerr.IncompatibleTypes;
import error.semerr.NameAlreadyDefined;
import error.semerr.SemErr;
import error.semerr.TypeExpected;
import error.synerr.SymbolExpected;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scanner.Scanner.Symbol;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class ErrorTests {

    private SynErr identExp;
    private SynErr statExp;
    private SynErr symExp;
    private SemErr blockNameMism;
    private SemErr cantPutOp;
    private SemErr illegalOp;
    private SemErr incompTypes;
    private SemErr nameAlrDefd;
    private SemErr typeExp;

    public ErrorTests() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        identExp = new IdentifierExpected();
        statExp = new StatementExpected();
        symExp = new SymbolExpected(Symbol.ASSIGNSY.toString());
        blockNameMism = new BlockNameMismatch("Sepp", "Fraunz");
        cantPutOp = new CantPutOperand();
        illegalOp = new IllegalOperand();
        incompTypes = new IncompatibleTypes("int", "bool");
        nameAlrDefd = new NameAlreadyDefined("Fritz");
        typeExp = new TypeExpected("int");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testErrors() {
        System.out.println("testErrors");

        Error e = identExp;
        assertEquals("Msg ", "Identifier expected", e.getMessage());
        assertEquals("ErrNo ", 0, e.getErrNo());

        e = statExp;
        assertEquals("Msg ", "Is not a statement", e.getMessage());
        assertEquals("ErrNo ", 1, e.getErrNo());

        e = symExp;
        assertEquals("Msg ", "assign expected", e.getMessage());
        assertEquals("ErrNo ", 2, e.getErrNo());

    }

    @Test
    public void testSemErrors() {
        System.out.println("testSemErrors");

        Error e = blockNameMism;
        assertEquals("Msg ", "Block Sepp ends with name Fraunz", e.getMessage());
        assertEquals("ErrNo ", 50, e.getErrNo());

        e = cantPutOp;
        assertEquals("Msg ", "Can't put this operand", e.getMessage());
        assertEquals("ErrNo ", 51, e.getErrNo());

        e = illegalOp;
        assertEquals("Msg ", "Illegal operand", e.getMessage());
        assertEquals("ErrNo ", 52, e.getErrNo());

        e = incompTypes;
        assertEquals("Msg ", "Incompatible types: Expected int found bool", e.getMessage());
        assertEquals("ErrNo ", 53, e.getErrNo());

        e = nameAlrDefd;
        assertEquals("Msg ", "Name Fritz already defined", e.getMessage());
        assertEquals("ErrNo ", 54, e.getErrNo());

        e = typeExp;
        assertEquals("Msg ", "Operand of type int expected", e.getMessage());
        assertEquals("ErrNo ", 55, e.getErrNo());
    }
}
