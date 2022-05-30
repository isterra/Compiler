package compiler;

import java.io.FileNotFoundException;
import java.io.IOException;
import compiler.Models.TOKEN;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class LexicalAnalyser {

    private final int EOF = -1;
    private char character = ' ';
    private final ReadFile readFile;
    private int Line = 1;
    private final SymbleTable symbleTable;
    private final ArrayList<String> errors;
    private int errorsCount;

    public LexicalAnalyser(String path) throws FileNotFoundException {
        this.errors = new ArrayList<>();
        this.readFile = new ReadFile(path);
        symbleTable = new SymbleTable();
        errorsCount = 0;
    }

    public TOKEN GetToken() throws IOException {
        TOKEN token = null;

        //Ignorar espacos em branco e comentarios
        for (;; Readch()) {
            if (character == ' ' || character == '\t' || character == '\r' || character == '\b') {
                continue;
            } else if (character == '\n') {
                Line++; //conta linhas
            } else if (character == '%') {
                //Comeco de comentario
                while (Readch()) {
                    if (character == '\n') {
                        Line++; //conta linhas
                    }
                    if (character == '%') {
                        //Fim de comentario
                        break;
                    }
                }
                if (character != '%') {
                    //Comecou o comentario , chegou no fim de arquivo e nao fechou
                    HandleComentError("Miss '%' to close comment");
                    break;
                }
            } else {
                break;
            }
        }

        token = Operators(character);
        if (token != null) {
            if ("GREATER_THAN".equals(token.getName()) || "LESS_THAN".equals(token.getName())) {
                return token;
            }
            Readch();
            return token;
        }
        //Const numbers
        if (Character.isDigit(character)) {
            token = ScanNumber();
            if (token != null) {
                return token;
            }
        }
        //Identifiers
        if (Character.isLetter(character)) {
            token = ScanIdentifier();
            if (token != null) {
                return token;
            }
        }
        if (character == '\'') {
            if (Readch()) {
                if (CharIsASCII(character, false)) {
                    char s = character;
                    if (Readch()) {
                        if (character == '\'') {
                            Readch();
                            token = new TOKEN("CHAR_CONST", Character.toString(s));
                        } else {
                            HandleError("Missing ' or invalid size to char constant, invalid character:" + character);
                        }
                    }
                }
            }
            if (token != null) {
                return token;
            }
        }
        if (character == '\"') {
            token = ScanLiteral();
            if (token != null) {
                return token;
            } else {
                HandleError("Missing \" to close Literal");
                return GetToken();
            }
        }
        //Character nao identificado
        if (CharIsASCII(character, false)) {
            if (!IsBlanckCharacter(character)) {
                HandleError("Invalid character: " + Character.toString(character));
            }
            Readch();
            return GetToken();
        }
        return token;
    }

    private boolean Readch() throws IOException {
        int character = readFile.NextCharacter();
        this.character = (char) character;
        return character != EOF;
    }

    private TOKEN ScanIdentifier() throws IOException {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(character);
            Readch();
        } while (Character.isLetterOrDigit(character));

        String string = sb.toString();
        String reservedSymbol = symbleTable.IsReservedSymbol(string);
        if (reservedSymbol != null) {
            TOKEN token = new TOKEN(reservedSymbol);
            return token;
        }
        String entryTable = Integer.toString(symbleTable.SetToken(string));
        TOKEN token = new TOKEN("ID", entryTable);
        return token;

    }

    public void printSymbleTable() {
        symbleTable.PrintHT();
    }

    private TOKEN ScanLiteral() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Readch(); CharIsASCII(character, true); Readch()) {
            sb.append(character);
        }
        if (character == '\"') {
            String string = sb.toString();
            TOKEN token = new TOKEN("LITERAL", string);
            Readch();
            return token;
        }
        return null;
    }

    private TOKEN ScanNumber() throws IOException {
        StringBuilder sb = new StringBuilder();
        TOKEN token = null;
        String name = "INTEGER_CONST";
        do {
            sb.append(character);
            Readch();
        } while (Character.isDigit(character));
        if (character == '.') {
            int floatRange = -1;
            do {
                sb.append(character);
                floatRange++;
                Readch();
            } while (Character.isDigit(character));
            if (floatRange == 0) {
                HandleError("Missing digit after '.' in " + sb.toString());
                return GetToken();
            }
            name = "FLOAT_CONST";
        }
            String string = sb.toString();
            token = new TOKEN(name, string);
        return token;
    }

    private TOKEN Operators(char ch) throws IOException {
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
                    } else {
                        HandleError("Miss '=' after ':'");
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
                token = new TOKEN("COMMA");
                break;
            case '>':
                if (Readch()) {
                    if (character == '=') {
                        token = new TOKEN("GREATER_THAN_EQUAL");
                    } else {
                        token = new TOKEN("GREATER_THAN");
                    }
                }
                break;
            case '<':
                if (Readch()) {
                    switch (character) {
                        case '=':
                            token = new TOKEN("LESS_THAN_EQUAL");
                            break;
                        case '>':
                            token = new TOKEN("NOT_EQUAL");
                            break;
                        default:
                            token = new TOKEN("LESS_THAN");
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

    private void HandleError(String value) throws IOException {
        String ANSI_RED = "\u001B[31m";
        String ANSI_RESET = "\u001B[0m";
        errors.add(ANSI_RED + "Erro na linha " + Line + ": " + value + ANSI_RESET);
        errorsCount++;
        for (Readch();; Readch()) {
            if (IsBlanckCharacter(character)) {
                break;
            }
        }
    }

    private boolean ValidCharacterAfterNumber() throws IOException {
        for (;; Readch()) {
            if (character == ' ' || character == '\t' || character == '\r' || character == '\b') {
                continue;
            } else {
                break;
            }
        }
        if (Character.isLetter(character)) {
            return false;
        }
        return true;
    }

    private void HandleComentError(String value) throws IOException {
        String ANSI_RED = "\u001B[31m";
        String ANSI_RESET = "\u001B[0m";
        errorsCount++;
        errors.add(ANSI_RED + "Erro na linha " + Line + ": " + value + ANSI_RESET);
    }

    public void ShowLogsOfLexicalAnalyser() {
        if (errorsCount > 0) {
            for (int i = 0; i < errors.size(); i++) {
                System.out.println(errors.get(i));
            }
        } else {
            String ANSI_GREEN = "\u001B[32m";
            String ANSI_RESET = "\u001B[0m";
            System.out.println(ANSI_GREEN + "BUILD SUCCESSFUL" + ANSI_RESET);
        }
    }

    public boolean IsBlanckCharacter(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\b' || c == '\n';
    }
}
