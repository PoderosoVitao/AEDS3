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
        this.header = new Arquivo(this.filepath);
    }

    // Checa se o arquivo tem um caminho valido.
    public Boolean exists()
    {
        File file = new File(filepath);
        return file.exists();
    }

    // Metodo Reload, que recarrega a DB tratada a partir das 10 DBs 'normais'
    // Esse metodo sempre sobreescreve todo o arquivo.
    public boolean reloadDB()
    {
        String backupDBsAddress = "E:/Software/Programming/Github/AEDS3/TP`1/Database/t/";
        if(this.exists())
        {
            MyIO.println("DB exists. Are you sure you want to overwrite it? y/n");
            if(MyIO.readChar() == 'y' || MyIO.readChar() == 'Y');
            else return false;
        }

        MyDLL allEntriesFromBackupDBs = loadBackupDBIntoDLL(backupDBsAddress);

        try {

            header.openWrite();
            while (allEntriesFromBackupDBs.getSize() > 0) {
                header.writeToFile(allEntriesFromBackupDBs.popDLLStart().printToCSV());
            }
            header.truncateFile();

        } catch (Exception e) {
            e.printStackTrace(System.out);
            return false;
        } finally {

            header.close();
        }

        return true;
    }

    // Metodo que carrega todos os Backups em uma DLL, e os trata.
    public static MyDLL loadBackupDBIntoDLL (String path)
    {
        String[] dbNames = {"USvideos.csv", "DEvideos.csv", "FRvideos.csv",
                            "GBvideos.csv", "INvideos.csv", "JPvideos.csv",
                            "KRvideos.csv", "MXvideos.csv", "RUvideos.csv"};

        char[][] countryCodes = {
            {'U', 'S'}, // Row 0
            {'D', 'E'}, // Row 1
            {'F', 'R'}, // Row 2
            {'G', 'B'}, // Row 3
            {'I', 'N'}, // Row 4 // IN is abnormally slow for some reason.
            {'J', 'P'}, // Row 5
            {'K', 'R'}, // Row 6
            {'M', 'X'}, // Row 7
            {'R', 'U'}  // Row 8
        };

        MyDLL returnDLL = new MyDLL();
        MyDLL tempDLL = null;
        for (int i = 0; i < 9; i++)
        {   
            try
            {
                tempDLL = loadBackupDBIntoDLL (path + dbNames[i], countryCodes[i]);
                returnDLL.mergeDLLs(tempDLL);
            } catch (Exception e) {
                
            }
        }
        return returnDLL;
    }

    // Metodo que carrega e insere cada linha em uma DLL.
    public static MyDLL loadBackupDBIntoDLL (String path, char code[]) throws Exception
    {
        Model tempModel = null;
        MyDLL returnList = new MyDLL();
        Arquivo dataset = new Arquivo(path);
        String loadBuffer = "";
        dataset.openRead();
        dataset.readLine(); // Pular a primeira linha, que sempre contém apenas metadados.
        loadBuffer = dataset.readLineContinuous(); // Ler a segunda linha, que já contem informações.
        int db_id = 0;
        while(loadBuffer.length() > 2 && db_id < 25)
        {
            try
            {
                tempModel = new Model(loadBuffer, db_id, code);
                returnList.addToDLLEnd(tempModel);
                db_id += 1;
            } catch (Exception e) {
                MyIO.println("Error at ID: " + db_id);
            } finally {
                loadBuffer = dataset.readLineContinuous();
            }
            //MyIO.println("Loaded: #" + db_id);
        }
        return returnList;
    }

    // Metodo CREATE

    // Metodo READ

    // Metodo UPDATE

    // Metodo DELETE
}
