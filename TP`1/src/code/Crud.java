package src.code;

import java.io.File;

/*
 * Arquivo que apenas provê os metodos do crud que serão usados no Main
 */

public class Crud {

    /*
     * A classe CRUD abre a database e guarda seu endereço. (Mais metadados depois?)
     */

    private String dbPath;
    private File filePath;

    // Esse construtor padrão não deve ser utilizado.
    public Crud ()
    {
        dbPath = null; // Não deve acontecer em uma situação normal.
        filePath = null;
        MyIO.println("ERROR: dbPath set to null!");
    }

    public Crud (String dbPath)
    {
        this.dbPath = dbPath;
        filePath = convertToPath();
    }

    // Converte dbPath em filePath;
    public File convertToPath()
    {
        File temp = new File(dbPath);
        return temp;
    }

    // Abre arquivo
    private void openFile(char type)
    {
        if(validate())
        {
            if(type == 'R') Arq.openRead(dbPath);
            else
            {
                MyIO.println("Sobreescrevendo em: " + dbPath);
                Arq.openWrite(dbPath);
            }
        }
        else
        {
            if(type == 'R') MyIO.println("Arquivo não encontrado: " + dbPath);
            else Arq.openWrite(dbPath);
        }
    }

    // Fecha arquivo
    private void closeFile()
    {
        Arq.close();
    }

    // Metodo que valida se o arquivo carregado existe.
    public boolean validate()
    {
        File temp = new File(dbPath);
        temp = temp.exists() ? temp : null;
        
        if(temp == null) return false;
        return true;
    }
/*
    // Metodo que carrega o conteúdo do arquivo na memória, em uma DLL

    // !CREATE!  Metodo simples que cria um registro do modelo no final de um arquivo aberto.
    // TODO: Permitir escolha de posição.
    public boolean createReg (Model instance)
    {

    }
    // !READ!  Metodo simples que cria um registro do modelo no final de um arquivo aberto.
    // 
    public boolean readReg (Model instance)
    {

    }
    // !UPDATE!  Metodo simples que cria um registro do modelo no final de um arquivo aberto.
    // 
    public boolean updateReg (Model instance)
    {

    }
    // !DELETE!  Metodo simples que cria um registro do modelo no final de um arquivo aberto.
    // 
    public boolean deleteReg (Model instance)
    {

    }
*/
}
