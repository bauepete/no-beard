/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import scanner.Scanner.Symbol;
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
public class NameManagerTest {

    private SrcReader sr;
    private SrcReader srMany;
    private SrcReader srKeywords;
    private Token t;
    private NameManager nameManager;
    private NameManager nameManagerMany;
    private NameManager nmKeywords;

    public NameManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        sr = new SrcStringReader("var1; var2;");
        srMany = new SrcStringReader("var1; var2; bla; blu; var2; blu;");
//        sr_ = new SrcStringReader("var_1; var_2; _var, var_, _var_");
//        srFail = new SrcStringReader("var_; _");
        srKeywords = new SrcStringReader("put, putln, unit, do, done, if, else, int, bool, char, true, false,");
        t = new Token();
        nameManager = new NameManager(sr);
        nameManagerMany = new NameManager(srMany);
        nmKeywords = new NameManager(srKeywords);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of readName method, of class NameManager.
     */
    @Test
    public void testReadName() {
        System.out.println("readName");

        sr.nextChar();
        nameManager.readName(t);

        assertEquals("IDENT ", Symbol.IDENTIFIER, t.getSymbol());
        assertEquals("Spix ", 0, t.getValue());
        assertEquals("Current char ", ';', sr.getCurrentChar());

        sr.nextChar();
        sr.nextChar();

        nameManager.readName(t);
        assertEquals("IDENT ", Symbol.IDENTIFIER, t.getSymbol());
        assertEquals("Spix ", 1, t.getValue());
        assertEquals("Current char ", ';', sr.getCurrentChar());
    }

    @Test
    public void testReadNameDouble() {
        System.out.println("testReadNameDouble");

        srMany.nextChar();
        nameManagerMany.readName(t); // var1
        srMany.nextChar();
        srMany.nextChar();

        nameManagerMany.readName(t); // var2
        int var2Spix = t.getValue();
        srMany.nextChar();
        srMany.nextChar();

        nameManagerMany.readName(t); // bla
        srMany.nextChar();
        srMany.nextChar();

        nameManagerMany.readName(t); // blu
        int bluSpix = t.getValue();
        srMany.nextChar();
        srMany.nextChar();

        nameManagerMany.readName(t); // var2
        srMany.nextChar();
        srMany.nextChar();
        assertEquals(var2Spix, t.getValue());

        nameManagerMany.readName(t); // blu
        srMany.nextChar();
        srMany.nextChar();
        assertEquals(bluSpix, t.getValue());
    }

    /**
     * Test of getStringName method, of class NameManager.
     */
    @Test
    public void testGetStringName() {
        System.out.println("getStringName");

        sr.nextChar();
        nameManager.readName(t);

        assertEquals("Name ", "var1", nameManager.getStringName(t.getValue()));

        sr.nextChar();
        sr.nextChar();

        nameManager.readName(t);

        assertEquals("Name ", "var2", nameManager.getStringName(t.getValue()));
    }

    @Test
    public void testKeywords() {
        System.out.println("testKeywords");

        srKeywords.nextChar();
        nmKeywords.readName(t);

        Symbol[] expTokens = {Symbol.PUT, Symbol.PUTLN, Symbol.UNIT, Symbol.DO,
            Symbol.DONE, Symbol.IF, Symbol.ELSE, Symbol.INT, Symbol.BOOL,
            Symbol.CHAR, Symbol.TRUE, Symbol.FALSE};

        for (Symbol s : expTokens) {
            assertEquals(s, t.getSymbol());
            assertEquals(',', srKeywords.getCurrentChar());
            srKeywords.nextChar();
            srKeywords.nextChar();
            nmKeywords.readName(t);
        }

    }
}
