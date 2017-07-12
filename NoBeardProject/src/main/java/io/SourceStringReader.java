/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import io.SourceReader;
import error.SourceCodeInfo;

/**
 *
 * @author peter
 */
public class SourceStringReader implements SourceReader, SourceCodeInfo {

    String srcString;
    int currentIndex;

    public SourceStringReader(String srcString) {
        this.srcString = srcString;
        currentIndex = -1;
    }

    @Override
    public void nextChar() {
        currentIndex++;
    }

    @Override
    public int getCurrentChar() {
        if (currentIndex < srcString.length()) {
            return srcString.charAt(currentIndex);
        }
        else {
            return -1;
        }
    }

    @Override
    public int getCurrentCol() {
        return currentIndex + 1;
    }

    @Override
    public int getCurrentLine() {
        return 1;
    }
}
