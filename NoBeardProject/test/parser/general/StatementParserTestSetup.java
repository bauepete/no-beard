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
import parser.StatementParser;
import symboltable.SymbolTable;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class StatementParserTestSetup extends ParserTestSetup {

    public static StatementParser getIntDeclarationTestSetup() {
        setupInfraStructure("int x;");
        getSymListManager().newUnit(1);
        return ParserFactory.create(StatementParser.class);
    }

    public static StatementParser getNoStatementTestSetup() {
        setupInfraStructure("unit foo");
        return ParserFactory.create(StatementParser.class);
    }

    public static StatementParser getCharDeclarationTestSetup() {
        setupInfraStructure("char x;");
        getSymListManager().newUnit(1);
        return ParserFactory.create(StatementParser.class);
    }

    public static StatementParser getBoolDeclarationTestSetup() {
        setupInfraStructure("bool x;");
        getSymListManager().newUnit(1);
        return ParserFactory.create(StatementParser.class);
    }

    public static StatementParser getAssignmentTestSetup() {
        setupInfraStructure("x = 3;");
        getSymListManager().newUnit(1);
        getSymListManager().newVar(0, SymbolTable.ElementType.INT, 1);
        return ParserFactory.create(StatementParser.class);
    }

    public static StatementParser getPutTestSetup() {
        setupInfraStructure("put(3 + 5, 42);");
        return ParserFactory.create(StatementParser.class);
    }

    public static StatementParser getPutLnTestSetup() {
        setupInfraStructure("putln;");
        return ParserFactory.create(StatementParser.class);
    }
}
