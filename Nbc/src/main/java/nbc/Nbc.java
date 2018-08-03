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
package nbc;

import compiler.NoBeardCompiler;
import io.BinaryFile;
import io.BinaryFileHandler;
import java.io.IOException;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class Nbc {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage nbc <source file>");
            return;
        }
        NoBeardCompiler.setSourceFile(args[0]);
        NoBeardCompiler.compile();
        if (NoBeardCompiler.lastCompilationWasSuccessful()) {
            String binaryFilePath = args[0].replaceAll(".nb", ".no");
            writeBinaryFile(binaryFilePath, NoBeardCompiler.getByteCode(), NoBeardCompiler.getStringStorage());
        } else {
            NoBeardCompiler.getErrorList().forEach(System.out::println);
        }
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
}
