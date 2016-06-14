/*
 * Copyright ©2015. Created by P. Bauer (p.bauer@htl-leonding.ac.at), Department
 * of Informatics and Media Technique, HTBLA Leonding, Limesstr. 12 - 14,
 * 4060 Leonding, AUSTRIA. All Rights Reserved. Permission to use, copy, modify,
 * and distribute this software and its documentation for educational,
 * research, and not-for-profit purposes, without fee and without a signed
 * licensing agreement, is hereby granted, provided that the above copyright
 * notice, this paragraph and the following two paragraphs appear in all
 * copies, modifications, and distributions. Contact the Head of Informatics,
 * Media Technique and Design, HTBLA Leonding, Limesstr. 12 - 14, 4060 Leonding,
 * Austria, for commercial licensing opportunities.
 * 
 * IN NO EVENT SHALL HTBLA LEONDING BE  LIABLE TO ANY PARTY FOR DIRECT,
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
package parser;

import error.ErrorHandler;
import nbm.Code;
import nbm.Nbm;
import scanner.Scanner;
import scanner.SrcReader;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class ParserFactory {
    private static SrcReader sourceReader;
    private static ErrorHandler errorHandler;
    private static Scanner scanner;
    private static Code codeGenerator;
    private static SymListManager symbolListManager;
    
    
    public static void setup(SrcReader sourceReader) {
        ParserFactory.sourceReader = sourceReader;
        ParserFactory.errorHandler = new ErrorHandler(sourceReader);
        ParserFactory.scanner = new Scanner(sourceReader, errorHandler);
        scanner.nextToken();
        
        ParserFactory.codeGenerator = new Code(Nbm.getMAXPROG());
        ParserFactory.symbolListManager = new SymListManager(codeGenerator, scanner, errorHandler);
        
        Operand.setSymListManager(symbolListManager);
        Operand.setErrorHandler(errorHandler);
        Operand.setStringManager(scanner.getStringManager());
    }
    
    static Scanner getScanner() {
        return scanner;
    }
    
    public static SymListManager getSymbolListManager() {
        return symbolListManager;
    }

    public static Code getCodeGenerator() {
        return codeGenerator;
    }

    static ErrorHandler getErrorHandler() {
        return errorHandler;
    }
    
    public static Parser createAssignmentParser() {
        return new AssignmentParser(scanner, symbolListManager, codeGenerator, errorHandler);
    }
}
