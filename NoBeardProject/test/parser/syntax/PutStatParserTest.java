/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import nbm.Code;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.PutStatParser;
import scanner.Scanner;
import scanner.SrcStringReader;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class PutStatParserTest {
    
    public PutStatParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of parse method, of class PutStatParser.
     */
    @Test
    public void testParsePut() {
        System.out.println("testParsePut");
        Scanner s = new Scanner(new SrcStringReader("put(5);"));
        s.nextToken();
        Code c = new Code();
        SymListManager sl = new SymListManager(c, s);
        PutStatParser instance = new PutStatParser(s, sl, c);
        assertTrue(instance.parse());
        
        s = new Scanner(new SrcStringReader("put('some bla')"));
        s.nextToken();
        instance = new PutStatParser(s, sl, c);
        assertTrue(instance.parse());
    }
    
    /**
     * Test of parse method, of class PutStatParser.
     */
    @Test
    public void testParsePutln() {
        System.out.println("testParsePutln");
        Scanner s = new Scanner(new SrcStringReader("putln;"));
        s.nextToken();
        Code c = new Code();
        SymListManager sl = new SymListManager(c, s);
        PutStatParser instance = new PutStatParser(s, sl, c);
        assertTrue(instance.parse());
    }
}
