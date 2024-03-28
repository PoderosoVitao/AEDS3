package src.code;

import javax.swing.tree.TreeNode;

// Implementação esquizofrênica e não otimizada de nós de arvode B com ordem N
public class BNode {

    public int ordem;
    public int dataSize;
    public int leavesNum;
    public MyBTree tree;
    public Index data[];
    public BNode next[];

    public BNode()
    {}

    public BNode(int ordem, MyBTree tree)
    {
        this.ordem     = ordem           ;
        this.dataSize  = 0               ;
        this.leavesNum = 0               ;
        this.tree      = tree            ;
        this.data      = new Index[ordem];
        this.next      = new BNode[ordem + 1];
    }

    // Insere indice na B-tree. Retorna posição em que foi inserido.
    public int insert(Index index, BNode parent)
    {
        int i = dataSize;
        this.data[dataSize] = new Index(index.id, index.byteOffset);
        dataSize++;

        // Sort the last element
        // We insertion-sort only the last element of the list. This is always O(n).
        while(i > 0 && this.data[i].id < this.data[i - 1].id)
        {
            // Swap
            Index temp = this.data[i - 1];
            this.data[i - 1] = this.data[i];
            this.data[i] = temp;
            i--;
        }

        if(this.dataSize == this.ordem)
        {
            this.split(parent);
        }

        return i;
    }

    // Retorna a posição em que um elemento seria inserido sem inserir.
    public int calcPos(Index index)
    {
        int i = 0;
        while(this.data[i] != null && index.id > this.data[i].id)
        {
            i++;
        }
        return i;
    }

    // This method should only be called if the tree is full
    public void split(BNode parent)
    {
        // Get middle element
        int middleOf = this.dataSize / 2;
        Index middleElement = this.data[middleOf];
        
        // Make two B-Trees with each 'half' of the original
        BNode tempLeft = new BNode(ordem, this.tree);
        for (int i = 0; i < middleOf; i++) {
            tempLeft.data[i] = new Index(this.data[i].id, this.data[i].byteOffset);
            tempLeft.next[i] = this.next[i];
        }
        tempLeft.sizeCalc();

        BNode tempRight = new BNode(ordem, this.tree);
        int i = middleOf + 1;
        int j = 0;
        for (i = middleOf + 1; i < ordem; i++, j++) {
            j = 0;
            tempRight.data[j] = new Index(this.data[i].id, this.data[i].byteOffset);
            tempRight.next[j] = this.next[i];
        }
            tempRight.next[j] = this.next[i]; // Copy the last extra 'next'
            tempRight.sizeCalc();


        // Bring middle element up to the previous tree.
        int insertPos = 0;
        if(this.tree.head == this)
        {
            BNode newHead = new BNode(this.ordem, this.tree);
            this.tree.head = newHead;
            newHead.insert(middleElement, null);
            this.tree.height++;

            // Add both sub-trees to the tree.
            newHead.next[insertPos] = tempLeft;
            newHead.next[insertPos + 1] = tempRight;
            newHead.sizeCalc();
        }
        else
        {
            insertPos = parent.calcPos(middleElement);
            parent.next[insertPos] = tempLeft;
            parent.next[insertPos + 1] = tempRight;
            parent.insert(middleElement, parent.findParent());
            parent.sizeCalc();
        }
    }

    // Calcula a quantidade de registros validos e o tamanho das folgas.
    public void sizeCalc()
    {
        int iSize = 0;
        for (Index index : data) {
            if (index != null) iSize++;
        }
        int lSize = 0;
        for (BNode nod : next) {
            if (nod != null) lSize++;
        }

        this.leavesNum = lSize;
        this.dataSize = iSize;
    }

    public BNode findParent()
    {
        return findParent(this.tree.head, this);
    }

    public BNode findParent(BNode current, BNode node) {
        if (current == null || current == node)
            return null;
        
        for (BNode nNode : current.next) {
            if (nNode == node)
            return current;
        }

        BNode temp;
        for (int i = 0; i < current.ordem; i++) {
            temp = findParent(current.next[i], node);
            if(temp != null) return temp;
        }
        
        return null;
    }

    public void merge()
    {

    }
}

