package compiler;

import compiler.Models.TOKEN;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Compiler {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File path = new File(System.getProperty("user.dir")); 
        if(args.length==0){
            System.out.println("Necessario arquivo como parametro");
            System.exit(-1);
        }
      
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(path+"\\"+args[0]);
        TOKEN token;
        Parser parser = new Parser(lexicalAnalyser,lexicalAnalyser.GetToken());
        parser.Program();
        System.out.println("**** TOKENS ****");
        System.out.println();
        /*while ((token = lexicalAnalyser.GetToken()) != null) {
            System.out.println(token);
        }*/
        System.out.println();
        lexicalAnalyser.printSymbleTable();
        System.out.println();
        lexicalAnalyser.ShowLogsOfLexicalAnalyser();
        System.out.println();
    }

}
