package src.code;

import java.io.File;

public class Huffman extends TimeSpace {

    // Decompressiona o arquivo.
    public void decompressDB (String dataSetPath) throws Exception
    {
        MyIO.println("Huffman - Descompactacao -- " +"Processando... --" + " Abrindo arquivo comprimido -- 1/6");   
        String dataSetAdd = dataSetPath + "HuffmanCompressao1";
        Arquivo header = new Arquivo(dataSetAdd);
        File fileIn = new File(dataSetAdd);
        header.openEdit();
        // Read binarray stored in file.
        MyIO.println("Huffman - Descompactacao -- " +"Processando... --" + " Lendo arquivo comprimido -- 2/6");
        long byteOffset = header.RAF.readLong();
        long totalSize = new File(dataSetAdd).length();

        MyIO.println("Huffman - Descompactacao -- " +"Processando... --" + " Lendo Frequencias salvas -- 3/6");
        int[] freqArray = readBinInfo(header);

        MyIO.println("Huffman - Descompactacao -- " +"Processando... --" + " Re-construindo Heap -- 4/6");
        MinHeap codeHeap = buildHeap(freqArray);

        MyIO.println("Huffman - Descompactacao -- " +"Processando... --" + " Descompactando Arquivo -- 5/6");

        Arquivo output = new Arquivo(dataSetPath);
        output.openWrite();

        try {
            byte readBuffer = 0;
            int opBuffer = 0;
            treeRunner runner = new treeRunner(codeHeap);
            while(header.RAF.getFilePointer() < totalSize)
            {
                readBuffer = header.RAF.readByte();
                byteOffset++;
                for (int i = 0; i < 8; i++) {
                    opBuffer = (readBuffer >> 7) & 1;
                    readBuffer = (byte) (readBuffer << 1);
                    int test = runner.transverse(opBuffer, output, header.RAF.getFilePointer() >= totalSize - 2);
                }
                if(header.RAF.getFilePointer() % 1000000 == 0) MyIO.println(header.RAF.getFilePointer()/1000000 +
                     "/" + totalSize/1000000 + "MB");
            }
        } catch (Exception e) {
            header.close();
            output.close();
        }
        header.close();
        output.close(); 
        
        MyIO.println("Huffman - Descompactação -- Arquivo Descompactado -- 6/6");
    }

    public void compactDB (String dataSetPath) throws Exception
    {
        MyIO.println("Huffman - Compactacao -- " + "Processando..." + " -- Encontrando Frequencia de cada Caractere -- 1/6");
        int[] letterFrequency = Huffman.getFrequency(dataSetPath);

        MyIO.println("Huffman - Compactacao -- " +"Processando..." + " -- Construindo Heap -- 2/7");
        MinHeap codeHeap = buildHeap(letterFrequency);

        // Construir um array índice contendo os códigos de cada letra.
        // O 'valor' unicode da letra é a sua posição no array.
        MyIO.println("Huffman - Compactacao -- " + "Processando..." + " -- Fazendo o Hashing Char - Caminho HEAP -- 3/6");
        String[] binArray = codeHeap.fetchBinCodes();
        
        // Por exemplo, o caractére 'a' tem valor ascii de '97'
        // A string na posição 97 do binArray (binArray[97]) corresponde ao caminho para se chegar a 'a'.

        String dataSetAdd = dataSetPath + "HuffmanCompressao1";
        
        Arquivo input = new Arquivo(dataSetPath);
        File fInput = new File(dataSetPath);
        input.openEdit();

        Arquivo header = new Arquivo(dataSetAdd);
        header.openWrite();

        MyIO.println("Huffman - Compactacao -- " + "Processando..." + " -- Salvando HEAP no Arquivo -- 4/6");

        // Save binArray information to de-compress the file later.
        long bitAmount = saveBinInfo(letterFrequency, header);

        // Read and write until END OF FILE.
        BitAccumulator bitWriter = new BitAccumulator();


        MyIO.println("Huffman - Compactacao -- " + "Processando..." + " -- Escrevendo Caracteres -- 5/6");
        int pos = 0;
        Boolean lastByte = false;
        Boolean hasWritten = false;
        try {
            for (int i = 0; i < fInput.length();) {
                if(i < fInput.length() - 1){
                    pos = (int) input.RAF.readChar();
                    i +=2;
                }
                else {
                    pos = (int) input.RAF.readByte();
                    i +=1;
                    lastByte = true;
                }
                String codeBuffer = binArray[pos];
                if(codeBuffer == null)
                {
                    codeBuffer = "";
                }
                for (int j = 0; j < codeBuffer.length(); j++) {
                    hasWritten = bitWriter.writeBit(codeBuffer.charAt(j), header);
                }
                if(i % 1000000 == 0) MyIO.println(i / 1000000 + " MegaBits escritos.");
            }
            if(!hasWritten && lastByte) bitWriter.forceWrite(header);
        } catch (Exception e) {
            MyIO.println("Erro!");
        }

        MyIO.println("Huffman - Compactacao -- " + "Arquivo compactado! --" + " 6/6");

        MyIO.println(" Bytes Total: "+ (bitWriter.writtenBytes + bitAmount));

        input.close();
        header.close();
    }

