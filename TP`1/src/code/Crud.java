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
    public void openFile(char type)
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
    public void closeFile()
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

    // Skips the first line of the .csv file, which only contains metadata.
    public void skipMetadata()
    {
        Arq.readLine(); // Skip first line
        Arq.readLine(); // Load first data
    }

    // Recarrega o arquivo de DB, condicionando o arquivo original.
    public void reloadDB()
    {
        String buffer = "";
        long db_id = 0;
        Model temp = null;
        MyDLL newLista = new MyDLL();
        Arq.openRead(dbPath);
        skipMetadata();

        // Esse loop while basicamente adiciona todas as entradas validas para uma lista dupla-encadeada
        // Acresenta o país em que o vídeo foi postado e cria um 'ID' do dataset, que é diferente do ID do video.
        while (buffer.length() > 1)
        { 
            try{
            temp = new Model(buffer, db_id, "United States of America");
            db_id += 1;
            newLista.addToDLLEnd(temp);
            }catch (Exception e) {
                //MyIO.println("Exception at ID: "+ db_id);
            }finally{
            buffer = Arq.readLine();
            }
        }
        Arq.close();
        MyIO.println("Done! " + db_id);

        // Salvar como NewDB.csv
        Arq.openWrite("NewDB.csv");
        Arq.println("db_id,video_id,trending_date,title,channel_title,category_id,publish_time,tags,views,likes,dislikes,comment_count,thumbnail_link,comments_disabled,ratings_disabled,video_error_or_removed,country,description");
        while (newLista.getSize() > 0) {
            Arq.println(newLista.popDLLStart().printToCSV());
        }
        Arq.close();
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
