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

    public void insert(Index indice)
    {
        if(this.isLeaf() == false)
        {
            int i = 0;
            while(this.data[i] != null)
            {
                if (indice.id < this.data[i].id)
                {
                    if(this.next[i] == null){
                        this.next[i] = new BNode(this.ordem, this.tree);
                        this.leavesNum++;
                        this.next[i].insert(indice);
                        break;
                    }
                    this.next[i].insert(indice);
                    break;
                }
                i++;
            }

            if(this.next[i] == null){
                this.next[i] = new BNode(this.ordem, this.tree);
                this.leavesNum++;
            }
            this.next[i].insert(indice);
        }
        else
        {
            int i = 0;
            while(this.data[i] != null) i++;
            this.data[i] = new Index(indice.id, indice.byteOffset);
            Index tempIndex = null;
            this.dataSize++;
            int j = i;
            while(j > 0 && this.data[j - 1].id > this.data[j].id)
            {
                tempIndex = this.data[j - 1];
                this.data[j - 1] = this.data[j];
                this.data[j] = tempIndex;

                j--;
            }
        }

        if (this.dataSize == this.ordem) {
            this.split();
        }
    }

    public void split()
    {
        BNode leftNode = new BNode(ordem, tree);
        BNode rightNode = new BNode(ordem, tree);

        int i = 0;
        while (i < ordem/2) {
            leftNode.next[i] = this.next[i];
            leftNode.data[i] = this.data[i];
            if(leftNode.next[i] != null) leftNode.leavesNum++;
            if(leftNode.data[i] != null) leftNode.dataSize++;
            i++;
        }
        Index middleNode = new Index(this.data[i].id, this.data[i].byteOffset);
        leftNode.next[i] = this.next[i];
        if(leftNode.next[i] != null) rightNode.leavesNum++;
        i++;
        while (i < ordem) {
            rightNode.next[i-((ordem/2) + 1)] = this.next[i];
            rightNode.data[i-((ordem/2) + 1)] = this.data[i];
            if(rightNode.next[i-((ordem/2) + 1)] != null) rightNode.leavesNum++;
            if(rightNode.data[i-((ordem/2) + 1)] != null) rightNode.dataSize++;
            i++;
        }

        // Insert on parent
        BNode tempParent = this.findParent();
       
        if(tempParent == null)
        {
            this.tree.head = new BNode(this.ordem, this.tree);
            tempParent = this.tree.head;
            this.tree.height++;
            tempParent.leavesNum++;
        }

        tempParent.data[tempParent.dataSize] = middleNode;
        int j = tempParent.dataSize;
        tempParent.dataSize += 1;

        Index tempIndex = null;
        while (j > 0 && tempParent.data[j - 1].id > tempParent.data[j].id) {
            tempIndex = tempParent.data[j - 1];
            tempParent.data[j - 1] = tempParent.data[j];
            tempParent.data[j] = tempIndex;
            tempIndex = null;
            j--;
        }

        tempParent.next[j] = leftNode;
        tempParent.next[tempParent.leavesNum] = rightNode;
        int k = tempParent.leavesNum;
        tempParent.leavesNum += 1;

        BNode tempNode = null;
        while (k > (j + 1)) {
            tempNode = tempParent.next[k - 1];
            tempParent.next[k - 1] = tempParent.next[k];
            tempParent.next[k] = tempNode;
            tempNode = null;
            k--;
        }

        tempParent.sizeCalc();
        leftNode.sizeCalc();
        rightNode.sizeCalc();

    }

    public int remove(int pos)
    {
        return 0;
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

    public boolean isLeaf() {
        return leavesNum == 0;
    }

    // Caminhamento na árvore
    public void caminha(){
        MyIO.println("----");
        for (int i = 0; i < data.length; i++) {
            if(this.next[i] != null) this.next[i].caminha();
            if(this.data[i] != null) MyIO.println(this.data[i].id);
        }
    }

    public void exportNode(Arquivo arq)
    {
        for (int i = 0; i < data.length; i++) {
            if(this.next[i] != null) this.next[i].exportNode(arq);
            if(this.data[i] != null){
                try {
                    arq.dosOUT.writeLong(this.data[i].id);
                    arq.dosOUT.writeLong(this.data[i].byteOffset);
                } catch (Exception e) {
                    MyIO.println("ERRO EXPORT NODE!");
                }
            }
        }
    }

}

