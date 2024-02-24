package src.code;
import java.io.IOError;
import java.io.IOException;
import java.io.RandomAccessFile;

// Classe que gerencia as operações do arquivo. Serve de API para a classe CRUD.
// Utiliza RAF.

public class Arquivo {
    
    private String filepath;
    private RandomAccessFile RAF;

    public Arquivo()
    {
        filepath = null;
    }

    public Arquivo(String filepath)
    {
        this.filepath = filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void     openWrite() throws IOException
    {
        RAF = new RandomAccessFile(filepath, "rw");
    }

    public void openRead() throws IOException
    {
        RAF = new RandomAccessFile(filepath, "r");
    }

    public void seek(long n) throws IOException
    {
        RAF.seek(n);
    }

    public void writeToFile(String a) throws IOException
    {
        RAF.writeBytes(a);
    }

    // Metodo que trunca o arquivo.
    public void truncateFile() throws IOException
    {
        RAF.setLength(RAF.getFilePointer());
    }

    public void truncateFile(int len) throws IOException
    {
        RAF.setLength(len);
    }

    public void close()
    {
        try
        {
            RAF.close();
            RAF = null;
        }
        catch(Exception e)
        {
            MyIO.println("Erro ao fechar RAF.");
        }
    }

    public String readLine() throws IOException
    {
        String returnString = "";
        returnString += RAF.readLine();
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
        returnString += readLine();

        while (returnString.charAt(returnString.length()-1) != '"') {
            returnString += readLine();
        }

        return returnString;
    }
}
