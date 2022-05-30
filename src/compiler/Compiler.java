package compiler;

import compiler.Models.TOKEN;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Compiler {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        if(args.length==0){
            System.out.println("Necessario arquivo como parametro");
            System.exit(-1);
        }
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(args[0]);
        TOKEN token;
        System.out.println("**** TOKENS ****");
        System.out.println();
        while ((token = lexicalAnalyser.GetToken()) != null) {
            System.out.println(token);
        }
        System.out.println();
        lexicalAnalyser.printSymbleTable();
        System.out.println();
        lexicalAnalyser.ShowLogsOfLexicalAnalyser();
        System.out.println();
    }

}
