/*
 * <one line to give the program's name and a brief idea of what it does.>
 * Copyright (C) 2012  Peter Bauer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package scanner;

import error.ErrorHandler;
import error.Error;
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
public class NumberAnalyzerTest {
    
    public NumberAnalyzerTest() {
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
     * Test of readNumber method, of class NumberAnalyzer.
     */
    @Test
    public void testReadNumber() {
        System.out.println("readNumber");
        SrcReader sr = new SrcStringReader("42;");
        sr.nextChar();
        
        int expResult = 42;
        int result = NumberAnalyzer.readNumber(sr);
        assertEquals(expResult, result);
        assertEquals(';', sr.getCurrentChar());
    }
    
    /**
     * Test of readNumber method, of class NumberAnalyzer.
     */
    @Test
    public void testReadNumberOverflow() {
        System.out.println("readNumberOverflow");
        SrcReader sr = new SrcStringReader("65536;");
        ErrorHandler.getInstance().reset();
        Error.setScanner(new Scanner(sr));

        int result = NumberAnalyzer.readNumber(sr);
        assertEquals(1, ErrorHandler.getInstance().getCount());
        assertEquals(1, ErrorHandler.getInstance().getLastError().getErrNo());
        assertEquals(';', sr.getCurrentChar());
    }
}
