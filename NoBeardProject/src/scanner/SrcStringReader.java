/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import error.SourceCodeInfo;

/**
 *
 * @author peter
 */
public class SrcStringReader implements SrcReader, SourceCodeInfo {

    String srcString;
    int currentIndex;

    public SrcStringReader(String srcString) {
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
