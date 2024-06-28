package src.code;

public class BoyerMoore {  
    String filepath;
    Crud myCrud;

    public BoyerMoore(String filepath, Crud myCrud)
    {
        this.filepath = filepath;
        this.myCrud = myCrud;
    }
    
    public void findPadrao(String padrao) {
        int[] badCharTable = buildBadCharTable(padrao);
        int m = padrao.length();
        
        for (int i = 0; i < myCrud.meta.getRegNum(); i++) {
            Model temp = myCrud.get(i);
            if(temp != null) {
                String buffer = temp.printToCSV();
                
                int n = buffer.length();
                int s = 0; // shift of the pattern with respect to text
                
                while(s <= (n - m)) {
                    int j = m - 1;
    
                    // Keep reducing index j of pattern while characters of
                    // pattern and text are matching at this shift s
                    while(j >= 0 && padrao.charAt(j) == buffer.charAt(s + j)) {
                        j--;
                    }
                    
                    // If the pattern is present at current shift, then index j
                    // will become -1 after the above loop
                    if (j < 0) {
                        System.out.println("Padrao encontrado!");
                        System.out.println("ID: " + i +" Posicao: " + s);
                        s += (s + m < n) ? m - badCharTable[buffer.charAt(s + m)] : 1;
                    } else {
                        s += Math.max(1, j - badCharTable[buffer.charAt(s + j)]);
                    }
                }
            }
        }
    }
    
    private int[] buildBadCharTable(String padrao) {
        final int ALPHABET_SIZE = 65536; // tamanho do alfabeto UTF-8
        int[] badCharTable = new int[ALPHABET_SIZE];
        // Inicializa todas as ocorrências como -1
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            badCharTable[i] = -1;
        }
        // Preenche a última ocorrência de um caractere
        for (int i = 0; i < padrao.length(); i++) {
            badCharTable[padrao.charAt(i)] = i;
        }
        return badCharTable;
    }
}
