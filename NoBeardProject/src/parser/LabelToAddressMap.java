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
package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import machine.CodeGenerator;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class LabelToAddressMap {

    public static final int UNDEFINED_ADDRESS = -1;

    private final CodeGenerator codeGenerator;

    private class Label {

        Label(int address) {
            this.address = address;
            unresolvedJumpSources = new ArrayList();
        }
        public int address;
        public List<Integer> unresolvedJumpSources;
    }
    private final HashMap<String, Label> labelToAddressMap;

    LabelToAddressMap(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
        labelToAddressMap = new HashMap<>();
    }

    void add(String label, int address) {
        if (labelToAddressMap.containsKey(label)) {
            Label existingLabel = labelToAddressMap.get(label);
            existingLabel.address = address;
            for (int codeAddress : existingLabel.unresolvedJumpSources) {
                codeGenerator.fixup(codeAddress, address);
            }
            existingLabel.unresolvedJumpSources.clear();
        } else {
            labelToAddressMap.put(label, new Label(address));
        }
    }

    int getAddress(String label) {
        if (!labelToAddressMap.containsKey(label)) {
            Label newLabel = new Label(UNDEFINED_ADDRESS);
            newLabel.unresolvedJumpSources.add(codeGenerator.getPc());
            labelToAddressMap.put(label, newLabel);
        } else {
            Label existingLabel = labelToAddressMap.get(label);
            if (existingLabel.address == UNDEFINED_ADDRESS) {
                existingLabel.unresolvedJumpSources.add(codeGenerator.getPc());
            }
        }
        return labelToAddressMap.get(label).address;
    }

    List<Integer> getUnresolvedJmpAddresses(String label) {
        return labelToAddressMap.get(label).unresolvedJumpSources;
    }

    boolean hasUndefinedLabels() {
        if (labelToAddressMap.entrySet().stream().anyMatch((l) -> (!l.getValue().unresolvedJumpSources.isEmpty()))) {
            return true;
        }
        return false;
    }

    String[] getUndefinedLabels() {
        List<String> undefinedLabels = new ArrayList<>();
        labelToAddressMap.entrySet().stream().filter((l) -> (!l.getValue().unresolvedJumpSources.isEmpty())).forEach((l) -> {
            undefinedLabels.add(l.getKey());
        });
        return undefinedLabels.stream().toArray(String[]::new);
    }

}
