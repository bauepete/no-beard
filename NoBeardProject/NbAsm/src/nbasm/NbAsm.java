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
import io.SourceFileReader;
import io.SourceReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import nbm.InstructionSet;
import nbm.InstructionSet.Instruction;

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
            System.err.println("Usage: nbasm <assembler_file>.na");
            return;
        }
        if (args[0].compareTo("-v") == 0) {
            System.out.println(NoBeardAssembler.getVersion());
            return;
        }
        SourceReader sourceReader;
        try {
            sourceReader = new SourceFileReader(args[0]);
        } catch (FileNotFoundException ex) {
            System.err.println("Assembler file " + args[0] + " not found");
        }
//        Path path = Paths.get(args[1]);
//        byte[] fileContent;
//        try {
//            fileContent = Files.readAllBytes(path);
//        } catch (IOException ex) {
//            System.out.println("File " + args[1] + " not found");
//            return;
//        }
//        int i = 0;
//        while (fileContent[i] != Instruction.HALT.getId()) {
//            
//        }
    }
    
}
