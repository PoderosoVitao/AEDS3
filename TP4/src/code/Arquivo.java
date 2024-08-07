package src.code;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

// Classe que gerencia as operações do arquivo. Serve de API para a classe CRUD.

public class Arquivo {
    
    private String filepath;

    // Usadas para a DS tratada

    public FileOutputStream arqOUT;
    public DataOutputStream dosOUT;
    public FileInputStream arqIN;
    public DataInputStream dosIN;

    // Usadas para os DS não tratados
    public BufferedReader buffIN;

    // Usadas para o metodo Update e Remove
    public RandomAccessFile RAF;
    

    public Arquivo()
    {
        filepath = null;
    }

    public Arquivo(String filepath)
    {
        this.filepath = filepath;
        arqIN  = null;
        dosIN  = null;
        arqOUT = null;
        arqOUT = null;
        buffIN = null;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void openWrite() throws IOException
    {
        arqOUT = new FileOutputStream(filepath);
        dosOUT = new DataOutputStream(arqOUT);
    }

    public void openWriteAppend() throws IOException
    {
        arqOUT = new FileOutputStream(filepath, true);
        dosOUT = new DataOutputStream(arqOUT);
    }

    public void openRead() throws IOException
    {
        RAF = new RandomAccessFile(filepath, "r");
        arqIN = new FileInputStream(filepath);
        dosIN = new DataInputStream(arqIN);
    }

    public void openEdit() throws IOException
    {
        RAF = new RandomAccessFile(filepath, "rw");
        arqOUT = new FileOutputStream(RAF.getFD());
        dosOUT = new DataOutputStream(arqOUT);
        arqIN  = new FileInputStream(RAF.getFD());
        dosIN  = new DataInputStream(arqIN);
    }

    // Metodo que le das DBs backup.
    public void openReadUntreated() throws IOException
    {
        buffIN = new BufferedReader(new FileReader(filepath));
    }

    // Escreve como um fluxo de bytes a partir de um modelo.
    public void writeModel(Model a) throws IOException
    {
        // Write Lapide before anything else.

        dosOUT.writeBoolean(a.getLapide());

        // DB_ID and ByteSize are written first, to allow finding indexes and skipping them effectively.

        dosOUT.writeLong(a.getDb_id());
        dosOUT.writeInt(a.getByteSize());

        // Then attributes with a fixed size are written.

        dosOUT.writeByte(a.getCategory_id());

        dosOUT.writeInt(a.getComment_count());

        dosOUT.writeLong(a.getViews());
        dosOUT.writeLong(a.getLikes());
        dosOUT.writeLong(a.getDislikes());

        String country = a.getCountry();
        dosOUT.writeChar(country.charAt(0));
        dosOUT.writeChar(country.charAt(1));

        dosOUT.writeBoolean(a.getComments_disabled());
        dosOUT.writeBoolean(a.getRatings_disabled());
        dosOUT.writeBoolean(a.getVideo_error_or_removed());

        // Then variable size strings.

        dosOUT.writeUTF(a.getVideo_id());
        dosOUT.writeUTF(a.getTrending_date());
        dosOUT.writeUTF(a.getTitle());
        dosOUT.writeUTF(a.getChannel_title());
        dosOUT.writeUTF(a.getPublish_time());
        dosOUT.writeUTF(a.getTags());
        dosOUT.writeUTF(a.getThumbnail_link());
        dosOUT.writeUTF(a.getDescription());
    }

    public void writeMeta(Metadata a) throws IOException
    {
        dosOUT.writeLong(a.getLastId());
        dosOUT.writeLong(a.getNextId());
        dosOUT.writeLong(a.getFileSize());
        dosOUT.writeInt(a.getRegNum());
        dosOUT.writeInt(a.getLapideNum());
        dosOUT.writeInt(a.getOOPNum());
    }

    public Metadata readMeta() throws IOException
    {
        Metadata retData = new Metadata(this);
        return retData;
    }

    public void close()
    {
        try {
            if (arqIN != null) {
                arqIN.close();
                arqIN = null;
            }
            if (arqOUT != null) {
                arqOUT.close();
                arqOUT = null;
            }
            if (dosIN != null) {
                dosIN.close();
                dosIN = null;
            }
            if (dosOUT != null) {
                dosOUT.close();
                dosOUT = null;
            }
            if (buffIN != null) {
                buffIN.close();
                buffIN = null;
            }
        } catch (Exception e) {
            MyIO.println("Error closing some file streams.");
        }
    }

    //

    public String readLineUntreated() throws IOException
    {
        String returnString = "";
        returnString += buffIN.readLine();
        return returnString;
    }

    /*
     * Esse metodo não é tão auto-explicativo quanto o readLine.
     * Em vez de apenas lermos uma linha, consideramos que podem haver carecteres especiais em um dataset não tratado 
     * como \n, que podem interferir na leitura POR LINHA. (A linha pode terminar antes dos dados) (Isso acontece MUITO com
     * o campo DESCRIÇÃO no Dataset que eu escolhi, por isso um metodo especifico para tratar esse erro.)
     * 
     * Para lidar com esse problema, checamos se o ultimo caractere é '"' Se sim, temos uma linha completa.
     * Se não, lemos a proxima e adicionamos-a.
     */

    public String readLineContinuous() throws IOException
    {
        String returnString = "";
        returnString += readLineUntreated();

        while ((returnString.charAt(returnString.length()-1)) != '"' 
            && (returnString.equals(null) == false) 
            && (returnString.length() > 10)) {
            returnString += readLineUntreated();
        }

        return returnString;
    }

    // Metodo SEEK, move o cabeçote para um ID específico de maneira linear.
    public boolean seekLinear(long id)
    {
        long bytesSkipped = Metadata.MetadataHeaderSize;
        File file = new File(filepath);
        long arqLength = file.length();

        // Iterar sequencialmente até um ID.
        try{
            // Skip header
            RAF.seek(RAF.getFilePointer() + Metadata.MetadataHeaderSize);
            Boolean tempLapide = this.RAF.readBoolean();
            long tempID = this.RAF.readLong();
            int byteSize = this.RAF.readInt();
            bytesSkipped += 13; //1 Byte for Bool, 4 bytes for an int, 8 bytes for the long;
            while ((bytesSkipped + byteSize) <= arqLength) {
                if(tempID == id && tempLapide == false)
                {
                    RAF.seek(RAF.getFilePointer() - 13);
                    return true;
                }
                bytesSkipped += byteSize;
                this.arqIN.skip(byteSize - 13);
                tempLapide = this.dosIN.readBoolean();
                tempID = this.dosIN.readLong();
                byteSize = this.dosIN.readInt();
            }
            if(tempID == id && tempLapide == false)
                {
                    RAF.seek(RAF.getFilePointer() - 13);
                    return true;
                }
        } catch (Exception e) {
            MyIO.println("Error on FindLastID");
        }
        return false;   
    }

    // Avança o cabeçote um numero baseado no índice em B-tree
    public boolean seekIndex(long id, MyBTree index)
    {
        File file = new File(filepath);
        Index temp = index.search(id);

        try {
            // Não é necessário pular os metadados, pois eles já estão inclusos no offset do índice.
            RAF.seek(RAF.getFilePointer() + temp.byteOffset);
            return true;
        } catch (Exception e) {
            MyIO.println("Exceção seekIndex");
        }

        return false;   
    }

    // Converte um arquivo inteiro em uma string de simbolos.
    public String arqToString()
    {
        try{
        long byteOffset = 0;

        Metadata meta = this.readMeta();
        byteOffset += 36;
        
        // Add all meta chars to our unicode array.
        String metaBuffer = "" + meta.getFileSize() + ',' + meta.getLapideNum() + ',' +
                             meta.getLastId() + ',' + meta.getNextId() + ',' +
                             meta.getOOPNum() + ',' + meta.getRegNum() + ',';

        // Make models out of all entries and add them to the unicode array. -- O(n) complexity
        while(byteOffset < meta.getFileSize())
        {
            Model buffer = new Model(this.RAF);
            byteOffset += buffer.getByteSize();
            metaBuffer += buffer.printCompact();
        }
        return metaBuffer;
        } catch (Exception e) {
            MyIO.println("Erro arqToString!!");
        }    
        return null;
    }
}