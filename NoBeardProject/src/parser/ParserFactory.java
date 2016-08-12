/*
 * Copyright ©2015, 2016. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
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
package parser;

import error.ErrorHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import nbm.CodeGenerator;
import nbm.NoBeardMachine;
import scanner.Scanner;
import io.SourceReader;
import symboltable.Operand;
import symboltable.SymbolTable;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class ParserFactory {
    private static ErrorHandler errorHandler;
    private static Scanner scanner;
    private static CodeGenerator codeGenerator;
    private static SymbolTable symbolListManager;
    
    
    public static void setup(SourceReader sourceReader) {
        ParserFactory.errorHandler = new ErrorHandler(sourceReader);
        ParserFactory.scanner = new Scanner(sourceReader, errorHandler);
        scanner.nextToken();
        
        ParserFactory.codeGenerator = new CodeGenerator(NoBeardMachine.MAX_PROG);
        ParserFactory.symbolListManager = new SymbolTable(scanner, errorHandler);
        
        Operand.setSymListManager(symbolListManager);
        Operand.setErrorHandler(errorHandler);
        Operand.setStringManager(scanner.getStringManager());
    }
    
    public static void setup(SourceReader sourceReader, ErrorHandler errorHandler, Scanner scanner, CodeGenerator codeGenerator, SymbolTable symbolListManager) {
        ParserFactory.errorHandler = errorHandler;
        ParserFactory.scanner = scanner;
        scanner.nextToken();
        
        ParserFactory.codeGenerator = codeGenerator;
        ParserFactory.symbolListManager = symbolListManager;
        
        Operand.setSymListManager(symbolListManager);
        Operand.setErrorHandler(errorHandler);
        Operand.setStringManager(scanner.getStringManager());
    }
    
    public static Scanner getScanner() {
        return scanner;
    }
    
    public static SymbolTable getSymbolListManager() {
        return symbolListManager;
    }

    public static CodeGenerator getCodeGenerator() {
        return codeGenerator;
    }

    public static ErrorHandler getErrorHandler() {
        return errorHandler;
    }
    
    public static <T extends Parser> T create(Class c) {
        T t = null;
        try {
            t = (T) c.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ParserFactory.class.getName()).log(Level.SEVERE, "ParserFactory: Was not able to instantiate parser", ex);
        }
        finally {
        }
        return t;
    }
}
