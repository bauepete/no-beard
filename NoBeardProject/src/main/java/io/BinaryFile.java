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

import java.nio.ByteBuffer;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class BinaryFile {

    private static final byte[] FILE_ID = new byte[]{'1', '7'};
    private static final byte[] CURRENT_VERSION = new byte[]{'v', 1, 0, 0};

    private final String filePath;

    private final byte[] versionInfo;
    private byte[] stringStorage;
    private byte[] program;

    private BinaryFile(String filePath) {
        this.filePath = filePath;
        stringStorage = new byte[]{};
        program = new byte[]{};
        versionInfo = CURRENT_VERSION.clone();
    }

    public static BinaryFile get(String filePath) {
        return new BinaryFile(filePath);
    }

    public byte[] getStringStorage() {
        return stringStorage;
    }

    public byte[] getProgram() {
        return program;
    }

    public void setStringStorage(byte[] stringStorage) {
        if (stringStorage != null) {
            this.stringStorage = stringStorage;
        } else {
            this.stringStorage = new byte[]{};
        }
    }

    public void setProgram(byte[] program) {
        if (program != null) {
            this.program = program;
        } else {
            this.program = new byte[]{};
        }
    }

    String getPath() {
        return filePath;
    }

    byte[] getByteStream() {
        byte[] headerSegment = createHeaderSegment();
        byte[] stringStorageSegment = createStringStorageSegment();
        byte[] programSegment = createProgramSegment();
        byte[] byteStream = copySegementsIntoByteStream(headerSegment, stringStorageSegment, programSegment);
        return byteStream;
    }

    private byte[] createHeaderSegment() {
        byte[] segment = new byte[FILE_ID.length + getVersion().length];
        System.arraycopy(FILE_ID, 0, segment, 0, FILE_ID.length);
        System.arraycopy(getVersion(), 0, segment, FILE_ID.length, getVersion().length);
        return segment;
    }

    private byte[] createStringStorageSegment() {
        return getSegment(stringStorage);
    }

    private byte[] getSegment(final byte[] requestedSegment) {
        byte[] segment = new byte[SIZE_OF_LENGTH + requestedSegment.length];
        byte[] len = ByteBuffer.allocate(SIZE_OF_LENGTH).putInt(requestedSegment.length).array();
        System.arraycopy(len, 0, segment, 0, SIZE_OF_LENGTH);
        System.arraycopy(requestedSegment, 0, segment, SIZE_OF_LENGTH, requestedSegment.length);
        return segment;
    }

    private byte[] createProgramSegment() {
        return getSegment(program);
    }

    private byte[] copySegementsIntoByteStream(byte[] headerSegment, byte[] stringStorageSegment, byte[] programSegment) {
        byte[] byteStream = new byte[headerSegment.length + stringStorageSegment.length + programSegment.length];
        System.arraycopy(headerSegment, 0, byteStream, 0, headerSegment.length);
        System.arraycopy(stringStorageSegment, 0, byteStream, headerSegment.length, stringStorageSegment.length);
        System.arraycopy(programSegment, 0, byteStream, headerSegment.length + stringStorageSegment.length, programSegment.length);
        return byteStream;
    }

    void setByteStream(byte[] byteStream) {
        checkHeader(byteStream);
        extractVersionInfo(byteStream);
        int stringLength = extractStringStorage(byteStream);
        extractProgram(byteStream, stringLength);
    }

    private void checkHeader(byte[] byteStream) throws IllegalArgumentException {
        if (byteStream.length == 0 || byteStream[0] != '1' || byteStream[1] != '7' || byteStream[2] != 'v') {
            throw new IllegalArgumentException("No NoBeard binary file.");
        }
    }

    private void extractVersionInfo(byte[] byteStream) {
        System.arraycopy(byteStream, 2, versionInfo, 0, SIZE_OF_LENGTH);
    }

    private int extractStringStorage(byte[] byteStream) {
        int stringLength = extractLength(byteStream, START_OF_STRING_SEGMENT);
        stringStorage = new byte[stringLength];
        System.arraycopy(byteStream, START_OF_STRING_SEGMENT + SIZE_OF_LENGTH, stringStorage, 0, stringLength);
        return stringLength;
    }

    private void extractProgram(byte[] byteStream, int stringLength) {
        int programLength = extractLength(byteStream, START_OF_STRING_SEGMENT + SIZE_OF_LENGTH + stringLength);
        program = new byte[programLength];
        System.arraycopy(byteStream, START_OF_STRING_SEGMENT + SIZE_OF_LENGTH + stringLength + SIZE_OF_LENGTH, program, 0, programLength);
    }
    private static final int SIZE_OF_LENGTH = 4;
    private static final int START_OF_STRING_SEGMENT = 6;

    private int extractLength(byte[] byteStream, final int fromPosition) {
        byte[] stringLength = new byte[SIZE_OF_LENGTH];
        System.arraycopy(byteStream, fromPosition, stringLength, 0, SIZE_OF_LENGTH);
        int strLen = ByteBuffer.wrap(stringLength).getInt();
        return strLen;
    }

    public byte[] getVersion() {
        return versionInfo;
    }
}
