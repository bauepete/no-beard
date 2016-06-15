/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import error.ErrorHandler;
import nbm.CodeGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.StatParser;
import scanner.Scanner;
import scanner.SrcReader;
import scanner.SrcStringReader;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class StatParserTest {
    private ErrorHandler errorHandler;
    private Scanner scanner;
    
    private Scanner assignS;
    private Scanner noAssignS;
    private Scanner simplePutS;
    private Scanner putS;
    private Scanner noPutS;
    private SymListManager sym;
    private CodeGenerator c;
    
    public StatParserTest() {
    }

    @Before
    public void setUp() {
        c = new CodeGenerator(256);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of parseOldStyle method, of class StatParser.
     */
    @Test
    public void testParseAssignStat() {
        System.out.println("testParseAssignStat");
        setupTestObjects("a = 5;");
        sym = new SymListManager(c, scanner, errorHandler);
        sym.newUnit(1);
        sym.newVar(0, SymListManager.ElementType.INT);
        Operand.setSymListManager(sym);
        scanner.nextToken();
        
        StatParser p = new StatParser(scanner, sym, c, errorHandler);
        assertEquals("Parse ", true, p.parseOldStyle());
        
    }
    
    private void setupTestObjects(String srcLine) {
        SrcReader sourceReader = new SrcStringReader(srcLine);
        errorHandler = new ErrorHandler(sourceReader);
        scanner = new Scanner(sourceReader, errorHandler);
    }

    /**
     * Test of parseOldStyle method, of class StatParser.
     */
    @Test
    public void testParsePutStat() {
        System.out.println("testParsePutStat");
        setupTestObjects("put(5, 3);");
        scanner.nextToken();
        
        StatParser p = new StatParser(scanner, sym, c, errorHandler);
        assertEquals("Parse ", true, p.parseOldStyle());
        
    }

    /**
     * Test of parseOldStyle method, of class StatParser.
     */
    @Test
    public void testParseSimplePutStat() {
        System.out.println("testParseSimplePutStat");
        setupTestObjects("put(5);");
        scanner.nextToken();
        
        StatParser p = new StatParser(scanner, sym, c, errorHandler);
        assertEquals("Parse ", true, p.parseOldStyle());
        
    }
    /**
     * Test of parseOldStyle method, of class StatParser.
     */
    @Test
    public void testParseNoAssignStat() {
        System.out.println("testParseNoAssignStat");
        setupTestObjects("a == 5;");
        sym = new SymListManager(c, scanner, errorHandler);
        sym.newUnit(1);
        sym.newVar(0, SymListManager.ElementType.INT);
        Operand.setSymListManager(sym);
        scanner.nextToken();
        
        StatParser p = new StatParser(scanner, sym, c, errorHandler);
        assertEquals("Parse ", false, p.parseOldStyle());
        
    }
    /**
     * Test of parseOldStyle method, of class StatParser.
     */
    @Test
    public void testParseNoPutStat() {
        System.out.println("testParseNoPutStat");
        setupTestObjects("put(5, 3, 8);");
        scanner.nextToken();
        
        StatParser p = new StatParser(scanner, sym, c, errorHandler);
        assertEquals("Parse ", false, p.parseOldStyle());
        
    }
}
