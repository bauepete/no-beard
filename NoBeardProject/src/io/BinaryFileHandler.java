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
package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import nbm.InstructionSet.Instruction;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class BinaryFileHandler {

    public static void save(BinaryFile filePath) throws IOException {
        Path p = Paths.get(filePath.getPath());
        final int programLength = filePath.getProgram().length;
        final int stringStorageLength = filePath.getStringStorage().length;
        byte[] data = new byte[programLength + stringStorageLength];
        System.arraycopy(filePath.getProgram(), 0, data, 0, programLength);
        System.arraycopy(filePath.getStringStorage(), 0, data, programLength, stringStorageLength);
        Files.write(p, data);
    }

    public static BinaryFile open(String filePath) throws IOException {
        BinaryFile openedFile = BinaryFile.get(filePath);

        Path p = Paths.get(filePath);
        byte[] rawData = Files.readAllBytes(p);

        int positionOfHalt = 0;
        while (positionOfHalt < rawData.length && rawData[positionOfHalt] != Instruction.HALT.getId()) {
            positionOfHalt++;
        }
        if (positionOfHalt == rawData.length) {
            byte[] program = new byte[rawData.length];
            System.arraycopy(rawData, 0, program, 0, program.length);
            openedFile.setProgram(program);
        } else {
            byte[] program = new byte[positionOfHalt + 1];
            System.arraycopy(rawData, 0, program, 0, program.length);
            openedFile.setProgram(program);

            byte[] stringStorage = new byte[rawData.length - positionOfHalt - 1];
            System.arraycopy(rawData, positionOfHalt + 1, stringStorage, 0, stringStorage.length);
            openedFile.setStringStorage(stringStorage);
        }
        return openedFile;
    }

}
