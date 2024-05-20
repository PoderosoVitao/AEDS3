package src.code;
import java.io.File;

public class LZW extends TimeSpace {

    // Faz a compressão do arquivo utilizando LZW.
    public void compressDB(String dataSetPath) throws Exception
    {
        MyIO.println("LZW - Compactacao -- Processando..." + " -- Abrindo arquivo -- 1/5");   
        String dataSetAdd = dataSetPath + "LZWCompressao1";
        
        Arquivo baseDataBase = new Arquivo(dataSetPath);
        Arquivo compressedFile = new Arquivo(dataSetAdd);

        MyIO.println("LZW - Compactacao -- Processando..." + " -- Construindo Dicionário -- 2/5");  
        
        // Passo 1: Construir Dicionário.
        DictionaryHash dicionario = new DictionaryHash(1000);
        dicionario.populate();
        
        // Passo 2: Lendo metadados.
        baseDataBase.openRead();
        MyIO.println("LZW - Compactacao -- Processando..." + " -- Lendo Metadados -- 3/5");  
        Metadata meta = new Metadata(baseDataBase);
        baseDataBase.RAF.seek(0); // Voltar ao ínicio.
        MyIO.println("LZW - Compactacao -- Processando..." + " -- Compactando Arquivo -- 4/5");  
        
        // Passo 3: Compactação.
        compressedFile.openWrite();

        for (long byteCount = 0; byteCount < meta.getFileSize() + 36;) {
            String buffer = "";
            char lastChar = '\0';
            int code     =  0;
            int tempCode =  0;
            
            if(byteCount < meta.getFileSize() + 36 - 1)
            {
                buffer += baseDataBase.RAF.readChar();
                byteCount +=  2;

                // Look for matches in dictionary
                code = dicionario.search(buffer);
                tempCode = code;

                while(tempCode != -1 && byteCount <= meta.getFileSize() + 36)
                {
                    code       = tempCode;
                    if(byteCount < meta.getFileSize() + 36 - 1)
                    {
                        buffer    += baseDataBase.RAF.readChar();
                        tempCode   = dicionario.search(buffer);
                    }
                    else if(byteCount < meta.getFileSize() + 36)
                    {
                        lastChar = (char)baseDataBase.RAF.readByte();
                        buffer += lastChar;
                        tempCode = dicionario.search(buffer);
                        byteCount +=1;
                    }
                    else tempCode = -1;
                
                    byteCount += 2;
                }
                byteCount -= 2;
                baseDataBase.RAF.seek(baseDataBase.RAF.getFilePointer() - 2);
            }
            else
            {
                buffer += baseDataBase.RAF.readByte();
                code   = dicionario.search(buffer);
                byteCount +=1;
            }

            compressedFile.dosOUT.writeInt(code);
            dicionario.insert(buffer);
            if(lastChar != '\0')
            {
                code   = dicionario.search("" + lastChar);
                compressedFile.dosOUT.writeInt(code);
            }

            if(byteCount % 1000000 == 0){
                MyIO.println(byteCount/1000000 + "MB/" + meta.getFileSize()/1000000 + "MB  " + (byteCount * 100)/meta.getFileSize() + "%");
            }
        }
        compressedFile.dosOUT.writeInt(dicionario.elemNum);

        MyIO.println("LZW - Compactacao -- " + "Compactação Finalizada -- 5/5");  
        baseDataBase.close();
        compressedFile.close();
    }

    // Faz a decompressão do arquivo que foi comprimido por LZW.

    public void decompressDB(String dataSetPath) throws Exception
    {
        MyIO.println("LZW - Descompactacao -- Processando..." + " -- Abrindo arquivo -- 1/5");   
        String dataSetAdd = dataSetPath + "LZWCompressao1";
        
        Arquivo baseDataBase = new Arquivo(dataSetAdd);
        Arquivo compressedFile = new Arquivo(dataSetPath);

        MyIO.println("LZW - Descompactacao -- Processando..." + " -- Lendo Metadado -- 2/5");
        baseDataBase.openEdit();
        File file = new File(dataSetAdd);
        baseDataBase.RAF.seek(file.length() - 4);
        int dataSize = baseDataBase.RAF.readInt();
        baseDataBase.RAF.seek(0);

        MyIO.println("LZW - Descompactacao -- Processando..." + " -- Construindo Indice para decompressão. -- 3/5");

        // Poderiamos usar o mesmo dicionário, mas vamos garantir um acesso ainda mais rapido usando um array.
        // Como salvamos a quantidade de elementos como um 'footer' no arquivo, sabemos a quantidade fixa, máxima
        // de elementos no array.
        String[] dictionaryIndex = new String[dataSize];
        // É só não termos mais que 2^32 elementos...

        int dicNum = 0;
        for (dicNum = 0; dicNum < 65536; dicNum++) dictionaryIndex[dicNum] = "" + (char) dicNum;

        MyIO.println("LZW - Descompactacao -- Processando..." + " -- Descompactando Arquivo -- 4/5");  
        // Passo 3: Descompactação.
        compressedFile.openWrite();
        for (long byteCount = 0; byteCount < file.length() - 4;) {
            int bufferN = baseDataBase.RAF.readInt();
            int bufferH = -1;
            byteCount +=  4;
            if(byteCount < file.length() - 4)
            {
                bufferH = baseDataBase.RAF.readInt();
                baseDataBase.RAF.seek(baseDataBase.RAF.getFilePointer() - 4);
                if(dictionaryIndex[bufferH] == null){
                    dictionaryIndex[dicNum++] = "" + dictionaryIndex[bufferN]
                     + dictionaryIndex[bufferN].charAt(0);
                }
                else dictionaryIndex[dicNum++] = "" + dictionaryIndex[bufferN] + dictionaryIndex[bufferH].charAt(0);
            }
            String writeBuffer = dictionaryIndex[bufferN];

            if(byteCount >= file.length() - 4 && bufferN < 255 && writeBuffer.length() == 1)
            {
                compressedFile.dosOUT.writeByte((byte)writeBuffer.charAt(0));
            }

            else
            {
                for (int i = 0; i < writeBuffer.length(); i++) {
                    compressedFile.dosOUT.writeChar(writeBuffer.charAt(i));
                }
            }

            if((byteCount) % 3600000 == 0){
                MyIO.println(byteCount/1000000 + "MB/" + (file.length() - 8)/1000000 + "MB  " + (byteCount * 100)/(file.length() - 8) + "%");
            }
        }

        MyIO.println("LZW - Descompactacao  --" + " Arquivo Descompactado 5/5");

        baseDataBase.close();
        compressedFile.close();
    }
}
