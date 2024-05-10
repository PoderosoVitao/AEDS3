package src.code;

public class MinHeap {
    
    private NodeIntChar head;
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
        }

        tempList.joinNodesRecursive();
        int a = 5;

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
                NodeIntCharHolder insertPosition = last;
                while(insertPosition.getPrev() != null && insertPosition.getData().getCount() > a.getCount())
                {
                    insertPosition = insertPosition.getPrev();
                }
                NodeIntCharHolder nextPtr = insertPosition.getNext();

                insertPosition.setNext(temp);
                nextPtr.setPrev(temp);

                temp.setPrev(insertPosition);
                temp.setNext(nextPtr);
            }
        }
        elemNum++;
    }

    public NodeIntChar pop()
    {
        if(head == null) return null;
        NodeIntCharHolder temp = head;
        head = head.getNext();
        elemNum--;
        return temp.getData();
    }

    public void joinNodesRecursive()
    {
        // The two leftmost nodes always have the smallest frequency.
        NodeIntChar temp = new NodeIntChar('¨', 0);
        temp.setLeft(this.pop()); 
        temp.setRight(this.pop());
        temp.setCount(temp.getLeft().getCount() + temp.getRight().getCount());
        insertSorted(temp);

        if(elemNum > 1) this.joinNodesRecursive();
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