    // Saves the binArray with codes for each word.
    private long saveBinInfo(int[] binArray, Arquivo header) throws Exception
    {
        int toIncrease = 0;
        for (int a : binArray) if(a > 0) toIncrease++;

        long byteAmount = toIncrease * 6; // char + int = 6

        byteAmount += 16; // long + int = 12
        header.dosOUT.writeLong(byteAmount);
        header.dosOUT.writeInt(binArray.length);
        header.dosOUT.writeInt(toIncrease);

        for (int i = 0; i < binArray.length; i++) {
            if(binArray[i] > 0)
            {
                header.dosOUT.writeChar((char) i);
                header.dosOUT.writeInt(binArray[i]);
            }
        }

        return byteAmount;
    }

    // Read binArray header to later decompress the file.
    private int[] readBinInfo(Arquivo header) throws Exception
    {
        int num = header.RAF.readInt();
        int validPositions = header.RAF.readInt();
        int[] binArray = new int[num];
        for (int i = 0; i < validPositions; i++) 
            binArray[(int)header.RAF.readChar()] = header.RAF.readInt();
        return binArray;
    }

    private MinHeap buildHeap(int[] dataSetFreq) throws Exception
    {
        NodeIntChar[] allNodes = getNodeList(dataSetFreq);
        NodeIntChar.quicksort(allNodes);
        
        // Cria heap que associa cada letra a um código em binário.
        MinHeap HuffmanHeap = new MinHeap(allNodes);
        return HuffmanHeap;
    }

    private NodeIntChar[] getNodeList(int[] frequencies)
    {
        int realsize = 0;
        for (int i : frequencies) if(i > 0) realsize++;

        NodeIntChar[] allNodes = new NodeIntChar[realsize];

        for (int i = 0, j = 0; i < frequencies.length; i++){
            if(frequencies[i] > 0) allNodes[j++] = new NodeIntChar((char) i, frequencies[i]);
        }
        return allNodes;
    }

    // Construir índice que contêm todos os caractéres no texto e sua frequência
    public static int[] getFrequency(String dataSetPath) throws Exception
    {
        int[] unicodeArray = new int[65536];
        Arquivo header = new Arquivo(dataSetPath);
        File fileIn = new File(dataSetPath);
        header.openEdit();
        long byteOffset = 0;
        
        if(fileIn.length() % 2 == 1)
        {
            while(byteOffset < fileIn.length())
            {
                if(byteOffset < fileIn.length() - 1){
                    unicodeArray[(int) header.RAF.readChar()]++;
                }
                else {
                    unicodeArray[(int) header.RAF.readByte()]++;
                }
                byteOffset += 2;
            }
        }
        else
        {
            while(byteOffset < fileIn.length())
            {
                unicodeArray[(int) header.RAF.readChar()]++;
                byteOffset+= 2;
            }
        }
        header.close();
        return unicodeArray;
    }
}

class BitAccumulator{
    public long writtenBytes;
    public byte bitWrite;
    public byte count;

    BitAccumulator()
    {
        writtenBytes = 0;
        bitWrite = 0b00000000;
        count = 0;
    }

    public Boolean writeBit(char bit, Arquivo header) throws Exception
    {
        if(bit == '0')
        {
            bitWrite = (byte) (bitWrite << 1);
            count++;
        }
        else if(bit == '1')
        {
            bitWrite = (byte) ((bitWrite << 1) | 0b00000001);
            count++;
        }
        
        if(count >= 8)
        {
            header.dosOUT.writeByte(bitWrite);
            writtenBytes += 1;
            count = 0;
            return true;
        }
        return false;
    }
    public void forceWrite(Arquivo header) throws Exception
    {
        while(count < 8)
        {
            bitWrite = (byte) (bitWrite << 1);
            count++;
        }
        header.dosOUT.writeByte(bitWrite);
    }
}

class treeRunner{
    MinHeap tree;
    NodeIntChar currentPTR;
    private long cycle;

    treeRunner(MinHeap a)
    {
        tree = a;
        currentPTR = a.head;
    }

    // Percorre o Heap a procura do código de um caractere.
    public int transverse(int a, Arquivo header, Boolean isSingleByte)
    {
        if (a == 0)
        {
            currentPTR = currentPTR.getLeft();
        }
        else{
            currentPTR = currentPTR.getRight();
        }

        if(currentPTR.getLeft() == null && currentPTR.getRight() == null)
        {
            try {
                char retchar = currentPTR.getCharac();
                if(isSingleByte && retchar < 255) header.dosOUT.writeByte(retchar);
                else header.dosOUT.writeChar(retchar);
                currentPTR = tree.head;
                return 1;
            } catch (Exception e) {
                MyIO.println("ERRO transverse HUFFMAN");
            }
        }
        cycle++;
        return -1;
    }

    // Percorre os ultimos bytes, tendo cuidado com um possivel 'padding' no final.
    public int transverseLast(int a, Arquivo header, int cyc)
    {
        if (a == 0)
        {
            currentPTR = currentPTR.getLeft();
        }
        else{
            currentPTR = currentPTR.getRight();
        }

        if(currentPTR.getLeft() == null && currentPTR.getRight() == null)
        {
            try {
                char retchar = currentPTR.getCharac();
                header.dosOUT.writeChar(retchar);
                currentPTR = tree.head;
                return 1;
            } catch (Exception e) {
                MyIO.println("ERRO transverse HUFFMAN");
            }
        }
        return -1;
    }

}