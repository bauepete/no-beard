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
package nbm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class DataMemoryTest {

    private DataMemory mem;

    public DataMemoryTest() {
    }

    @Before
    public void setUp() {
        mem = new DataMemory(1024);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testStoreWord() {
        int atAddress = 0;
        int value = 42;
        assertTrue(mem.storeWord(atAddress, value));
        assertEquals(value, mem.loadWord(atAddress));
    }
        
    @Test
    public void testStoreWordAtUpperLimitOfMemory() {
        int atAddress = 1020;
        int value = Integer.MAX_VALUE;
        assertTrue(mem.storeWord(atAddress, value));
        assertEquals(value, mem.loadWord(atAddress));
    }
    
    @Test
    public void testStoreWordDataAddressError() {
        int atAddress = 1023;
        int value = Integer.MAX_VALUE;
        assertFalse(mem.storeWord(atAddress, value));
    }
    
    @Test
    public void testLoadWordDataAddressError() {
        int atAddress = 1021;
        assertEquals(-1, mem.loadWord(atAddress));
    }
    
    @Test
    public void testStore() {
        int atAddress = 0;
        byte[] memoryBlock = {17, 42, 127, 91, 79, 53};
        assertTrue(mem.store(atAddress, memoryBlock));
        assertArrayEquals(memoryBlock, mem.load(atAddress, memoryBlock.length));
    }
    
    @Test
    public void testStoreAtUpperLimitOfMemory() {
        byte[] memoryBlock = {17, 42, 127, 91, 79, 53};
        int atAddress = 1024 - memoryBlock.length;
        assertTrue(mem.store(atAddress, memoryBlock));
        assertArrayEquals(memoryBlock, mem.load(atAddress, memoryBlock.length));
    }
    
    @Test
    public void testStoreDataAddressError() {
        byte[] memoryBlock = {17, 42, 127, 91, 79, 53};
        int atAddress = 1024 - memoryBlock.length + 1;
        assertFalse(mem.store(atAddress, memoryBlock));
    }
    
    @Test
    public void testLoadDataAddressError() {
        assertNull(mem.load(1023, 2));
    }
    
    @Test
    public void testStoreStringConstants() {
        byte[] stringConstants = {(byte)'f', (byte) 'o', (byte) 'o', (byte) 'b', (byte) 'a', (byte) 'r', (byte) '!', (byte) 'C'};
        assertEquals(8, mem.storeStringConstants(stringConstants));
        assertArrayEquals(stringConstants, mem.load(0, stringConstants.length));
    }
    
    @Test
    public void testStoreStringConstantsUnalignedToWordLength() {
        byte[] stringConstants = {(byte)'f', (byte) 'o', (byte) 'o'};
        assertEquals(4, mem.storeStringConstants(stringConstants));
        assertArrayEquals(stringConstants, mem.load(0, stringConstants.length));
    }
    
    @Test
    public void testStoreStringConstantsDataAddessError() {
        DataMemory memory = new DataMemory(7);
        byte[] stringConstants = {(byte)'f', (byte) 'o', (byte) 'o', (byte) 'b', (byte) 'a', (byte) 'r', (byte) '!', (byte) 'C'};
        assertEquals(-1, memory.storeStringConstants(stringConstants));
    }
}
