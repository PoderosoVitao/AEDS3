package src.code;

import java.io.File;

    /*
    * A classe CRUD abre a database e guarda seu endereço.
    * Basicamente, serve de mensageiro entre as classes de baixa abstração (Arquivo.java, Modelo.java e MyDLL.java)
    * e a classe de alto nivel Main.java, que apenas mostra informações e age como I/O.
    */

public class Crud {

    private String filepath; // Base de dados tratada.
    private Arquivo header;  // Cabeçote

    private Crud()
    {
        filepath = null;
        header = null;
    }

    public Crud(String filepath)
    {
        this.filepath = filepath;
        header = null;
    }

    // Checa se o arquivo tem um caminho valido.
    public Boolean exists()
    {
        File file = new File(filepath);
        return file.exists();
    }

    // Metodo CREATE

    // Metodo READ

    // Metodo UPDATE

    // Metodo DELETE
}
