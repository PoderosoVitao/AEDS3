package src.code;

import java.io.*;

public class Huffman {

    // Guarda quantidade de caractéres.
    private int wordAmount = 0;

    public void compactDB (String dataSetPath) throws Exception
    {
        MinHeap codeHeap = buildHeap(dataSetPath);

        // Construir um array índice contendo os códigos de cada letra.
        // O 'valor' unicode da letra é a sua posição no array.

        String[] binArray = codeHeap.fetchBinCodes();
        
        // Por exemplo, o caractére 'a' tem valor ascii de '97'
        // A string na posição 97 do binArray (binArray[97]) corresponde ao caminho para se chegar a 'a'.

        String dataSetAdd = dataSetPath + "Huffman-V1.huff";
        
        Arquivo header = new Arquivo(dataSetPath);
        header.openEdit();
        String entireDBinOneString = header.arqToString();
        header.close();

        header = new Arquivo(dataSetAdd);
        header.openEdit();

        // TODO: Save binArray information to de-compress the file later.

        // Read and write until END OF FILE.
        BitAccumulator bitWriter = new BitAccumulator();

        try {
        for (int i = 0; i < entireDBinOneString.length(); i++) {
            int pos = (char) entireDBinOneString.charAt(i);
            String codeBuffer = binArray[pos];
            if(codeBuffer == null)
            {
                codeBuffer = "";


                /*
                 * Due to the way we generate our index, 
                 * every single character in the DB should be mapped to a corresponding hash
                 * in our binArray.
                 */

                // This should never happen.
                // And yet it does.
            }
            //if(i % 1000 == 0) MyIO.println("i = " + i);
            for (int j = 0; j < codeBuffer.length(); j++) {
                bitWriter.writeBit(codeBuffer.charAt(j), header);
            }
        }
        } catch (Exception e) {
            MyIO.println("Erro!");
        }

        header.close();

        int aaa = 5;
    }

    private MinHeap buildHeap(String dataSetPath) throws Exception
    {
        NodeIntChar[] allNodes = getNodeList(getFrequency(dataSetPath));
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
    private int[] getFrequency(String dataSetPath) throws Exception
    {
        int[] unicodeArray = new int[65536];
        Arquivo header = new Arquivo(dataSetPath);
        header.openEdit();
        long byteOffset = 0;

        Metadata meta = header.readMeta();
        byteOffset += 36;
        
        // Add all meta chars to our unicode array.
        String metaBuffer = "" + meta.getFileSize() + meta.getLapideNum() + 
                             meta.getLastId() + meta.getNextId() + 
                             meta.getOOPNum() + meta.getRegNum();

        // Make models out of all entries and add them to the unicode array. -- O(n) complexity
        while(byteOffset < meta.getFileSize())
        {
            Model buffer = new Model(header.RAF);
            byteOffset += buffer.getByteSize();
            metaBuffer += buffer.printCompact();
        }
        this.wordAmount = metaBuffer.length();
        for (int i = 0; i < wordAmount; i++) unicodeArray[(int) metaBuffer.charAt(i)] += 1;
        header.close();
        return unicodeArray;
    }
}

class BitAccumulator{
    public byte bitWrite;
    public byte count;

    BitAccumulator()
    {
        bitWrite = 0b00000000;
        count = 0;
    }

    public void writeBit(char bit, Arquivo header) throws Exception
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
            count = 0;
        }

    }
}