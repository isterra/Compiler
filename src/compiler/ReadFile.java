/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author israel.terra
 */
public class ReadFile {
    private BufferedReader  _buffer;
    private final int _EOF = -1;
    private int _character;
    public ReadFile(String filePath) throws FileNotFoundException{
        _buffer= new BufferedReader(new FileReader(filePath));
        _character=0;
    }
    public int NextCharacter() throws IOException{
        _character= _buffer.read();
        return _character;
    }
}
