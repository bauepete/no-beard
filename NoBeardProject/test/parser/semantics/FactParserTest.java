/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import parser.FactorParser;
import parser.general.FactParserTestSetup;
import symlist.Operand.OperandKind;

/**
 *
 * @author peter
 */
public class FactParserTest {

    public FactParserTest() {
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
     * Test of parseOldStyle method, of class FactorParser.
     */
    @Test
    public void testParseIdentifier() {
        System.out.println("testParseIdentifier");

        FactorParser p = FactParserTestSetup.getIdentifierTestSetup();

        assertEquals("Parse ", true, p.parse());
        assertEquals("Operand ", OperandKind.VARIABLE, p.getOperand().getKind());
        assertEquals("Value ", 32, p.getOperand().getValaddr());
    }

    @Test
    public void testParseNumber() {
        System.out.println("testParseNumber");

        FactorParser p = FactParserTestSetup.getNumberTestSetup();

        assertEquals("Parse ", true, p.parse());
        assertEquals("Operand ", OperandKind.CONSTANT, p.getOperand().getKind());
        assertEquals("Value ", 42, p.getOperand().getValaddr());
    }

    @Test
    public void testParseString() {
        System.err.append("testParseString");

        FactorParser p = FactParserTestSetup.getStringTestSetup();
        
        assertEquals("Parse ", true, p.parse());
        assertEquals("Operand ", OperandKind.CONSTANT, p.getOperand().getKind());
        assertEquals("Value ", 0, p.getOperand().getValaddr());
    }
    
    @Test
    @Ignore
    public void testParseExpr() {
        System.err.append("testParseExpr");
        
        FactorParser p = FactParserTestSetup.getExprSetup();
        assertEquals("Parse ", true, p.parse());
        assertEquals("Operand ", OperandKind.VALUEONSTACK, p.getOperand().getKind());
        assertEquals("Value ", 36, p.getOperand().getValaddr());
    }   
}
