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
    private Token t;
    private NameManager instance;

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
        t = new Token();
        instance = new NameManager(sr);
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
        instance.readName(t);

        assertEquals("IDENT ", Symbol.IDENTSY, t.getSy());
        assertEquals("Spix ", 0, t.getValue());
        assertEquals("Current char ", ';', sr.getCurrentChar());

        sr.nextChar();
        sr.nextChar();
        
        instance.readName(t);
        assertEquals("IDENT ", Symbol.IDENTSY, t.getSy());
        assertEquals("Spix ", 1, t.getValue());
        assertEquals("Current char ", ';', sr.getCurrentChar());
    }

    /**
     * Test of getStringName method, of class NameManager.
     */
    @Test
    public void testGetStringName() {
        System.out.println("getStringName");

        int spix = 0;

        sr.nextChar();
        instance.readName(t);

        assertEquals("Name ", "var1", instance.getStringName(t.getValue()));
        
        sr.nextChar();
        sr.nextChar();
        
        instance.readName(t);
        
        assertEquals("Name ", "var2", instance.getStringName(t.getValue()));
    }
}
