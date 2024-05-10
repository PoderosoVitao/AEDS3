package src.code;
public class Huffman {

    public static void buildHeap(String dataSetPath) throws Exception
    {
        NodeIntChar[] allNodes = getNodeList(getFrequency(dataSetPath));
        NodeIntChar.quicksort(allNodes);
        
        MinHeap HuffmanHeap = new MinHeap(allNodes);

    }

    public static NodeIntChar[] getNodeList(int[] frequencies)
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
        for (int i = 0; i < metaBuffer.length(); i++) unicodeArray[(int) metaBuffer.charAt(i)] += 1;
        header.close();
        return unicodeArray;
    }
}