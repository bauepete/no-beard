/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symboltable;

import symboltable.Operand.Type;
import scanner.Scanner;
import symboltable.Operand.Kind;
import error.ErrorHandler;
import error.Error;
import error.Error.ErrorType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.SourceStringReader;
import static org.junit.Assert.*;
import org.junit.Ignore;
import io.SourceReader;
import scanner.NameManagerForCompiler;

/**
 *
 * @author peter
 */
public class SymbolTableTest {

    private ErrorHandler errorHandler;
    private SymbolTable symbolTable;

    public SymbolTableTest() {
    }

    @Before
    public void setUp() {
        SourceStringReader sr = new SourceStringReader("TestUnit; aName");
        errorHandler = new ErrorHandler(sr);
        Scanner scanner = prepareScanner(sr);
        symbolTable = new SymbolTable(scanner, errorHandler);

        symbolTable.newUnit(0);
    }
    
    private Scanner prepareScanner(SourceReader sr) {
        Scanner s = new Scanner(sr, errorHandler, new NameManagerForCompiler(sr));
        s.nextToken();
        s.nextToken();
        s.nextToken();
        return s;
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of newUnit method, of class SymbolTable.
     */
    @Test
    public void testNewUnit() {
        assertEquals(Kind.UNIT, symbolTable.getCurrBlock().getKind());
        assertEquals(Type.VOID, symbolTable.getCurrBlock().getType());
        assertEquals(1, symbolTable.getCurrLevel());
        SymbolTableEntry unitObj = symbolTable.findObject(0);
        assertEquals(unitObj.getKind(), Kind.UNIT);
    }

    @Test
    public void testNestedUnitsFail() {
        symbolTable.newUnit(1);
        assertEquals(1, symbolTable.getCurrLevel());
        assertEquals(Kind.UNIT, symbolTable.getCurrBlock().getKind());
        assertEquals(ErrorType.NO_NESTED_MODULES.getNumber(), errorHandler.getLastError().getNumber());
    }

    @Test
    public void testNewSimpleVar() {
        System.out.println("newSimpleVar");

        symbolTable.newVar(1, SymbolTable.ElementType.INT);
        assertEquals("datAddr expected ", 36, symbolTable.getDatAddr());
        assertEquals(4, symbolTable.getCurrBlock().getSize());

        symbolTable.newVar(2, SymbolTable.ElementType.BOOL);
        assertEquals("datAddr expected ", 40, symbolTable.getDatAddr());
        assertEquals(8, symbolTable.getCurrBlock().getSize());

        symbolTable.newVar(3, SymbolTable.ElementType.CHAR);
        assertEquals(44, symbolTable.getDatAddr());
        assertEquals(9, symbolTable.getCurrBlock().getSize());
    }

    @Test
    public void testNewArrayVar() {
        System.out.print("testNewArrayVar");

        symbolTable.newVar(1, SymbolTable.ElementType.INT, 10);
        assertEquals(72, symbolTable.getDatAddr());
        assertEquals(40, symbolTable.getCurrBlock().getSize());

        symbolTable.newVar(2, SymbolTable.ElementType.BOOL, 5);
        assertEquals(92, symbolTable.getDatAddr());
        assertEquals(60, symbolTable.getCurrBlock().getSize());

        symbolTable.newVar(3, SymbolTable.ElementType.CHAR, 15);
        assertEquals(108, symbolTable.getDatAddr());
        assertEquals(75, symbolTable.getCurrBlock().getSize());
    }

    @Test
    public void testNewVarFail() {
        System.out.println("testNewVarFail");

        symbolTable.newVar(0, SymbolTable.ElementType.INT);
        assertEquals(4, symbolTable.getCurrBlock().getSize());
        assertEquals(36, symbolTable.getDatAddr());

        symbolTable.newVar(0, SymbolTable.ElementType.BOOL);
        assertEquals(4, symbolTable.getCurrBlock().getSize());
        assertEquals(36, symbolTable.getDatAddr());
    }

    @Test
    public void testNewBlock() {
        System.out.println("testNewBlock");

        symbolTable.newVar(1, SymbolTable.ElementType.INT);
        symbolTable.newBlock();
        assertEquals(2, symbolTable.getCurrLevel());

        symbolTable.newVar(1, SymbolTable.ElementType.INT);
        assertEquals(2, symbolTable.getCurrLevel());
        assertEquals(0, errorHandler.getCount());
        assertEquals(4, symbolTable.getCurrBlock().getSize());
    }

    @Test
    public void testNewFuncFail() {
        System.out.println("testNewFuncFail");

        symbolTable.newVar(1, SymbolTable.ElementType.INT);
        symbolTable.newFunc(1, Type.SIMPLEBOOL);
        assertEquals(ErrorType.NAME_ALREADY_DEFINED.getNumber(), errorHandler.getLastError().getNumber());
        assertEquals(1, symbolTable.getCurrLevel());
        assertEquals(0, symbolTable.getCurrBlock().getName());
        assertEquals(Kind.UNIT, symbolTable.getCurrBlock().getKind());
    }

    @Test
    public void testNewFunc() {
        System.out.println("testNewFunc");

        symbolTable.newFunc(1, Type.ARRAYINT);
        assertEquals(2, symbolTable.getCurrLevel());
        assertEquals(32, symbolTable.getDatAddr());
    }

    @Test
    public void testEndBlock() {
        System.out.println("testEndBlock");

        symbolTable.newVar(0, SymbolTable.ElementType.INT);
        symbolTable.newFunc(1, Type.VOID);
        symbolTable.newVar(0, SymbolTable.ElementType.CHAR);
        symbolTable.newBlock();
        symbolTable.newVar(0, SymbolTable.ElementType.INT);
        symbolTable.newVar(1, SymbolTable.ElementType.INT, 5);

        // Test whether setup is correct
        assertEquals(0, errorHandler.getCount());
        assertEquals(3, symbolTable.getCurrLevel());
        assertEquals(24, symbolTable.getCurrBlock().getSize());
        assertEquals(56, symbolTable.getDatAddr());

        symbolTable.endBlock();
        assertEquals(2, symbolTable.getCurrLevel());
        assertEquals(1, symbolTable.getCurrBlock().getSize());
        assertEquals(36, symbolTable.getDatAddr());

        symbolTable.endBlock();
        assertEquals(1, symbolTable.getCurrLevel());
        assertEquals(4, symbolTable.getCurrBlock().getSize());
        assertEquals(36, symbolTable.getDatAddr());

        symbolTable.endBlock();
        assertEquals(null, symbolTable.getCurrBlock());
    }

    @Test
    public void testTwoModulesHavingSameName() {
        symbolTable.endBlock();
        
        symbolTable.newUnit(0);
        assertEquals("SemErr ", 1, errorHandler.getCount(Error.ErrorClass.SEMANTICAL));
        assertEquals("Errors count", 1, errorHandler.getCount());
        assertEquals("Error ", ErrorType.NAME_ALREADY_DEFINED.getNumber(), errorHandler.getLastError().getNumber());
    }

    @Test
    public void testAlignDatAddrTo4() {
        System.out.println("alignDatAddrTo4");

        symbolTable.newVar(2, SymbolTable.ElementType.CHAR);
        assertEquals("datAddr expected ", 36, symbolTable.getDatAddr());
    }

    @Test
    public void testGetObject() {
        System.out.println("testGetObject");

        symbolTable.newVar(1, SymbolTable.ElementType.CHAR);
        symbolTable.newVar(2, SymbolTable.ElementType.INT);

        assertEquals("Kind expected: ", Operand.Kind.UNIT, symbolTable.findObject(0).getKind());
        assertEquals("Type expected: ", Operand.Type.VOID, symbolTable.findObject(0).getType());
        assertEquals("Size expected: ", 5, symbolTable.findObject(0).getSize());

        assertEquals("Kind expected: ", Operand.Kind.VARIABLE, symbolTable.findObject(1).getKind());
        assertEquals("Type expected: ", Operand.Type.SIMPLECHAR, symbolTable.findObject(1).getType());
        assertEquals("Size expected: ", 1, symbolTable.findObject(1).getSize());
        assertEquals("Address expected: ", 32, symbolTable.findObject(1).getAddr());

        assertEquals("Kind expected: ", Operand.Kind.VARIABLE, symbolTable.findObject(2).getKind());
        assertEquals("Type expected: ", Operand.Type.SIMPLEINT, symbolTable.findObject(2).getType());
        assertEquals("Size expected: ", 4, symbolTable.findObject(2).getSize());
        assertEquals("Address expected: ", 36, symbolTable.findObject(2).getAddr());

        assertEquals("Kind ", Operand.Kind.ILLEGAL, symbolTable.findObject(4).getKind());
    }

    @Test()
    @Ignore
    public void testDefineProcStart() {
        System.out.println("defineProcStart");

        symbolTable.newVar(1, SymbolTable.ElementType.INT);

        SymbolTableEntry u = symbolTable.findObject(0);
        symbolTable.defineFuncStart(u, 2);

        assertEquals("Start PC ", 2, symbolTable.findObject(0).getAddr());

        u = symbolTable.findObject(1);
        symbolTable.defineFuncStart(u, 2);
        assertEquals("SemErr ", 1, errorHandler.getCount(Error.ErrorClass.SEMANTICAL));
        assertEquals("Addr of var ", 32, u.getAddr());
    }
}
