package compiler;

import compiler.Models.TOKEN;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Compiler {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser();
        TOKEN token;
        while((token=lexicalAnalyser.GetToken())!=null){
                    System.out.println(token);
        }
        lexicalAnalyser.printSymbleTable();
    }

}
