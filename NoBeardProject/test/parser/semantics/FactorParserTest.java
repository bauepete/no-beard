/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import parser.FactorParser;
import parser.general.FactorParserTestSetup;
import symboltable.Operand;
import symboltable.Operand.Kind;

/**
 *
 * @author peter
 */
public class FactorParserTest {

    public FactorParserTest() {
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

        FactorParser p = FactorParserTestSetup.getIdentifierTestSetup();

        assertEquals("Parse ", true, p.parse());
        assertEquals("Operand ", Kind.VARIABLE, p.getOperand().getKind());
        assertEquals("Value ", 32, p.getOperand().getValaddr());
    }

    @Test
    public void testParseNumber() {
        System.out.println("testParseNumber");

        FactorParser p = FactorParserTestSetup.getNumberTestSetup();

        assertEquals("Parse ", true, p.parse());
        assertEquals("Operand ", Kind.CONSTANT, p.getOperand().getKind());
        assertEquals("Value ", 42, p.getOperand().getValaddr());
    }

    @Test
    public void testSingleChar() {
        FactorParser p = FactorParserTestSetup.getSingleCharTestSetup();
        
        assertTrue(p.parse());
        assertEquals(Kind.CONSTANT, p.getOperand().getKind());
        assertEquals(Operand.Type.SIMPLECHAR, p.getOperand().getType());
        assertEquals(0, p.getOperand().getValaddr());
        assertEquals(1, p.getOperand().getSize());
    }
    
    @Test
    public void testParseString() {
        FactorParser p = FactorParserTestSetup.getStringTestSetup();
        
        assertEquals("Parse ", true, p.parse());
        assertEquals("Operand ", Kind.CONSTANT, p.getOperand().getKind());
        assertEquals(Operand.Type.ARRAYCHAR, p.getOperand().getType());
        assertEquals(6, p.getOperand().getSize());
        assertEquals("Value ", 0, p.getOperand().getValaddr());
    }
    
    @Test
    public void testParseExpr() {
        FactorParser p = FactorParserTestSetup.getExprSetup();
        assertEquals("Parse ", true, p.parse());
        assertEquals("Operand ", Kind.VALUEONSTACK, p.getOperand().getKind());
        assertEquals("Value ", 36, p.getOperand().getValaddr());
    }   
}
