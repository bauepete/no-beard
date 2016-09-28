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
package parser;

import error.ErrorHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import machine.CodeGenerator;
import machine.NoBeardMachine;
import scanner.Scanner;
import io.SourceReader;
import scanner.NameManager;
import symboltable.Operand;
import symboltable.SymbolTable;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class ParserFactory {

    private static boolean isReady = false;

    private static ErrorHandler errorHandler;
    private static Scanner scanner;
    private static CodeGenerator codeGenerator;
    private static SymbolTable symbolListManager;

    public static void setup(SourceReader sourceReader, final NameManager nameManager) {
        ParserFactory.errorHandler = new ErrorHandler(sourceReader);
        ParserFactory.scanner = new Scanner(sourceReader, errorHandler, nameManager);
        scanner.nextToken();

        ParserFactory.codeGenerator = new CodeGenerator(NoBeardMachine.MAX_PROG);
        ParserFactory.symbolListManager = new SymbolTable(scanner, errorHandler);

        Operand.setSymListManager(symbolListManager);
        Operand.setErrorHandler(errorHandler);
        Operand.setStringManager(scanner.getStringManager());

        isReady = true;
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

        isReady = true;
    }

    public static void shutDown() {
        isReady = false;
    }

    public static boolean isReady() {
        return isReady;
    }

    public static Scanner getScanner() {
        if (isReady()) {
            return scanner;
        } else {
            return null;
        }
    }

    public static SymbolTable getSymbolListManager() {
        if (isReady()) {
            return symbolListManager;
        } else {
            return null;
        }
    }

    public static CodeGenerator getCodeGenerator() {
        if (isReady()) {
            return codeGenerator;
        } else {
            return null;
        }
    }

    public static ErrorHandler getErrorHandler() {
        if (isReady()) {
            return errorHandler;
        } else {
            return null;
        }
    }

    public static <T extends Parser> T create(Class c) {
        if (isReady()) {
            T t = null;
            try {
                t = (T) c.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(ParserFactory.class.getName()).log(Level.SEVERE, "ParserFactory: Was not able to instantiate parser", ex);
            } finally {
            }
            return t;
        } else {
            return null;
        }
    }
}
