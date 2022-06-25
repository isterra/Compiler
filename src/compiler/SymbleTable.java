package compiler;

import java.util.HashMap;
import java.util.Map;

public class SymbleTable {

    private final HashMap<Integer, String> symbleTable;
    private int position;

    public SymbleTable() {
        symbleTable = new HashMap<>();
        position = 20;
        SetTable();
    }

    private void SetTable() {
        symbleTable.put(0, "declare");
        symbleTable.put(1, "routine");
        symbleTable.put(2, "begin");
        symbleTable.put(3, "end");
        symbleTable.put(4, "int");
        symbleTable.put(5, "float");
        symbleTable.put(6, "char");
        symbleTable.put(7, "if");
        symbleTable.put(8, "then");
        symbleTable.put(9, "else");
        symbleTable.put(10, "repeat");
        symbleTable.put(11, "until");
        symbleTable.put(12, "while");
        symbleTable.put(13, "do");
        symbleTable.put(14, "read");
        symbleTable.put(15, "write");
        symbleTable.put(16, "not");
        symbleTable.put(17, "or");
        symbleTable.put(18, "and");
    }

    public int SetToken(String lexeme) {
        for (Map.Entry<Integer, String> st : symbleTable.entrySet()) {
            if (st.getValue().equals(lexeme)) {
                return st.getKey();
            }
        }
        int nextPosition = NextPosition();
        symbleTable.put(nextPosition, lexeme);
        return nextPosition;
    }

    public String IsReservedSymbol(String lexeme) {
        for (Map.Entry<Integer, String> st : symbleTable.entrySet()) {
            if (st.getValue().equals(lexeme)) {
               if(st.getKey()<=19){
                   return lexeme.toUpperCase();
               }
            }
        }
        return null;
    }

    public int NextPosition() {
        return position++;
    }
    
    public void PrintHT(){
        System.out.println("************************");
        System.out.println("** Tabela de Simbolos **");
        System.out.println("************************");
        for(int i=0;i<position;i++){
            System.out.println("      "+i+" : "+symbleTable.get(i));
        }
    }
}
