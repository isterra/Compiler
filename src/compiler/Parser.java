/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compiler;

import compiler.Models.TOKEN;
import java.io.IOException;


/**
 *
 * @author lucas
 */
public class Parser {
    private final LexicalAnalyser lexicalAnalyser;
    private TOKEN token;
    public final static String
            //PALAVRAS RESERVADAS
            ROUTINE = "ROUTINE",
            BEGIN = "BEGIN",
            END = "END",
            DECLARE = "DECLARE",
            INT = "INT",
            FLOAT = "FLOAT",
            CHAR = "CHAR",
            IF = "IF",
            THEN = "THEN",
            ELSE = "ELSE",
            REPEAT = "REPEAT",
            UNTIL = "UNTIL",
            WHILE = "WHILE",
            DO = "DO",
            READ = "READ",
            WRITE = "WRITE",
            NOT = "NOT",
            AND = "AND",
            OR = "OR",
            //OPERADORES
            COMPARATION = "COMPARATION",
            LOWER = "LESS_THAN",
            LOWER_EQUAL = "LESS_THAN_EQUAL",
            GREATER = "GREATER_THAN",
            GREATER_EQUAL = "GREATER_THAN_EQUAL",
            LOWER_GREATER = "NOT_EQUAL",
            //OPERAÇÕES
            ADD = "PLUS_OP",
            SUB = "MINUS_OP",
            MUL = "MULT_OP",
            DIV = "DIV_OP",
            //PONTUAÇÕES
            ASSING = "ASSIGN",
            OP_PARENTHESES = "OPEN_PARENTHESES",
            CL_PARENTHESES = "CLOSE_PARENTHESES",
            SEMICOLON = "SEMICOLON",
            COMMA = "COMMA",
            //OUTROS_TOKENS
            INTEGER_CONST = "INTEGER_CONST",
            FLOAT_CONST = "FLOAT_CONST",
            CHAR_CONST = "CHAR_CONST",
            LITERAL = "LITERAL",
            IDENTIFIER = "ID";
    public Parser(LexicalAnalyser lexicalAnalyser,TOKEN token) throws IOException{
        this.lexicalAnalyser = lexicalAnalyser;
        this.token = token;
    }
    void Advance()throws IOException{
        this.token = this.lexicalAnalyser.GetToken();
    }
    void Eat (String tokenName)throws IOException{
        if(this.token.getName().equals(tokenName)){
            Advance();
        }
        else{
            System.out.println("linha "+lexicalAnalyser.Line+": Expected "+tokenName);
            //System.out.println("Expected "+tokenName);
            //System.out.println("token="+this.token.getName());
        }
    }
    void Program()throws IOException{
        
        switch(token.getName()){
            //program->routine body
            case ROUTINE:
                Eat(ROUTINE);Body();
                break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": Expected 'routine'");
                break;
        }
    }
    void Body()throws IOException{
        
        switch(token.getName()){
            //body->[decl-list] begin stmt-list end
            case DECLARE:
                DeclList();Eat(BEGIN);StmtList();Eat(END);break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": Expected 'declare'");
                break;
        }
    }
    void DeclList()throws IOException{
        
        switch(token.getName()){
            //decl-list->declare decl ";" decl-listPrime
            case DECLARE:
                Eat(DECLARE);Decl();Eat(SEMICOLON);DeclListPrime();break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": Expected 'declare'");
                break;
        }
    }
    void DeclListPrime()throws IOException{
        
        switch(token.getName()){
            //decl-list'->lambda
            case BEGIN:
                break;
            //decl-list'->decl ";" decl-listPrime
            case INT:
            case FLOAT:
            case CHAR:
               Decl();Eat(SEMICOLON);DeclListPrime();break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": 'int','float' or 'char'");
                break;
        }
        
    }
    void Decl()throws IOException{
        
        switch(token.getName()){
            //decl->type ident-list
            case INT:
            case FLOAT:
            case CHAR:
               Type();IdentList();break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": 'int','float' or 'char'");
                break;
        }

    }
    void IdentList()throws IOException{
        
        switch(token.getName()){
            //ident-list ->identifier ident-listPrime
            case IDENTIFIER:
               Eat(IDENTIFIER);IdentListPrime();break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": Expected 'identifier'");
                break;
        }
    }
    void IdentListPrime()throws IOException{
        switch(token.getName()){
            //ident-list'-> lambda
            case SEMICOLON:
               break;
            //ident-list'-> ,identifier ident-listPrime
            case COMMA:
                Eat(COMMA);Eat(IDENTIFIER);IdentListPrime();break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": Expected ','");
                break;
        }
    }
    void Type()throws IOException{
        switch(token.getName()){
            //type-> int
            case INT:
                Eat(INT);break;
            //type-> float
            case FLOAT:
                Eat(FLOAT);break;
            //type-> char
            case CHAR:
               Eat(CHAR);break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": Expected 'int', 'float' or 'char'");
                break;
        }

    }
    void StmtList()throws IOException{
        
        switch(token.getName()){
            //stmt-list-> stmt stmt-listPrime
            case IDENTIFIER:
            case IF:
            case WHILE:
            case REPEAT:
            case READ:
            case WRITE:
                Stmt();break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": 'identifier','if','while','repeat','read' or 'write'");
                break;
        }
    }
    void Stmt()throws IOException{
        
         switch(token.getName()){
            //stmt->assign-stmt
             case IDENTIFIER:
                AssignStmt();StmtPrime();break;
            //stmt->if-stmt 
             case IF:
                IfStmt();StmtPrime();break;
            //stmt-> while-stmt 
            case WHILE:
                WhileStmt();StmtPrime();break;
            //stmt-> repeat-stmt
            case REPEAT:
                RepeatStmt();StmtPrime();break;
            //stmt->read-stmt
            case READ:
                ReadStmt();StmtPrime();break;
            //stmt-> write-stmt
            case WRITE:
                WriteStmt();StmtPrime();break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": 'identifier','if','while','repeat','read' or 'write'");
                break;
        }
    }
    void StmtPrime()throws IOException{
        switch(token.getName()){
            //stmt-list'-> lambda
            case END:
            case ELSE:
            case UNTIL:
                break;
            //stmt-list'->; stmt stmt-listPrime
            case SEMICOLON:
                Eat(SEMICOLON);Stmt();break;
            default: 
                System.out.println("linha "+(lexicalAnalyser.Line-1)+": Expected ';'");
                break;
        }
    }
    
    void AssignStmt()throws IOException{
        
        switch(token.getName()){
            //assingn-stmt->identifier ":=" simple_expr
            case IDENTIFIER:
                Eat(IDENTIFIER);Eat(ASSING);SimpleExpr();break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": Expected 'identifier'");
                break;
        }
    }
    void IfStmt()throws IOException{
        
        switch(token.getName()){
            //if-stmt->if condition then stmt-list if-stmtPrime
            case IF:
                Eat(IF);Condition();Eat(THEN);StmtList();IfStmtPrime();break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": Expected 'if'");
                break;
        }

    }
    void IfStmtPrime()throws IOException{
        switch(token.getName()){
            //if-stmt'->lambda
            case UNTIL:
            case SEMICOLON:
                break;
            //if-stmt'->end
            case END:
                Eat(END);break;
            //if-stmt'->else stmt-list end | if-stmt'->lambda
            case ELSE:
                Eat(ELSE);StmtList();Eat(END);break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": Expected 'end' or 'else'");
                break;
        }
    }
    void Condition()throws IOException{ 
        switch(token.getName()){
            //condition->expression
            case IDENTIFIER:
            case OP_PARENTHESES:
            case NOT:
            case INTEGER_CONST:
            case FLOAT_CONST:
            case CHAR_CONST:
            case SUB:
                Expression();break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": 'identifier','(','not','integer_const','float_const','char_const' or '-'");
                break;
        }
    }
    void RepeatStmt()throws IOException{
        switch(token.getName()){
            //repeat-stmt->repeat stmt-list stmt-suffix
            case REPEAT:
                Eat(REPEAT);StmtList();StmtSuffix();break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": Expected 'repeat'");
                break;
        }
    }
    void StmtSuffix()throws IOException{
        switch(token.getName()){
            //stmt-suffix->until condition
            case UNTIL:
                Eat(UNTIL);Condition();break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": Expected 'until'");
                break;
        }
    }
    void WhileStmt()throws IOException{
         switch(token.getName()){
            //while-stmt->stmt-prefix stmt-list end
            case WHILE:
                StmtPrefix();StmtList();Eat(END);break;
            default:  
                System.out.println("linha "+lexicalAnalyser.Line+": Expected 'While'");
                break;
        }
    }
    void StmtPrefix()throws IOException{
        switch(token.getName()){
            //stmt-prefix->while condition do
            case WHILE:
                Eat(WHILE);Condition();Eat(DO);break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": Expected 'while'");
                break;
        }
    }
    void ReadStmt()throws IOException{
         switch(token.getName()){
            //read-stmt->read "(" identifier ")"
            case READ:
                Eat(READ);Eat(OP_PARENTHESES);Eat(IDENTIFIER);Eat(CL_PARENTHESES);break;
            default: 
                 System.out.println("linha "+lexicalAnalyser.Line+": Expected 'read'");
                 break;
        }   
    }
    void WriteStmt()throws IOException{
         switch(token.getName()){
            //write-stmt->write"("writable")"
            case WRITE:
                Eat(WRITE);Eat(OP_PARENTHESES);Writable();Eat(CL_PARENTHESES);break;
            default: 
                 System.out.println("linha "+lexicalAnalyser.Line+": Expected 'write'");
                break;
        }  
    }
    void Writable()throws IOException{        
        switch(token.getName()){
            //writable->simple-expr
            case IDENTIFIER:
            case INTEGER_CONST:
            case FLOAT_CONST:
            case CHAR_CONST:
            case OP_PARENTHESES:
            case NOT:
            case SUB:
                SimpleExpr();break;
            //writable->literal
            case LITERAL:
                Eat(LITERAL);break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": 'identifier','(','not','integer_const','float_const','char_const','-' or 'literal'");
                break;
        }  
    }
    void Expression()throws IOException{        
         switch(token.getName()){
            //expression->simple-expr expression'
            case IDENTIFIER:
            case INTEGER_CONST:
            case FLOAT_CONST:
            case CHAR_CONST:
            case OP_PARENTHESES:
            case NOT:
            case SUB:
                SimpleExpr();ExpressionPrime();break;         
            default:  
                System.out.println("linha "+lexicalAnalyser.Line+": 'identifier','(','not','integer_const','float_const','char_const' or '-'");
                break;
        }  
    }
    void ExpressionPrime()throws IOException{
        switch(token.getName()){
            //expression'->lambda
            case CL_PARENTHESES:
            case THEN:
            case SEMICOLON:
            case END:
            case ELSE:
            case UNTIL:
            case DO:
                break;
            //expression'->relop simple-expr
            case COMPARATION:
            case LOWER:
            case LOWER_EQUAL:
            case GREATER:
            case GREATER_EQUAL:
            case LOWER_GREATER:
                Relop();SimpleExpr();break;         
            default:  
                System.out.println("linha "+lexicalAnalyser.Line+": Expected '=','>' ,'>=' , '<' , '<=' or '<>'" );
                break;
        }  
    }
    void SimpleExpr()throws IOException{        
        switch(token.getName()){
            //simple-expr-> term simple-expr'
            case IDENTIFIER:
            case INTEGER_CONST:
            case FLOAT_CONST:
            case CHAR_CONST:
            case OP_PARENTHESES:
            case NOT:
            case SUB:
                Term();SimpleExprPrime();break;         
            default:  
                System.out.println("linha "+lexicalAnalyser.Line+": 'identifier','(','not','integer_const','float_const','char_const' or '-'");
                break;
        }  
    }
    void SimpleExprPrime()throws IOException{    
        switch(token.getName()){
            //simple-expr'->lambda
            case SEMICOLON:
            case CL_PARENTHESES:
            case COMPARATION:
            case LOWER:
            case LOWER_EQUAL:
            case GREATER:
            case GREATER_EQUAL:
            case LOWER_GREATER:
            case END:
            case ELSE:
            case THEN:
            case UNTIL:
            case DO:
                break;
            //simple-expr'->addop term simple-expr'
            case OR:
            case SUB:
            case ADD:
                Addop();Term();SimpleExprPrime();break;         
            default:  
                System.out.println("linha "+lexicalAnalyser.Line+": Expected 'or','-' or '+' " );
                break;
        }  
    }
    void Term()throws IOException{
        switch(token.getName()){
            //term->factor-a term'
            case IDENTIFIER:
            case INTEGER_CONST:
            case FLOAT_CONST:
            case CHAR_CONST:
            case OP_PARENTHESES:
            case NOT:
            case SUB:
                FactorA();TermPrime();break;         
            default:  
                System.out.println("linha "+lexicalAnalyser.Line+": 'identifier','(','not','integer_const','float_const','char_const' or '-'");
                break;
        }  
    }
    void TermPrime()throws IOException{        
        switch(token.getName()){
            //term'->lambda
            case OR:
            case SUB:
            case ADD:
            case SEMICOLON:
            case END:
            case ELSE:
            case CL_PARENTHESES:
            case COMPARATION:
            case LOWER:
            case LOWER_EQUAL:
            case GREATER:
            case GREATER_EQUAL:
            case LOWER_GREATER:
            case THEN:
            case UNTIL:
            case DO:
                break;
            //term'->mulop factor-a term'
            case AND:
            case MUL:
            case DIV:
                Mulop();FactorA();TermPrime();break;         
            default:  
                System.out.println("linha "+lexicalAnalyser.Line+": Expected 'and','*' or '/' ");
                break;
            
        }  
        
    }
    void FactorA()throws IOException{
        switch(token.getName()){
            //factor-a->factor
            case IDENTIFIER:
            case INTEGER_CONST:
            case FLOAT_CONST:
            case CHAR_CONST:
            case OP_PARENTHESES:
                Factor();break;
            //factor-a->not factor
            case NOT:
                Eat(NOT);Factor();break;
            //factor-a->- factor
            case SUB:
                 Eat(SUB);Factor();break;
            default:  
                System.out.println("linha "+lexicalAnalyser.Line+": 'identifier','(','not','integer_const','float_const','char_const' or '-'");
                break;
        }  

    }
    void Factor()throws IOException{
        switch(token.getName()){
            //factor->identifier
            case IDENTIFIER:
                Eat(IDENTIFIER);break;
            //factor->(expression)
            case OP_PARENTHESES:
                Eat(OP_PARENTHESES);Expression();Eat(CL_PARENTHESES);break;
            //factor->constant
            case FLOAT_CONST:
            case CHAR_CONST:
            case INTEGER_CONST:
                Constant();break;
            default:  
                System.out.println("linha "+lexicalAnalyser.Line+": 'identifier','(','integer_const','float_const' or 'char_const'");
                break;
        }  
    }
    void Relop()throws IOException{
         switch(token.getName()){
            //relop->"=" 
            case COMPARATION:
                Eat(COMPARATION);break;
            //relop->">"
            case LOWER:
                Eat(LOWER);break;
            //relop->">="
            case LOWER_EQUAL:
                Eat(LOWER_EQUAL);break;
            //relop-> "<" 	
            case GREATER:
                Eat(GREATER);break;
            //relop->"<="
            case GREATER_EQUAL:
                Eat(GREATER_EQUAL);break;
            //relop->"<>"
            case LOWER_GREATER:
                Eat(LOWER_GREATER);break;  
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": Expected '=','>' ,'>=' , '<' , '<=' or '<>'" );
                break;
        }  
    }
    void Addop()throws IOException{
         switch(token.getName()){
            //addop->or
            case OR:
                Eat(OR);break;
            //addop->+
            case ADD:
                Eat(ADD);break;
            //addop->-
            case SUB:
                Eat(SUB);break;
            default: 
                System.out.println("linha "+lexicalAnalyser.Line+": Expected 'or','-' or '+' " );
                break;  
        }  
    }
    void Mulop()throws IOException{
        switch(token.getName()){
            //mulop->and	
            case AND:
                Eat(AND);break;
            //mulop->*
            case MUL:
                Eat(MUL);break;
            //mulop->/
            case DIV:
                Eat(DIV);break;
            default:
                System.out.println("linha "+lexicalAnalyser.Line+": Expected 'and','*' or '/' ");
                break;
        } 
    }
    void Constant()throws IOException{
           switch(token.getName()){
            //constant->float_const	
            case FLOAT_CONST:
                Eat(FLOAT_CONST);break;
            //constant->char_const
            case CHAR_CONST:
                Eat(CHAR_CONST);break;
            //constant->integer_const
            case INTEGER_CONST:
                Eat(INTEGER_CONST);break;
            default: System.out.println("linha "+lexicalAnalyser.Line+": Expected 'integer_const','float_const' or 'char_const'");
            System.out.println("linha="+lexicalAnalyser.Line);
        }  
    }
}
