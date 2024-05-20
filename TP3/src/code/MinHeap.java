package src.code;

public class MinHeap {
    
    public NodeIntChar head;
    private int elemNum;

    // Constroi MinHeap a partir de um array com frequencias
    // Em que o índice do array é a posição unicode de um caractere
    // EX: Ocorrencias de a == frequencies[97]
    // EX: Ocorrencias de b == frequencies[98]

    public MinHeap(NodeIntChar[] frequencies)
    {
        NodeIntCharList tempList = new NodeIntCharList();
        for (int i = 0; i < frequencies.length; i++) {
            tempList.insert(frequencies[i]);
            this.elemNum++;
        }

        tempList.joinNodesAll();
        this.head = tempList.head.getData();
    }

    public String[] fetchBinCodes()
    {
        String[] binArray = new String[65536];

        // Realizar o caminhamento.
        String caminho = "";
        fetchBinCodes(head, binArray, caminho);


        return binArray;
    }

    private void fetchBinCodes(NodeIntChar node, String[] binArray, String caminho)
    {
        String caminhoNext = "";
        caminhoNext += caminho;

        if(node != null){
            if(node.getLeft() == null && node.getRight() == null ) binArray[(int) node.getCharac()] = caminho;
            else{
            if(node.getLeft() != null) fetchBinCodes(node.getLeft(), binArray, caminhoNext + "0");
            if(node.getRight() != null) fetchBinCodes(node.getRight(), binArray, caminhoNext + "1");
            }
        }

    }

    // Inserir um Node já pronto
    private void insertNode(char a, int b)
    {
        if(head == null) head = new NodeIntChar(a, b);
        else insertNode(a, b, head);
        elemNum++;
    }
    
    private void insertNode(char a, int b, NodeIntChar node)
    {

        if(b > node.getCount()){
            if(node.getRight() != null) insertNode(a, b, node.getRight());
            else{
                node.setRight(new NodeIntChar(a, b));
                node.getRight().setPrev(node);
            }
        }

        else if (b <= node.getCount()){
            if(node.getLeft() != null) insertNode(a, b, node.getLeft());
            else{
                node.setLeft(new NodeIntChar(a, b));
                node.getLeft().setPrev(node);
            }
        }
    }
}

// Lista DLL para tipos NodeIntChar
// Em prática, só é usada para a construção do Heap.
class NodeIntCharList {
    public NodeIntCharHolder head;
    public NodeIntCharHolder last;
    public int elemNum;

    public NodeIntCharList()
    {
        head = null;
        last = head;
        elemNum = 0;
    }

    public void insert(NodeIntChar a)
    {
        NodeIntCharHolder temp = new NodeIntCharHolder(a);
        if (head==null){
            head = temp;
            last = head;
        }
        else{
            last.setNext(temp);
            last.getNext().setPrev(last);
            last = last.getNext();
        }
        elemNum++;
    }

    public void insertSorted(NodeIntChar a)
    {
        if (head==null){
            head = new NodeIntCharHolder(a);
            last = head;
        }
        else{
            NodeIntCharHolder temp = new NodeIntCharHolder(a);
            if(a.getCount() >= last.getData().getCount())
            {
                last.setNext(temp);
                temp.setPrev(last);
                last = temp;
            }
            else
            {
                NodeIntCharHolder positionWeWillInsertTempIn = this.head;
                while(temp.getData().getCount() > positionWeWillInsertTempIn.data.getCount() && positionWeWillInsertTempIn.getNext() != null)
                    positionWeWillInsertTempIn = positionWeWillInsertTempIn.getNext();
                
                if(positionWeWillInsertTempIn.getPrev() != null){
                positionWeWillInsertTempIn.getPrev().setNext(temp);
                temp.setNext(positionWeWillInsertTempIn);
                temp.setPrev(positionWeWillInsertTempIn.getPrev());
                positionWeWillInsertTempIn.setPrev(temp);
                }
                else{ 
                    temp.setNext(positionWeWillInsertTempIn);
                    positionWeWillInsertTempIn.setPrev(temp);
                    head = temp;
                }
            }
        }
        elemNum++;
    }

    public NodeIntChar pop()
    {
        NodeIntChar returnVal = head.getData();
        head = head.getNext();
        if(head != null) head.setPrev(null);
        elemNum--;

        return returnVal;
    }

    public void joinNodesAll()
    {
        // The two leftmost nodes always have the smallest frequency.
        while(this.head != this.last && elemNum > 1)
        {
            NodeIntChar temp = new NodeIntChar((char) 0, 0);
            NodeIntChar leftBuffer = this.pop();
            NodeIntChar rightBuffer = this.pop();

            temp.setRight(rightBuffer);
            temp.setLeft(leftBuffer);
            temp.setCount(temp.getLeft().getCount() + temp.getRight().getCount());
            temp.setCharac('\0');
           
            this.insertSorted(temp);
        }
    }
}

class NodeIntCharHolder {
    public NodeIntChar data;
    public NodeIntCharHolder next;
    public NodeIntCharHolder prev;

    public NodeIntCharHolder()
    {
        next = null;
        prev = null;
        data = null;
    }
    public NodeIntCharHolder(NodeIntChar a)
    {
        next = null;
        prev = null;
        data = a;
    }

    public NodeIntCharHolder getNext() {
        return next;
    }
    public NodeIntCharHolder getPrev() {
        return prev;
    }
    public NodeIntChar getData() {
        return data;
    }
    public void setData(NodeIntChar data) {
        this.data = data;
    }
    public void setNext(NodeIntCharHolder next) {
        this.next = next;
    }
    public void setPrev(NodeIntCharHolder prev) {
        this.prev = prev;
    }
}
