/*
 * Copyright ©2011 - 2016. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
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
package compiler;

import java.io.FileNotFoundException;
import error.Error;
import parser.NoBeardParser;
import parser.Parser;
import parser.ParserFactory;
import io.SourceFileReader;
import io.SourceReader;
import io.SourceStringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import scanner.NameManagerForCompiler;

/**
 *
 * @author peter
 */
public class NoBeardCompiler {

    private static SourceReader sourceReader;
    private static Parser parserl;
    private static boolean sourceIsAvailable;

    public static void setSourceString(String source) {
        NoBeardCompiler.sourceReader = new SourceStringReader(source);
        sourceIsAvailable = true;
        ParserFactory.setup(sourceReader, new NameManagerForCompiler(sourceReader));
        parserl = ParserFactory.create(NoBeardParser.class);
    }

    public static boolean compile() {
        return sourceIsAvailable && parserl.parse();
    }

    public static byte[] getByteCode() {
        return ParserFactory.getCodeGenerator().getByteCode();
    }

    public static byte[] getStringStorage() {
        return ParserFactory.getScanner().getStringManager().getStringStorage();
    }

    public static void setSourceFile(String sourceFilePath) {
        try {
            NoBeardCompiler.sourceReader = new SourceFileReader(sourceFilePath);
            sourceIsAvailable = true;
            ParserFactory.setup(sourceReader, new NameManagerForCompiler(sourceReader));
            parserl = ParserFactory.create(NoBeardParser.class);
        } catch (FileNotFoundException ex) {
            NoBeardCompiler.sourceReader = new SourceStringReader("");
            ParserFactory.setup(sourceReader, new NameManagerForCompiler(sourceReader));
            parserl = ParserFactory.create(NoBeardParser.class);
            parserl.getErrorHandler().throwFileNotFound(sourceFilePath);
            sourceIsAvailable = false;
        } 
    }

    public static boolean lastCompilationWasSuccessful() {
        return parserl.parsingWasSuccessful();
    }

    public static List<String> getErrorList() {
        List<Error> allErrors = parserl.getErrorHandler().getAllErrors();
        return allErrors.stream().map(e -> "Error in line " + e.getLineNumber() + ": " + e.getMessage()).collect(Collectors.toList());
    }
}
