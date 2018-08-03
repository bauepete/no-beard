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

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class CallStack {

    private final DataMemory memory;
    private int currentFramePointer;
    private int currentStackPointer;
    private final int sizeOfAuxiliaryCells;

    /**
     * Creates a new call stack.
     * @param memory The underlying memory on which the call stack is built.
     * @param addressOfFirstFrame The address where the first frame starts.
     * @param sizeOfAuxiliaryCells The number of bytes reserved for auxiliary data,
     * like return address, static link, etc., for each frame.
     */
    public CallStack(DataMemory memory, int addressOfFirstFrame, int sizeOfAuxiliaryCells) {
        this.memory = memory;
        this.sizeOfAuxiliaryCells = sizeOfAuxiliaryCells;
        setFrameAndStackPointer(addressOfFirstFrame, sizeOfAuxiliaryCells);
    }

    private void setFrameAndStackPointer(int addressOfFirstFrame, int sizeOfAuxiliaryCells) {
        this.currentFramePointer = addressOfFirstFrame;
        this.currentStackPointer = addressOfFirstFrame + sizeOfAuxiliaryCells;
    }

    public void setCurrentFramePointer(int currentFramePointer) {
        setFrameAndStackPointer(currentFramePointer, sizeOfAuxiliaryCells);
    }

    public int getFramePointer() {
        return currentFramePointer;
    }

    public int getStackPointer() {
        return currentStackPointer;
    }

    public void setStackPointer(int currentStackPointer) {
        this.currentStackPointer = currentStackPointer;
    }

    void push(int value) {
        currentStackPointer += 4;
        memory.storeWord(currentStackPointer, value);
    }
    
    int peek() {
        return memory.loadWord(currentStackPointer);
    }

    int pop() {
        int value = peek();
        currentStackPointer -= 4;
        return value;
    }
}
