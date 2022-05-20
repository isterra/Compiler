package compiler;

import java.util.HashMap;
import java.util.Map;

public class SymbleTable {

    private HashMap<String, String> symbleTable;
    private int position;

    public SymbleTable() {
        symbleTable = new HashMap<String, String>();
        position = 20;
        SetTable();
    }

    private void SetTable() {
        symbleTable.put("0", "declare");
        symbleTable.put("1", "routine");
        symbleTable.put("2", "begin");
        symbleTable.put("3", "end");
        symbleTable.put("4", "int");
        symbleTable.put("5", "float");
        symbleTable.put("6", "char");
        symbleTable.put("7", "if");
        symbleTable.put("8", "then");
        symbleTable.put("9", "end");
        symbleTable.put("10", "else");
        symbleTable.put("11", "repeat");
        symbleTable.put("12", "until");
        symbleTable.put("13", "while");
        symbleTable.put("14", "do");
        symbleTable.put("15", "read");
        symbleTable.put("16", "write");
        symbleTable.put("17", "not");
        symbleTable.put("18", "or");
        symbleTable.put("19", "and");

    }

    public String SetToken(String lexeme) {
        for (Map.Entry<String, String> st : symbleTable.entrySet()) {
            if (st.getValue().equals(lexeme)) {
                return st.getKey();
            }
        }
        String nextPosition = NextPosition();
        symbleTable.put(nextPosition, lexeme);
        return nextPosition;
    }

    public String IsReservedSymbol(String lexeme) {
        for (Map.Entry<String, String> st : symbleTable.entrySet()) {
            if (st.getValue().equals(lexeme)) {
               if(Integer.parseInt(st.getKey())<=19){
                   return lexeme.toUpperCase();
               }
            }
        }
        return null;
    }

    public String NextPosition() {
        return Integer.toString(position++);
    }
    
    public void PrintHT(){
        for(int i=0;i<position;i++){
            System.out.println(i+" : "+symbleTable.get(Integer.toString(i)));
        }
    }
}
