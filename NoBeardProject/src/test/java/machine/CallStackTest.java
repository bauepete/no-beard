/*
 * Copyright ©2016. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
 * Department of Informatics and Media Technique, HTBLA Leonding, 
 * Limesstr. 12 - 14, 4060 Leonding, AUSTRIA. All Rights Reserved. Permission
 * to use, copy, modify, and distribute this software and its documentation
 * for educational, research, and not-for-profit purposes, without fee and
 * without a signed licensing agreement, is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies, modifications, and distributions. Contact the Head of
 * Informatics and Media Technique, HTBLA Leonding, Limesstr. 12 - 14,
 * 4060 Leonding, Austria, for commercial licensing opportunities.
 * 
 * IN NO EVENT SHALL HTBLA LEONDING BE LIABLE TO ANY PARTY FOR DIRECT,
 * INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST
 * PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF HTBLA LEONDING HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * HTBLA LEONDING SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE. THE SOFTWARE AND ACCOMPANYING DOCUMENTATION, IF ANY,
 * PROVIDED HEREUNDER IS PROVIDED "AS IS". HTBLA LEONDING HAS NO OBLIGATION
 * TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */
package machine;

import machine.DataMemory;
import machine.CallStack;
import error.ErrorHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class CallStackTest {

    private DataMemory memory;
    private CallStack callStack;

    public CallStackTest() {
    }

    @Before
    public void setUp() {
        memory = new DataMemory(1024, new ErrorHandler(new FakeSourceCodeInfo()));
        int addressOfFirstFrame = 40;
        int sizeOfAuxiliaryCells = 28;
        callStack = new CallStack(memory, addressOfFirstFrame, sizeOfAuxiliaryCells);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testConstruction() {
        assertEquals(40, callStack.getFramePointer());
        assertEquals(68, callStack.getStackPointer());
    }
    
    @Test
    public void setCurrentFramePointer() {
        callStack.setCurrentFramePointer(64);
        assertEquals(64, callStack.getFramePointer());
        assertEquals(92, callStack.getStackPointer());
    }

    @Test
    public void testPush() {
        callStack.push(42);
        assertEquals(72, callStack.getStackPointer());
        assertEquals(42, memory.loadWord(72));
    }
    
    @Test
    public void testPeek() {
        callStack.push(17);
        assertEquals(17, callStack.peek());
        assertEquals(72, callStack.getStackPointer());
    }
    
    @Test
    public void testPop() {
        callStack.push(42);
        int value = callStack.pop();
        assertEquals(42, value);
        assertEquals(68, callStack.getStackPointer());
    }
}
