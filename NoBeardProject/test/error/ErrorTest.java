/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import scanner.SrcFileReader;
import scanner.Scanner.Symbol;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scanner.Scanner;
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
        try {
            Error.setScanner(new Scanner(new SrcFileReader("SamplePrograms/Smallest.nb")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ErrorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        identExp = new SynErr().new IdentifierExpected();
        statExp = new SynErr().new StatementExpected();
        symExp = new SynErr().new SymbolExpected(Symbol.ASSIGNSY.toString());
        blockNameMism = new SemErr().new BlockNameMismatch("Seppi", "Fraunzn");
        cantPutOp = new SemErr().new CantPutOperand();
        illegalOp = new SemErr().new IllegalOperand();
        incompTypes = new SemErr().new IncompatibleTypes("int", "bool");
        nameAlrDefd = new SemErr().new NameAlreadyDefined("Fritz");
        typeExp = new SemErr().new TypeExpected("int");
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getErrorClass method, of class Error.
     */
    @Test
    public void testSynErrs() {
        System.out.println("SynErrs");
        Error e = identExp;
        assertEquals("Msg ", "Identifier expected", e.getMessage());
        assertEquals("ErrNo ", 20, e.getErrNo());

        e = statExp;
        assertEquals("Msg ", "Is not a statement", e.getMessage());
        assertEquals("ErrNo ", 21, e.getErrNo());

        e = symExp;
        assertEquals("Msg ", "assign expected", e.getMessage());
        assertEquals("ErrNo ", 22, e.getErrNo());
    }

    /**
     * Test of getMessage method, of class Error.
     */
    @Test
    public void testSemErrs() {
         System.out.println("testSemErrors");

        Error e = blockNameMism;
        assertEquals("Msg ", "Block Seppi ends with name Fraunzn", e.getMessage());
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
