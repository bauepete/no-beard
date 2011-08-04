/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import compiler.NbCompiler;
import error.ErrorHandler;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nbm.Code;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.NoBeardParser;
import scanner.Scanner;
import scanner.SrcFileReader;
import scanner.SrcReader;
import scanner.SrcStringReader;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class NoBeardParserTest {
    
    private Code c;
    private SymListManager sym;
    private Scanner s;
    private NoBeardParser p;
    
    public NoBeardParserTest() {
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
        ErrorHandler.getInstance().reset();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of parse method, of class NoBeardParser.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        
        setupTest(new SrcStringReader("unit foo; do int x = 3; done foo;"));
        assertTrue("True expected", p.parse());
        
        setupTest(new SrcStringReader("unit bah; do int x = 3; put (x); done bah;"));
        assertTrue("True expected", p.parse());
        
        setupTest(new SrcStringReader("unit rsch; do int x = 3; int y = 1; put (x + y); done rsch;"));
        assertTrue("True expected", p.parse());
    }
    
    @Test
    public void testParseEmpty() {
        System.out.println("parse");
        
        setupTest(new SrcStringReader("unit foo; do done foo;"));

        assertTrue("True expected", p.parse());
    }
    
    @Test
    public void testUnitExpected() {
        System.out.println("testUnitExpected");

        setupTest(new SrcStringReader("unti foo; do put x; done foo;"));

        assertFalse("False expected", p.parse());
        assertEquals("Error count expected: ", 1, ErrorHandler.getInstance().getCount());
    }

    @Test
    public void testUnitIdentifierExpected() {
        System.out.println("testUnitIdentifierExpected");

        setupTest(new SrcStringReader("unit; do put x; done foo;"));
        
        assertFalse("False expected", p.parse());
        assertEquals("SynErr count ", 1, ErrorHandler.getInstance().getCount("SynErr"));
    }

    @Test
    public void testParseSmallest() {
        System.out.println("testParseSmallest");

        try {
            setupTest(new SrcFileReader("SamplePrograms/Smallest.nb"));

            assertTrue("True expected", p.parse());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NoBeardParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setupTest(SrcReader sr) {
        NbCompiler compiler = new NbCompiler(sr);
        s = compiler.getScanner();
        sym = compiler.getSymListManager();
        c = compiler.getCode();
        p = compiler.getParser();
    }
}
