package src.code;

import java.io.File;

    /*
    * A classe CRUD abre a database e guarda seu endereço.
    * Basicamente, serve de mensageiro entre as classes de baixa abstração (Arquivo.java, Modelo.java e MyDLL.java)
    * e a classe de alto nivel Main.java, que apenas mostra informações e age como I/O.
    */

public class Crud {

    private String filepath; // Base de dados tratada.
    private String indexPath; // Arquivo de índices em B-tree
    private MyBTree BTreeIndex;
    public Metadata meta; // Metadados do arquivo.

    private Crud()
    {
        filepath = null;
        indexPath = null;
        BTreeIndex = null;
        meta = null;
    }

    public Crud(String filepath)
    {
        this.filepath = filepath;
        this.indexPath = null;
        BTreeIndex = null;
        meta = this.getData();
    }

    public Crud(String filepath, String indexPath)
    {
        this.filepath = filepath;
        this.indexPath = indexPath;
        BTreeIndex = MyBTree.importTree(indexPath);
        meta = this.getData();
    }

    // Checa se o arquivo tem um caminho valido.
    public Boolean exists()
    {
        File file = new File(filepath);
        return file.exists();
    }

    // Metodo Reload, que recarrega a DB tratada a partir das 10 DBs 'normais'
    // Esse metodo sempre sobreescreve todo o arquivo.
    public boolean reloadDB ()
    {
        return reloadDB("E:/Software/Programming/Github/AEDS3/Database/t/");
    }

