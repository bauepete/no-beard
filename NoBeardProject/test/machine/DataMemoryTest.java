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

import error.ErrorHandler;
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
    private ErrorHandler errorHandler;

    public DataMemoryTest() {
    }

    @Before
    public void setUp() {
        errorHandler = new ErrorHandler(new FakeSourceCodeInfo());
        mem = new DataMemory(1024, errorHandler);
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void testStoreByte() {
        int atAddress = 0;
        byte value = 17;
        mem.storeByte(atAddress, value);
        byte retrievedValue = mem.loadByte(atAddress);
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testStoreByteAtUpperLimitOfMemory() {
        int atAddress = 1023;
        byte value = (byte) 255;
        mem.storeByte(atAddress, value);
        byte retrievedValue = mem.loadByte(atAddress);
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testStoreByteAtTooHighAddress() {
        int atAddress = 1024;
        byte value = (byte) 255;
        mem.storeByte(atAddress, value);
        assertDataAddressError();
    }
    
    private void assertDataAddressError() {
        assertEquals(error.Error.ErrorType.DATA_ADDRESS_ERROR.getNumber(), errorHandler.getLastError().getNumber());
    }
    
    @Test
    public void testStoreByteAtTooLowAddress() {
        int atAddress = -1;
        byte value = (byte) 255;
        mem.storeByte(atAddress, value);
        assertDataAddressError();
    }
    
    @Test
    public void testLoadByteAtTooHighAddress() {
        int atAddress = 1024;
        byte retrievedValue = mem.loadByte(atAddress);
        assertDataAddressError();
    }
    
    @Test
    public void testStoreAtTooLowAddress() {
        int atAddress = -1;
        mem.loadByte(atAddress);
        assertDataAddressError();
    }

    @Test
    public void testStoreWord() {
        int atAddress = 0;
        int value = 42;
        mem.storeWord(atAddress, value);
        assertEquals(value, mem.loadWord(atAddress));
    }
    
    @Test
    public void testStoreNegativeWord() {
        int atAddress = 0;
        int value = -17;
        mem.storeWord(atAddress, value);
        assertEquals(value, mem.loadWord(atAddress));
    }
    
    @Test
    public void testStoreWordWithMSBSetInTheMiddle() {
        mem.storeWord(0, 128);
        assertEquals(128, mem.loadWord(0));
    }
    
    @Test
    public void testStoreWordAtUpperLimitOfMemory() {
        int atAddress = 1020;
        int value = Integer.MAX_VALUE;
        mem.storeWord(atAddress, value);
        assertEquals(value, mem.loadWord(atAddress));
    }
    
    @Test
    public void testStoreWordAtTooHighAddress() {
        int atAddress = 1023;
        int value = Integer.MAX_VALUE;
        mem.storeWord(atAddress, value);
        assertDataAddressError();
    }
    
    @Test
    public void testStoreWordAtTooLowAddress() {
        int atAddress = -1;
        int value = Integer.MAX_VALUE;
        mem.storeWord(atAddress, value);
        assertDataAddressError();
    }

    @Test
    public void testLoadWordAtTooHighAddress() {
        int atAddress = 1021;
        int retrievedValue = mem.loadWord(atAddress);
        assertDataAddressError();
    }
    
    @Test
    public void testLoadWordAtTooLowAddress() {
        int atAddress = -1;
        mem.loadWord(atAddress);
        assertDataAddressError();
    }
    
    @Test
    public void testStore() {
        int atAddress = 0;
        byte[] memoryBlock = {17, 42, 127, 91, 79, 53};
        mem.store(atAddress, memoryBlock);
        assertArrayEquals(memoryBlock, mem.load(atAddress, memoryBlock.length));
    }
    
    @Test
    public void testStoreAtUpperLimitOfMemory() {
        byte[] memoryBlock = {17, 42, 127, 91, 79, 53};
        int atAddress = 1024 - memoryBlock.length;
        mem.store(atAddress, memoryBlock);
        assertArrayEquals(memoryBlock, mem.load(atAddress, memoryBlock.length));
    }
    
    @Test
    public void testStoreDataAddressError() {
        byte[] memoryBlock = {17, 42, 127, 91, 79, 53};
        int atAddress = 1024 - memoryBlock.length + 1;
        mem.store(atAddress, memoryBlock);
        assertDataAddressError();
    }
    
    @Test
    public void testLoadDataAddressError() {
        assertNull(mem.load(1023, 2));
        assertDataAddressError();
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
    public void testStoreStringConstantsDataAddressError() {
        DataMemory memory = new DataMemory(7, errorHandler);
        byte[] stringConstants = {(byte)'f', (byte) 'o', (byte) 'o', (byte) 'b', (byte) 'a', (byte) 'r', (byte) '!', (byte) 'C'};
        assertEquals(-1, memory.storeStringConstants(stringConstants));
        assertDataAddressError();
    }
    
    @Test
    public void testCopyBlock() {
        byte[] stringConstants = {(byte)'f', (byte) 'o', (byte) 'o', (byte) 'b', (byte) 'a', (byte) 'r', (byte) '!', (byte) 'C'};
        mem.storeStringConstants(stringConstants);
        int fromAddress = 0;
        int length = stringConstants.length;
        int toAddress = 1024 - 8;
        mem.copyBlock(fromAddress, toAddress, length);
        assertArrayEquals(stringConstants, mem.load(toAddress, stringConstants.length));
    }
    
    @Test
    public void testCopyBlockDataAddressError() {
        byte[] stringConstants = {(byte)'f', (byte) 'o', (byte) 'o', (byte) 'b', (byte) 'a', (byte) 'r', (byte) '!', (byte) 'C'};
        mem.storeStringConstants(stringConstants);
        int fromAddress = 0;
        int length = stringConstants.length;
        int toAddress = 1024 - 7;
        mem.copyBlock(fromAddress, toAddress, length);
        assertDataAddressError();
    }
}
