package compiler;

import java.io.FileNotFoundException;
import java.io.IOException;
import compiler.Models.TOKEN;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LexicalAnalyser {

    private final int EOF = -1;
    private char character = ' ';
    Path currentRelativePath = Paths.get("");
    String s = currentRelativePath.toAbsolutePath().toString();
    private final ReadFile readFile;
    private int Line = 1;

    public LexicalAnalyser() throws FileNotFoundException {
        this.readFile = new ReadFile(s+"\\src\\Codes\\test2.txt");
    }

    public TOKEN Scan() throws IOException {
        TOKEN token = null;
        for (;; Readch()) {
            if (character == ' ' || character == '\t' || character == '\r' || character == '\b') {
                continue;
            } else if (character == '\n') {
                Line++; //conta linhas
            } else if (character == '%') {
                while(Readch()){
                    if (character == '\n') {
                        Line++; //conta linhas
                    }
                    if(character == '%'){
                        break;
                    }
                }
            } else {
                break;
            }
        }
        
        token = Operators(character);
        if(token!=null){
            Readch();
            return token;
        }
        //Const numbers
        if (Character.isDigit(character)) {
            token = ScanNumber();
            if(token!=null){
                return token;
            }
        }
        //Identifiers
        if (Character.isLetter(character)) {
            token = ScanIdentifier();
             if(token!=null){
                return token;
            }
        }
        if (character == '\'') {
            if (Readch()) {
                if (CharIsASCII(character, false)) {
                    char s = character;
                    if (Readch()) {
                        if (character == '\'') {
                            token = new TOKEN("CHAR_CONST", Character.toString(s));
                        }
                    }
                }
            }
             if(token!=null){
                return token;
            }
        }
        if (character == '\"') {
           token = ScanLiteral();
            if(token!=null){
                return token;
            }
        }
        if(CharIsASCII(character,false)){
            token=new TOKEN("INVALID",Character.toString(character));
            Readch();
        }
         return token;
    }

    private boolean Readch() throws IOException {
        int character = readFile.NextCharacter();
        this.character = (char) character;
        return character != EOF;
    }

    private TOKEN ScanIdentifier() throws IOException {
        StringBuffer sb = new StringBuffer();
        do {
            sb.append(character);
            Readch();
        } while (Character.isLetterOrDigit(character));
        
        String s = sb.toString();
        TOKEN token = new TOKEN("ID", s);
        return token;

    }

    private TOKEN ScanLiteral() throws IOException {
        StringBuffer sb = new StringBuffer();
        for (Readch(); CharIsASCII(character, true); Readch()) {
            sb.append(character);
        }
        if (character == '\"') {
            String s = sb.toString();
            TOKEN token = new TOKEN("LITERAL", s);
            Readch();
            return token;
        }
        return null;
    }

    private TOKEN ScanNumber() throws IOException {
        StringBuffer sb = new StringBuffer();
        String name = "INTEGER_CONST";
        do {
            sb.append(character);
            Readch();
        } while (Character.isDigit(character));
        if (character == '.') {
            do {
                sb.append(character);
                Readch();
            } while (Character.isDigit(character));
            name = "FLOAT_CONST";
        }
        String s = sb.toString();
        TOKEN token = new TOKEN(name, s);
        return token;

    }
    private TOKEN Operators(char ch) throws IOException{
        TOKEN token = null;
        switch (ch) {
            case ';':
                token = new TOKEN("SEMICOLON");
                break;
            case '+':
                token = new TOKEN("PLUS_OP");
                break;

            case '-':
                token = new TOKEN("MINUS_OP");
                break;

            case '*':
                token = new TOKEN("MULT_OP");
                break;

            case '/':
                token = new TOKEN("DIV_OP");
                break;

            case '=':
                token = new TOKEN("COMPARATION");
                break;
            case ':':
                if (Readch()) {
                    if (character == '=') {
                        token = new TOKEN("ASSIGN");
                    }
                }
                break;
            case '(':
                token = new TOKEN("OPEN_PARENTHESES");
                break;
            case ')':
                token = new TOKEN("CLOSE_PARENTHESES");
                break;
            case ',':
                token = new TOKEN("comma");
                break;
            case '>':
                if (Readch()) {
                    if (character == '=') {
                        token = new TOKEN("GTE");
                    } else {
                        token = new TOKEN("GT");
                    }
                }
                break;

            case '<':
                if (Readch()) {
                    switch (character) {
                        case '=':
                            token = new TOKEN("LTE");
                            break;
                        case '>':
                            token = new TOKEN("NE");
                            break;
                        default:
                            token = new TOKEN("LT");
                            break;
                    }
                }
                break;
            default:
                break;
        };
        return token;
    }
    private static boolean CharIsASCII(char character, boolean condition) {
        if (condition) {
            if (character > 0x00 && character < 0xff) {
                if (character != 0x27 && character != 0x0a && character != 0x22) {
                    return true;
                }
                return false;
            }
        }
        return character > 0x00 && character < 0xff;
    }
}