    public boolean reloadDB(String path)
    {
        Arquivo header = new Arquivo(filepath);
        String backupDBsAddress = "E:/Software/Programming/Github/AEDS3/Database/t/";
        if(this.exists())
        {
            MyIO.println("DB exists. Are you sure you want to overwrite it? y/n");
            if(MyIO.readChar() == 'y' || MyIO.readChar() == 'Y');
            else return false;
        }

        MyDLL allEntriesFromBackupDBs = loadBackupDBIntoDLL(backupDBsAddress);

        try {
            header.openWrite();
            // Get data for metadata first.
            long lastByte  = allEntriesFromBackupDBs.tail.getData().getDb_id();
            int regAmount  = allEntriesFromBackupDBs.getSize();
            long byteSize  = allEntriesFromBackupDBs.getByteSize();

            Metadata meta = new Metadata(lastByte, lastByte + 1, byteSize, regAmount, 0, 0);
            
            // MetaData takes 36 bytes to write.
            header.writeMeta(meta);
            long byteCounter = 36;

            // Create B-Tree for the index.
            MyBTree indexBTree = new MyBTree(8);

            // Create temp Index to store the ID in the B-tree.
            Index temp = new Index(0, byteCounter);

            while (allEntriesFromBackupDBs.getSize() > 0) {
                // Pop the model to be written from the DLL
                Model toWrite = allEntriesFromBackupDBs.popDLLStart();

                // Get the ID and byteOffset for the index file
                temp.id = toWrite.getDb_id();
                temp.byteOffset = byteCounter;

                // Update the byteCounter with the current size
                byteCounter += toWrite.getByteSize();

                // Finally, write the model into the file AND insert the index into the B-tree.
                header.writeModel(toWrite);
                indexBTree.insert(temp);
            }

            // Export B-tree to a file.
            // indexBTree.camninha();
            indexBTree.export(this.indexPath);

        } catch (Exception e) {
            e.printStackTrace(System.out);
            return false;
        } finally {
            header.close();
        }

        BTreeIndex = MyBTree.importTree(indexPath);
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
        //  {'J', 'P'}, // Row 5 // JP is abnormally slow for some reason. // Removed for performance
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
                MyIO.println("ERROR DB: " + dbNames[i]); 
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
        while(loadBuffer.length() > 2 && (loadBuffer.equals("null") == false) && i < 50)
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
                //MyIO.println("Error at ID: " + db_id[0]);
            } finally {
                loadBuffer = dataset.readLineContinuous();
            }
        }
        return returnList;
    }

    // Metodo CREATE
    public boolean create ()
    {
        // prompt for model details
        String data = Model.readModel();

        MyIO.println("Escreva o codigo do país em que o video foi publicado (US, UK, NK, SK, BR, DE...)");
        String countrycode = MyIO.readLine();
        char CountryCode[] = {countrycode.charAt(0), countrycode.charAt(1)};

        Arquivo header = new Arquivo(filepath);
        try {
            header.openEdit();
            Metadata meta = new Metadata(header);
            header.close();

            // Model que será escrito.
            Model a = new Model(data, meta.getNextId(), CountryCode);
            meta.setRegNum(meta.getRegNum() + 1);
            meta.setNextId(meta.getNextId() + 1);
            meta.setLastId(a.getDb_id());
            meta.setFileSize(meta.getFileSize() + a.getByteSize());

            // Escrever metadados.
            header.openEdit();
            header.writeMeta(meta);
            header.close();

            // Escrever o dado novo.
            header.openWriteAppend();
            header.writeModel(a);
            header.close();

            ///////////////////////// ATUALIZAR ÍNDICE B tree, se existir.
            if(indexPath != null)
            {

            }
            

        } catch (Exception e) {
            header.close();
            MyIO.println("Excecao Create");
        }

        return true;
    }

    // Overload do metodo Create quando o metodo Update estoura o tamanho alocado.
    public boolean create (Model a)
    {

        Arquivo header = new Arquivo(filepath);
        try {
            header.openWriteAppend();
            header.writeModel(a);
            header.close();

            // Ler o byteOffset do último registro:

            header.openEdit();
            // Posicionar cabeçote
            Boolean exists = false;
            if(this.BTreeIndex != null) header.seekIndex(meta.getLastId(), BTreeIndex);
            else header.seekLinear(meta.getLastId());
            Model readModel = new Model(header.RAF);
            Index newIndice = new Index(a.getDb_id(), a.getByteSize() + readModel.getByteSize());
            // Escrever índice
            BTreeIndex.insert(newIndice);

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
            Boolean exists = false;

            // Posicionar cabeçote
            if(this.BTreeIndex != null) exists = header.seekIndex(id, BTreeIndex);
            else exists = header.seekLinear(id);

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

            // Posicionar cabeçote
            if(this.BTreeIndex != null) header.seekIndex(seekID, BTreeIndex);
            else header.seekLinear(seekID);

            Model prevModel = new Model(header.RAF);
            Model newModel = prevModel.edit();
            // Se o tamanho for menor ou igual, sobre-escreve na mesma posição e PRONTO.
            // TODO: Metodo que acha registros que podem ser encurtados
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
                header.RAF.seek(header.RAF.getFilePointer() - prevModel.getByteSize());
                header.dosOUT.writeBoolean(true);
                header.close();

                // Model que será escrito.
                header.openEdit();
                Metadata meta = new Metadata(header);
                header.close();
                meta.setRegNum(meta.getRegNum() + 1);
                meta.setLapideNum(meta.getLapideNum() + 1);
                meta.setOOPNum(meta.getOOPNum() + 1);
                meta.setLastId(newModel.getDb_id());
                meta.setFileSize(meta.getFileSize() + newModel.getByteSize());

                // Escrever metadados.
                header.openEdit();
                header.writeMeta(meta);
                header.close();

                // Escrever o dado novo.
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
            Metadata meta = new Metadata(header);
            header.close();
            meta.setLapideNum(meta.getLapideNum() + 1);
            header.openEdit();
            header.writeMeta(meta);
            header.close();
            header.openEdit();

            // Posicionar cabeçote
            if(this.BTreeIndex != null) header.seekIndex(ID, BTreeIndex);
            else header.seekLinear(ID);
            
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

    public Metadata getData()
    {
        try{
            Arquivo header = new Arquivo(filepath);
            header.openEdit();
            Metadata a = new Metadata(header);
            header.close();
            return a;
        }catch(Exception e){
            MyIO.println("Erro GETDATA");
            return null;
        }
    }

    public void saveBTree()
    {
        this.BTreeIndex.export(this.indexPath);
    }

}
