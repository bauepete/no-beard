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
package parser.general;

import parser.ParserFactory;
import parser.VariableDeclarationParser;
import static parser.general.ParserTestSetup.setupInfraStructure;
import symboltable.SymbolTable;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class VariableDeclarationParserTestSetup extends ParserTestSetup {

    public static VariableDeclarationParser getSimpleIntTestSetup() {
        return setupTestObjectsAndParser("int x;");
    }

    private static VariableDeclarationParser setupTestObjectsAndParser(final String srcLine) {
        setupInfraStructure(srcLine);
        symListManager.newUnit(1);
        VariableDeclarationParser instance = ParserFactory.create(VariableDeclarationParser.class);
        return instance;
    }

    public static VariableDeclarationParser getSimpleCharTestSetup() {
        return setupTestObjectsAndParser("char x;");
    }

    public static VariableDeclarationParser getInvalidDeclarationTestSetup() {
        return setupTestObjectsAndParser("x;");
    }

    public static VariableDeclarationParser getSimpleBoolTestSetup() {
        return setupTestObjectsAndParser("bool x;");
    }

    public static VariableDeclarationParser getIntArrayTestSetup() {
        return setupTestObjectsAndParser("int[42] x;");
    }

    public static VariableDeclarationParser getInvalidArrayTestSetup() {
        return setupTestObjectsAndParser("int[k] x;");
    }

    public static VariableDeclarationParser getNameAlreadyDefinedSetup() {
        VariableDeclarationParser p = setupTestObjectsAndParser("int x;");
        symListManager.newVar(0, SymbolTable.ElementType.INT, 1);
        return p;
    }

    public static VariableDeclarationParser getNegativeArraySizeSetup() {
        VariableDeclarationParser p = setupTestObjectsAndParser("bool[-5] x;");
        return p;
    }

    public static VariableDeclarationParser getDeclarationAndInitializationSetup() {
        VariableDeclarationParser p = setupTestObjectsAndParser("int x = 17;");
        return p;
    }

    public static VariableDeclarationParser getStringDeclarationAndInitializationSetup() {
        VariableDeclarationParser p = setupTestObjectsAndParser("char[11] x = 'Hello World';");
        return p;
    }
}
