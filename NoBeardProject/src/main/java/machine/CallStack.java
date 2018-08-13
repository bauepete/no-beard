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
class CallStack {

    private final DataMemory memory;
    private int currentFramePointer;
    private int currentStackPointer;
    private final int sizeOfAuxiliaryCells;

    /**
     * Creates a new call stack.
     * @param memory The underlying memory on which the call stack is built.
     * @param addressOfFirstFrame The address where the first frame starts.
     * @param sizeOfAuxiliaryCells The number of bytes reserved for auxiliary data,
     */
    CallStack(DataMemory memory, int addressOfFirstFrame, int sizeOfAuxiliaryCells) {
        this.memory = memory;
        this.sizeOfAuxiliaryCells = sizeOfAuxiliaryCells;
        setFrameAndStackPointer(addressOfFirstFrame, sizeOfAuxiliaryCells);
    }

    private void setFrameAndStackPointer(int addressOfFirstFrame, int sizeOfAuxiliaryCells) {
        this.currentFramePointer = addressOfFirstFrame;
        this.currentStackPointer = addressOfFirstFrame + sizeOfAuxiliaryCells;
    }

    /**
     * setCurrentFramePointer sets the start address of the current frame.
     * @param currentFramePointer The new start address of the frame
     * @implSpec The default implementation also sets the start address of the stack pointer
     * relatively to the start of the current frame
     */
    void setCurrentFramePointer(int currentFramePointer) {
        setFrameAndStackPointer(currentFramePointer, sizeOfAuxiliaryCells);
    }

    /**
     *
     * @return The start address of the current frame.
     */
    int getFramePointer() {
        return currentFramePointer;
    }

    /**
     *
     * @return The address of the top of stack of the current frame.
     */
    int getStackPointer() {
        return currentStackPointer;
    }

    /**
     * Sets the address of the last used word of the stack.
     * @param currentStackPointer The address of the last used word.
     */
    void setStackPointer(int currentStackPointer) {
        this.currentStackPointer = currentStackPointer;
    }

    /**
     * Pushes a value on the stack if possible.
     * @param value The value to be pushed.
     */
    void push(int value) {
        currentStackPointer += 4;
        memory.storeWord(currentStackPointer, value);
    }

    /**
     *
     * @return The top value of the stack
     */
    int peek() {
        return memory.loadWord(currentStackPointer);
    }

    /**
     * Removes the top value of the stack.
     * @return The top value of the stack.
     */
    int pop() {
        int value = peek();
        currentStackPointer -= 4;
        return value;
    }
}
