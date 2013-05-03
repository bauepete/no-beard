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
    private Token t;
    private NameManager nameManager;
    private NameManager nameManagerMany;

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
        t = new Token();
        nameManager = new NameManager(sr);
        nameManagerMany = new NameManager(srMany);
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

        assertEquals("IDENT ", Symbol.IDENTSY, t.getSy());
        assertEquals("Spix ", 0, t.getValue());
        assertEquals("Current char ", ';', sr.getCurrentChar());

        sr.nextChar();
        sr.nextChar();
        
        nameManager.readName(t);
        assertEquals("IDENT ", Symbol.IDENTSY, t.getSy());
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

        int spix = 0;

        sr.nextChar();
        nameManager.readName(t);

        assertEquals("Name ", "var1", nameManager.getStringName(t.getValue()));
        
        sr.nextChar();
        sr.nextChar();
        
        nameManager.readName(t);
        
        assertEquals("Name ", "var2", nameManager.getStringName(t.getValue()));
    }
}
