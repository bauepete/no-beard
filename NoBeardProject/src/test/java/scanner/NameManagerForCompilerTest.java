/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import io.SourceStringReader;
import io.SourceReader;
import scanner.Scanner.Symbol;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class NameManagerForCompilerTest {

    private SourceReader sr;
    private SourceReader srMany;
    private SourceReader srKeywords;
    private Token token;
    private NameManagerForCompiler nameManager;
    private NameManagerForCompiler nameManagerMany;
    private NameManagerForCompiler nmKeywords;

    public NameManagerForCompilerTest() {
    }

    @Before
    public void setUp() {
        sr = new SourceStringReader("var1; var2;");
        srMany = new SourceStringReader("var1; var2; bla; blu; var2; blu;");
        srKeywords = new SourceStringReader("put, putln, unit, do, done, if, else, int, bool, char, true, false,");
        token = new Token();
        nameManager = new NameManagerForCompiler(sr);
        nameManagerMany = new NameManagerForCompiler(srMany);
        nmKeywords = new NameManagerForCompiler(srKeywords);
    }

    @Test
    public void testIsAPossibleStartOfName() {
        assertTrue(nameManager.isAPossibleStartOfName('a'));
        assertTrue(nameManager.isAPossibleStartOfName('z'));
        assertTrue(nameManager.isAPossibleStartOfName('_'));
        assertTrue(nameManager.isAPossibleStartOfName('$'));
        assertFalse(nameManager.isAPossibleStartOfName('0'));
        assertFalse(nameManager.isAPossibleStartOfName('.'));
    }

    /**
     * Test of readName method, of class NameManagerForCompiler.
     */
    @Test
    public void testReadName() {
        sr.nextChar();
        nameManager.readName(token);

        assertEquals(Symbol.IDENTIFIER, token.getSymbol());
        assertEquals(0, token.getValue());
        assertEquals("var1", token.getClearName());
        assertEquals("Current char ", ';', sr.getCurrentChar());

        sr.nextChar();
        sr.nextChar();

        nameManager.readName(token);
        assertEquals("IDENT ", Symbol.IDENTIFIER, token.getSymbol());
        assertEquals("Spix ", 1, token.getValue());
        assertEquals("var2", token.getClearName());
        assertEquals("Current char ", ';', sr.getCurrentChar());
    }

    @Test
    public void testReadWeirdNames() {
        SourceReader srWeird = new SourceStringReader("_;__;_$;$_;$$");
        srWeird.nextChar();
        NameManagerForCompiler nm = new NameManagerForCompiler(srWeird);

        nm.readName(token);
        assertEquals(Symbol.IDENTIFIER, token.getSymbol());
        assertEquals("_", token.getClearName());

        srWeird.nextChar();
        nm.readName(token);
        assertEquals(Symbol.IDENTIFIER, token.getSymbol());
        assertEquals("__", token.getClearName());

        srWeird.nextChar();
        nm.readName(token);
        assertEquals(Symbol.IDENTIFIER, token.getSymbol());
        assertEquals("_$", token.getClearName());

        srWeird.nextChar();
        nm.readName(token);
        assertEquals(Symbol.IDENTIFIER, token.getSymbol());
        assertEquals("$_", token.getClearName());

        srWeird.nextChar();
        nm.readName(token);
        assertEquals(Symbol.IDENTIFIER, token.getSymbol());
        assertEquals("$$", token.getClearName());
    }
    
    @Test
    public void testReadNameDouble() {
        srMany.nextChar();
        nameManagerMany.readName(token); // var1
        srMany.nextChar();
        srMany.nextChar();

        nameManagerMany.readName(token); // var2
        int var2Spix = token.getValue();
        srMany.nextChar();
        srMany.nextChar();

        nameManagerMany.readName(token); // bla
        srMany.nextChar();
        srMany.nextChar();

        nameManagerMany.readName(token); // blu
        int bluSpix = token.getValue();
        srMany.nextChar();
        srMany.nextChar();

        nameManagerMany.readName(token); // var2
        srMany.nextChar();
        srMany.nextChar();
        assertEquals(var2Spix, token.getValue());

        nameManagerMany.readName(token); // blu
        srMany.nextChar();
        srMany.nextChar();
        assertEquals(bluSpix, token.getValue());
    }

    /**
     * Test of getStringName method, of class NameManagerForCompiler.
     */
    @Test
    public void testGetStringName() {
        System.out.println("getStringName");

        sr.nextChar();
        nameManager.readName(token);

        assertEquals("Name ", "var1", nameManager.getStringName(token.getValue()));

        sr.nextChar();
        sr.nextChar();

        nameManager.readName(token);

        assertEquals("Name ", "var2", nameManager.getStringName(token.getValue()));
    }

    @Test
    public void testKeywords() {
        System.out.println("testKeywords");

        srKeywords.nextChar();
        nmKeywords.readName(token);

        Symbol[] expTokens = {Symbol.PUT, Symbol.PUTLN, Symbol.UNIT, Symbol.DO,
            Symbol.DONE, Symbol.IF, Symbol.ELSE, Symbol.INT, Symbol.BOOL,
            Symbol.CHAR, Symbol.TRUE, Symbol.FALSE};

        for (Symbol s : expTokens) {
            assertEquals(s, token.getSymbol());
            assertEquals(',', srKeywords.getCurrentChar());
            srKeywords.nextChar();
            srKeywords.nextChar();
            nmKeywords.readName(token);
        }

    }
}
