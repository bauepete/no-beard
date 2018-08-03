/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author peter
 */
public class SourceFileReader implements SourceReader {

    private final FileReader fd;
    private int currentChar;
    private int currentCol;
    private int currentLine;

    public SourceFileReader(String filePath) throws FileNotFoundException {
        fd = new FileReader(filePath);
    }

    @Override
    public void nextChar() {
        readNextChar();
        handleLineBreak();
    }

    private void readNextChar() {
        try {
            currentChar = fd.read();
        } catch (IOException ex) {
            Logger.getLogger(SourceFileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        currentCol++;
    }

    private void handleLineBreak() {
        if (currentChar == LINE_FEED) {
            currentCol = -1;
            currentLine++;
        }
    }
    private static final char LINE_FEED = '\n';

    @Override
    public int getCurrentChar() {
        return currentChar;
    }

    @Override
    public int getCurrentCol() {
        return currentCol;
    }

    @Override
    public int getCurrentLine() {
        return currentLine;
    }
    
    
}
