/*
 * Copyright ©2017. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
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

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

/**
 *
 * @author Peter Bauer (p.bauer@htl-leonding.ac.at)
 * Defines a contract every input device has to fulfill if it aims to support
 * the NoBeard Machine
 */
public interface InputDevice {
    /**
     * hasNext() checks whether there exists a further input.
     * @return true if there exists an input and false otherwise
     */
    boolean hasNext();
    
    /**
     * nextChar() returns the next character on the input device
     * @throws NoSuchElementException - if no more tokens are available
     * @return the next char
     */
    char nextChar() throws NoSuchElementException;
    
    /**
     * hasNextInt() checks whether the input device has a further int available
     * @return true if an int is available, false otherwise
     */
    boolean hasNextInt();
    
    /**
     * returns the next input as int.
     * @throws InputMismatchException – if the scanned token is no int
     * NoSuchElementException - if no more tokens are available
     * @return the next int scanned from input
     */
    int nextInt() throws InputMismatchException, NoSuchElementException;
}
