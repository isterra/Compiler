/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.Models;

/**
 *
 * @author israel.terra
 */
public class TOKEN {
    private String Lexeme;
    private String Name;
    
    public TOKEN(String Name,String Lexeme){
       this.Lexeme=Lexeme;
       this.Name=Name;
    }
    public TOKEN(String Name){
        this.Name=Name;
        Lexeme=null;
    }
    
    public String getLexeme() {
        return Lexeme;
    }

    public void setLexeme(String Lexeme) {
        this.Lexeme = Lexeme;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
    @Override
    public String toString(){
        if(Lexeme!=null)
            return "<"+Name+","+Lexeme+">";
        return  "<"+Name+">";
    }
}
