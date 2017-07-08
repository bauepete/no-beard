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
package nbasm;

import asm.NoBeardAssembler;
import io.BinaryFile;
import io.BinaryFileHandler;
import io.SourceFileReader;
import io.SourceReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import parser.AssemblerParser;
import parser.Parser;
import parser.ParserFactory;
import scanner.NameManagerForAssembler;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class NbAsm {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: nba <assembler_file>.na");
            return;
        }
        if (args[0].compareTo("-v") == 0) {
            System.out.println(NoBeardAssembler.getVersion());
            return;
        }
        SourceReader sourceReader = prepareSourceReader(args[0]);
        if (sourceReader == null)
            return;
        Parser parser = parseFile(sourceReader);
        presentParsingResult(parser, args);
    }

    private static SourceReader prepareSourceReader(String filePath) {
        try {
            SourceReader sourceReader = new SourceFileReader(filePath);
            return sourceReader;
        } catch (FileNotFoundException ex) {
            System.err.println("Assembler file " + filePath + " not found");
            return null;
        }
    }
    
    private static Parser parseFile(SourceReader sourceReader) {
        ParserFactory.setup(sourceReader, new NameManagerForAssembler(sourceReader));
        Parser parser = ParserFactory.create(AssemblerParser.class);
        parser.parse();
        return parser;
    }

    private static void writeBinaryFile(String filePath, byte[] byteCode, byte[] stringStorage) {
        BinaryFile file = BinaryFile.get(filePath);
        file.setProgram(byteCode);
        file.setStringStorage(stringStorage);
        try {
            BinaryFileHandler.save(file);
        } catch (IOException ex) {
            System.err.println("Unable to save binary file");
        }
    }
    
    private static void presentParsingResult(Parser parser, String[] args) {
        if (parser.parsingWasSuccessful()) {
            String binaryFilePath = args[0].replaceAll(".na", ".no");
            writeBinaryFile(binaryFilePath, ParserFactory.getCodeGenerator().getByteCode(), ParserFactory.getScanner().getStringManager().getStringStorage());
        } else {
            parser.getErrorHandler().getAllErrors().stream().forEach((e) -> {
                System.err.println(e.getMessage());
            });
        }
    }
}
