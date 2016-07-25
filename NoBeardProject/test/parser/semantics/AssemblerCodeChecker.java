/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class AssemblerCodeChecker {
    
    public static void assertCodeEquals(String msg, byte[] exp, byte[] act) {
        for (int i = 0; i < Math.min(exp.length, act.length); i++) {
            assertEquals("Byte " + i, exp[i], act[i]);
        }
    }
    
    public static void assertCodeEquals(byte[] expected, byte[] actual) {
        assertCodeEquals("", expected, actual);
    }
}
