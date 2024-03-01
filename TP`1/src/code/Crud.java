package src.code;

import java.io.File;

    /*
    * A classe CRUD abre a database e guarda seu endereço.
    * Basicamente, serve de mensageiro entre as classes de baixa abstração (Arquivo.java, Modelo.java e MyDLL.java)
    * e a classe de alto nivel Main.java, que apenas mostra informações e age como I/O.
    */

public class Crud {

    private String filepath; // Base de dados tratada.

    private Crud()
    {
        filepath = null;
    }

    public Crud(String filepath)
    {
        this.filepath = filepath;
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
        Arquivo header = new Arquivo(filepath);
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
                header.writeModel(allEntriesFromBackupDBs.popDLLStart());
            }

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
                            "GBvideos.csv", "INvideos.csv", /*"JPvideos.csv",*/
                            "KRvideos.csv", "MXvideos.csv", "RUvideos.csv"};

        char[][] countryCodes = {
            {'U', 'S'}, // Row 0
            {'D', 'E'}, // Row 1
            {'F', 'R'}, // Row 2
            {'G', 'B'}, // Row 3
            {'I', 'N'}, // Row 4
          //{'J', 'P'}, // Row 5 // JP is abnormally slow for some reason. // Removed for performance
            {'K', 'R'}, // Row 6
            {'M', 'X'}, // Row 7
            {'R', 'U'}  // Row 8
        };

        MyDLL returnDLL = new MyDLL();
        MyDLL tempDLL = null;
        int db_id[] = {0};
        for (int i = 0; i < 8; i++)
        {   
            MyIO.println("Loading from DB: " + dbNames[i]);
            try
            {
                tempDLL = loadBackupDBIntoDLL (path + dbNames[i], countryCodes[i], db_id);
                returnDLL.mergeDLLs(tempDLL);
            } catch (Exception e) {
                
            }
        }
        return returnDLL;
    }

    // Metodo que carrega e insere cada linha em uma DLL.
    public static MyDLL loadBackupDBIntoDLL (String path, char code[], int[] db_id) throws Exception
    {
        Model tempModel = null;
        MyDLL returnList = new MyDLL();
        Arquivo dataset = new Arquivo(path);
        String loadBuffer = "";
        dataset.openReadUntreated();
        dataset.readLineUntreated(); // Pular a primeira linha, que sempre contém apenas metadados.
        loadBuffer = dataset.readLineContinuous(); // Ler a segunda linha, que já contem informações.
        int i = 0; // Limits how many entries to read per dataset.
        while(loadBuffer.length() > 2 && (loadBuffer.equals("null") == false) && i < 1000)
        {
            if(db_id[0] % 1000 == 0)
            {
                MyIO.println("Loading: #" + db_id[0] + " to #"+ (db_id[0] + 1000) + "/232000");
            }
            try
            {
                tempModel = new Model(loadBuffer, db_id[0], code);
                returnList.addToDLLEnd(tempModel);
                db_id[0] += 1;
                i++;
            } catch (Exception e) {
                MyIO.println("Error at ID: " + db_id[0]);
            } finally {
                loadBuffer = dataset.readLineContinuous();
            }
        }
        return returnList;
    }

    // Metodo que acha o ultimo ID em um arquivo, e o retorna.
    public long findLastID()
    {
        long bytesSkipped = 0;
        File file = new File(filepath);
        long arqLength = file.length();
        Arquivo header = new Arquivo(filepath);
        try {
            header.openRead();
        } catch (Exception e) {
            MyIO.println("Excecao findLastID");
        }

        // Iterar sequencialmente até o final.
        try{
            Boolean tempLapide = header.dosIN.readBoolean();
            long tempID = header.dosIN.readLong();
            int byteSize = header.dosIN.readInt();
            bytesSkipped += 13; //1 Byte for Bool, 4 bytes for an int, 8 bytes for the long;
            while ((bytesSkipped + byteSize) <= arqLength) {
                bytesSkipped += byteSize;
                header.arqIN.skip(byteSize - 13);
                tempLapide = header.dosIN.readBoolean();
                tempID = header.dosIN.readLong();
                byteSize = header.dosIN.readInt();
            }
            header.close();
            return tempID;
        } catch (Exception e) {
            header.close();
            MyIO.println("Error on FindLastID");
        }   
        header.close();
        return -1;
    }

    // Metodo que retorna o N# de registros validos
    public long findAmount()
    {
        long bytesSkipped = 0;
        File file = new File(filepath);
        long arqLength = file.length();
        Arquivo header = new Arquivo(filepath);
        try {
            header.openRead();
        } catch (Exception e) {
            MyIO.println("Excecao findAmount");
        }
        // Iterar sequencialmente até o final.
        try{
            Boolean tempLapide = header.dosIN.readBoolean();
            long tempID = header.dosIN.readLong();
            int byteSize = header.dosIN.readInt();
            int amount = 0;
            bytesSkipped += 13; //1 Byte for Bool, 4 bytes for an int, 8 bytes for the long;
            while ((bytesSkipped + byteSize) <= arqLength) {
                if(tempLapide == false)
                {
                    amount++;
                }
                bytesSkipped += byteSize;
                header.arqIN.skip(byteSize - 13);
                tempLapide = header.dosIN.readBoolean();
                tempID = header.dosIN.readLong();
                byteSize = header.dosIN.readInt();
            }
            header.close();
            return amount;
        } catch (Exception e) {
            header.close();
            MyIO.println("Error on FindLastID");
        }   
        header.close();
        return -1;
    }

    // Metodo CREATE
    public boolean create ()
    {
        // prompt for model details
        String data = Model.readModel();

        MyIO.println("Escreva o codigo do país em que o video foi publicado (US, UK, NK, SK, BR, DE...)");
        String countrycode = MyIO.readLine();
        long lastId = this.findLastID();

        char CountryCode[] = {countrycode.charAt(0), countrycode.charAt(1)};

        if(lastId < 0)
        {
            // Error happened.
            return false;
        }

        // Model que será escrito.
        Model a = new Model(data, lastId + 1, CountryCode);

        File file = new File(filepath);
        long arqLength = file.length();
        Arquivo header = new Arquivo(filepath);
        try {
            header.openWriteAppend();
            header.writeModel(a);

        } catch (Exception e) {
            header.close();
            MyIO.println("Excecao Create");
        }

        return true;
    }

    // Overload do metodo Create quando o metodo Update estoura o tamanho alocado.
    public boolean create (Model a)
    {
        long lastId = this.findLastID();
        if(lastId < 0)
        {
            // Error happened.
            return false;
        }
        File file = new File(filepath);
        long arqLength = file.length();
        Arquivo header = new Arquivo(filepath);
        try {
            header.openWriteAppend();
            header.writeModel(a);

        } catch (Exception e) {
            header.close();
            MyIO.println("Excecao Create");
        }

        return true;
    }

    // Metodo READ

    public boolean read()
    {
        long seekID = MyIO.readLong("ID do registro: \n");
        return read(seekID);
    }

    // Ler um ID
    public boolean read(long id)
    {

        Arquivo header = new Arquivo(filepath);
        try {
            header.openEdit();
            Boolean exists = header.seek(id);
            if(exists)
            {
                Model readModel = new Model(header.RAF);
                readModel.printToString();
                return true;
            }
            else
            {
                MyIO.println("ID nao encontrado no Dataset.");
                return false;
            }
        } catch (Exception e)
        {
            MyIO.println("ID nao encontrado.");
            return false;
        }
    }

    // Metodo UPDATE
    public boolean update ()
    {
        int seekID = MyIO.readInt("ID do registro para dar UPDATE?");

        Arquivo header = new Arquivo(filepath);
        try {
            header.openEdit();
            header.seek(seekID);
            Model prevModel = new Model(header.RAF);
            Model newModel = prevModel.edit();
            // Se o tamanho for menor ou igual, sobre-escreve na mesma posição
            if(newModel.getByteSize() <= prevModel.getByteSize())
            {
                header.RAF.seek(header.RAF.getFilePointer() - prevModel.getByteSize());
                newModel.setByteSize(prevModel.getByteSize());
                header.writeModel(newModel);
                header.close();
                return true;
            }
            else
            {
                // Lapide = True, e depois, criar no fim.
                header.seek(header.RAF.getFilePointer() - prevModel.getByteSize());
                header.dosOUT.writeBoolean(true);
                header.close();
                return this.create(newModel);
            }

        } catch (Exception e) {
            header.close();
            MyIO.println("ID nao encontrado.");
            return false;
        }

    }

    // Metodo DELETE
    // Na verdade, não deletamos. Apenas damos toggle na lapide.
    public boolean delete()
    {
        long seekID = MyIO.readLong("ID do registro: \n");
        return delete(seekID);
    }

    public boolean delete(long ID)
    {
        char confirm = MyIO.readChar("Tem certeza que deseja deletar o registro? y/n\n");
        if (confirm != 'y' && confirm != 'Y')
        {
            return false;
        }

        Arquivo header = new Arquivo(filepath);
        try {
            header.openEdit();
            header.seek(ID);
            // Lapide = True, e depois, criar no fim.
            header.dosOUT.writeBoolean(true);
            header.close();
            return true;
        } catch (Exception e) {
            header.close();
            MyIO.println("ID nao encontrado.");
            return false;
        }
    }
}
