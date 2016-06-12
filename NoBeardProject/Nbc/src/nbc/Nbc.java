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

import compiler.NbCompiler;
import parser.Parser;
import java.io.FileNotFoundException;
import nbm.Nbm;
import scanner.SrcFileReader;
import scanner.SrcReader;

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
        SrcReader sourceReader;
        try {
            sourceReader = new SrcFileReader(args[0]);
        } catch (FileNotFoundException ex) {
            System.err.println("File " + args[0] + " not found");
            return;
        }
        NbCompiler compiler = new NbCompiler(sourceReader);
        Parser parser = compiler.getParser();
        boolean parsingWasSuccessfull = parser.parse();
        if (parsingWasSuccessfull) {
            Nbm machine = new Nbm();
            machine.loadProg(0, compiler.getCode().getByteCode());
            machine.loadDat(0, compiler.getStringStorage());
            machine.runProg(0);
        } else {
            parser.getErrorHandler().printSummary();
        }
    }
    
}
