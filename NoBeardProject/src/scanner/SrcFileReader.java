/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author peter
 */
public class SrcFileReader implements SrcReader {

    private FileReader fd;
    private int currentChar;
    private int currentCol;
    private int currentLine;

    public SrcFileReader(String filePath) throws FileNotFoundException {
        fd = new FileReader(filePath);
    }

    @Override
    public void nextChar() {
        try {
            currentChar = fd.read();
        } catch (IOException ex) {
            Logger.getLogger(SrcFileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        currentCol++;
        if (currentChar == '\n') {
            currentCol = -1;
            currentLine++;
        }
    }

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
