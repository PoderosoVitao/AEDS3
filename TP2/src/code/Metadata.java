package src.code;

/*
 * Classe que é utilizada para ler e escrever um cabeçalho com os metadados da DB
 * 
 * Em ordem, os dados armazenados são:
 * -> Ultimo ID do arquivo
 * -> Proximo ID a ser atribuido
 * -> Tamanho do Arquivo
 * -> # de Registros
 * -> # de Lapides
 * -> # de Registros fora de Posição
 * 
 */

public class Metadata{

    private long lastId;
    private long nextId;
    private long fileSize;
    private int  regNum;
    private int  lapideNum;
    private int  OOPNum;
    public final static byte MetadataHeaderSize = 36;

    public long getFileSize() {
        return fileSize;
    }
    public int getLapideNum() {
        return lapideNum;
    }
    public long getLastId() {
        return lastId;
    }
    public long getNextId() {
        return nextId;
    }
    public int getOOPNum() {
        return OOPNum;
    }
    public int getRegNum() {
        return regNum;
    }
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    public void setLapideNum(int lapideNum) {
        this.lapideNum = lapideNum;
    }
    public void setLastId(long lastId) {
        this.lastId = lastId;
    }
    public void setNextId(long nextId) {
        this.nextId = nextId;
    }
    public void setOOPNum(int oOPNum) {
        OOPNum = oOPNum;
    }
    public void setRegNum(int regNum) {
        this.regNum = regNum;
    }

    public Metadata()
    {
        lastId = -1;
        nextId = 0;
        fileSize = -1;
        regNum = 0;
        lapideNum = 0;
        OOPNum = 0;
    }

    public Metadata(long lastId, long nextId, long fileSize, int regNum, int lapideNum, int OOPnum)
    {
        this.lastId = lastId;
        this.nextId = nextId;
        this.fileSize = fileSize;
        this.regNum = regNum;
        this.lapideNum = lapideNum;
        this.OOPNum = OOPnum;
    }

    public Metadata(Arquivo file)
    {
        try {
            this.lastId    =    file.RAF.readLong();
            this.nextId    =    file.RAF.readLong();
            this.fileSize  =    file.RAF.readLong();
            this.regNum    =    file.RAF.readInt();
            this.lapideNum =    file.RAF.readInt();
            this.OOPNum    =    file.RAF.readInt();
        } catch (Exception e) {
            MyIO.println("Erro METADATA String Constructor");
        }
    }

    public Metadata(String filePath)
    {
        try {
            Arquivo header = new Arquivo(filePath);
            header.openEdit();
            this.lastId      = header.RAF.readLong();
            this.nextId      = header.RAF.readLong();
            this.fileSize    = header.RAF.readLong();
            this.regNum      = header.RAF.readInt();
            this.lapideNum   = header.RAF.readInt();
            this.OOPNum      = header.RAF.readInt();
            header.close();
        } catch (Exception e) {
            MyIO.println("Erro METADATA String Constructor");
        }
    }

}