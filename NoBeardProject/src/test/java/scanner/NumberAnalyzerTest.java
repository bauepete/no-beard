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

import io.SourceStringReader;
import io.SourceReader;
import error.ErrorHandler;
import error.Error.ErrorType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class NumberAnalyzerTest {
    
    private SourceReader sourceReader;
    private ErrorHandler errorHandler;
    
    public NumberAnalyzerTest() {
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
        prepareTestSetup("42;");
        
        int expResult = 42;
        int result = NumberAnalyzer.readNumber(sourceReader, errorHandler);
        assertEquals(expResult, result);
        assertEquals(';', sourceReader.getCurrentChar());
    }
    
    private void prepareTestSetup(String source) {
        sourceReader = new SourceStringReader(source);
        errorHandler = new ErrorHandler(sourceReader);
        sourceReader.nextChar();
    }
    
    @Test
    public void testLargestNumber() {
        prepareTestSetup(Integer.toString(NumberAnalyzer.MAX_INTEGER) + ";");
        int result = NumberAnalyzer.readNumber(sourceReader, errorHandler);
        assertEquals(NumberAnalyzer.MAX_INTEGER, result);
        assertEquals(';', sourceReader.getCurrentChar());
    }
    
    /**
     * Test of readNumber method, of class NumberAnalyzer.
     */
    @Test
    public void testReadNumberOverflow() {
        System.out.println("readNumberOverflow");
        String source = Integer.toString(NumberAnalyzer.MAX_INTEGER + 1) + ";";
        prepareTestSetup(source);
        int result = NumberAnalyzer.readNumber(sourceReader, errorHandler);
        assertEquals(1, errorHandler.getCount());
        assertEquals(ErrorType.INTEGER_OVERFLOW.getNumber(), errorHandler.getLastError().getNumber());
        assertEquals(';', sourceReader.getCurrentChar());
    }
}
