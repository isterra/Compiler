
package compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
    private final BufferedReader  _buffer;
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
