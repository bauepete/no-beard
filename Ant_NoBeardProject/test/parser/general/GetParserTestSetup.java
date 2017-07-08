/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import parser.GetParser;
import parser.ParserFactory;

/**
 *
 * @author peter
 */
public class GetParserTestSetup extends ParserTestSetup {

    public static GetParser getGetIntSetup() {
        setupInfraStructure("get(x, success);");
        return ParserFactory.create(GetParser.class);
    }
    
}
