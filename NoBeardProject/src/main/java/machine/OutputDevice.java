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

/**
 *
 * @author Peter Bauer (p.bauer@htl-leonding.ac.at)
 * Defines a contract every output device has to fulfill if it aims to support
 * the NoBeard Machine
 */
public interface OutputDevice {
    /**
     * Prints an integer value to the output device. If columnwidth is greater
     * than the number of digits in value the value is filled with leading blanks
     * that the value takes the size of columnwidth.
     * @param value – The value to be printed
     * @param columnwidth – The required column width
     */
    void printInt(int value, int columnwidth);
    
    /**
     * Prints a char to the output device. If columnwidth is greater
     * than 1 columnwidth - 1 trailing blanks are added (right aligned)
     * @param character – The character to be printed
     * @param columnwidth – The required column width
     */
    void printChar(char character, int columnwidth);
    
    /**
     * Prints a string to the output device. If columnwidth is greater
     * than the length of the string (strlen) columnwidth - strlen
     * trailing blanks are added (right aligned)
     * @param str – The string to be printed
     * @param columnwidth – The required column width
     */
    void print(String str, int columnwidth);
    
    /**
     * Prints a newline to the output device. 
     */
    void println();
}
