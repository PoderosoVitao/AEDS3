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

    // Inserção na arvore B
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

    // Split da B-tree apos inserção
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

    public Index remove(long key) {

        Index returnIndex = null;

        BNode targetNode = this.huntNode(key);
        if (targetNode == null) return returnIndex;

        int targetPosition = 0;
        while (targetNode.data[targetPosition].id != key) targetPosition++;
        BNode parentNode1 = targetNode.findParent();
        int positionInRegardsToTheParentNode = 0;

        while (targetNode.next[positionInRegardsToTheParentNode]!= targetNode) positionInRegardsToTheParentNode++;

        /*  #############################################################
           ### Casos 1.x = O nó é uma folha, ou uma cabeça sem folhas. ###
            ############################################################# */
        if(targetNode.isLeaf()){
            // Caso 1.1 = Remover o nó não faz com que a folha tenha menos do que ordem/2 elementos.
            // Ou estamos no nó cabeça sem folhas.
            if(targetNode.dataSize > ordem / 2 || (parentNode1 == null && targetNode.leavesNum == 0)){
                for (Index index : targetNode.data) if(index.id == key) index = null;
                    for (int i = 0; i < dataSize; i++) {
                        if(targetNode.data[i] == null)
                        {
                            targetNode.data[i] = targetNode.data[i+1];
                            targetNode.data[i+1] = null;
                        }
                }
                targetNode.dataSize--;
            }
            else if(parentNode1 != null)
            {
                BNode sideNode = null;
                // Caso 1.2 = O nó tem Datasize igual a ordem/2 ou menor.

                // Caso 1.2.1 -> Se o vizinho da esquerda poder emprestar um, rodamos a árvore com ele.
                if((positionInRegardsToTheParentNode - 1) >= 0 &&
                    parentNode1.next[positionInRegardsToTheParentNode - 1] != null &&
                    parentNode1.next[positionInRegardsToTheParentNode - 1].dataSize > ordem/2 ){ 
                    sideNode = parentNode1.next[positionInRegardsToTheParentNode - 1];
                    // Pegar o elemento mais da direita.
                    Index tempIndex = null;
                    for (int i = 0; i < sideNode.dataSize; i++) {
                        if(sideNode.data[i] == null)
                        {
                            tempIndex = sideNode.data[i -1];
                            sideNode.data[i - 1] = null;
                            sideNode.dataSize -= 1;
                            break;
                        }
                    }

                    // Descer o elemento mais a esquerda do pai e substitui-lo pelo elemento do filho.
                    targetNode.data[targetPosition] = parentNode1.data[0];
                    parentNode1.data[0] = tempIndex;

                    // Sortear a folha para garantir que tudo está em ordem.
                    // Insertion sort.
                    for (int i = 1; i < targetNode.dataSize; i++) {
                        Index a = targetNode.data[i];
                        int j = i;
                        while ((j > 0) && (a.id < targetNode.data[j - 1].id)) {
                            targetNode.data[j] = targetNode.data[j - 1];
                            j-= 1;
                        }
                        targetNode.data[j] = a;
                    }

                }

                // Caso 1.2.2 -> Se o vizinho da direita poder emprestar um, rodamos a árvore com ele.
                else if((positionInRegardsToTheParentNode - 1) >= 0 && 
                    parentNode1.next[positionInRegardsToTheParentNode + 1] != null &&
                    parentNode1.next[positionInRegardsToTheParentNode + 1].dataSize > ordem/2){
                    sideNode = parentNode1.next[positionInRegardsToTheParentNode + 1];
                    // Pegar o elemento mais da esquerda.
                    Index tempIndex = sideNode.data[0];
                    for (int i = 0; i < sideNode.dataSize; i++) {
                        if(sideNode.data[i + 1] != null) sideNode.data[i] = sideNode.data[i+1];
                    }
                    sideNode.dataSize--;
                    
                    // Descer o elemento mais a direita do pai e substitui-lo pelo elemento do filho.
                    int pos = 0;
                    while(parentNode1.data[pos] != null) pos++;

                    targetNode.data[targetPosition] = parentNode1.data[pos - 1];
                    parentNode1.data[pos - 1] = tempIndex;

                    // Sortear a folha para garantir que tudo está em ordem.
                    // Insertion sort.
                    for (int i = 1; i < targetNode.dataSize; i++) {
                        Index a = targetNode.data[i];
                        int j = i;
                        while ((j > 0) && (a.id < targetNode.data[j - 1].id)) {
                            targetNode.data[j] = targetNode.data[j - 1];
                            j-= 1;
                        }
                        targetNode.data[j] = a;
                    }
                }
            
                // Caso 1.2.3 -> Nenhum dos vizinhos podem emprestar uma migalha de índice.
                else{

                    // if(parentNode1.dataSize <= ordem/2) parentNode1.borrowFromNearestNode();
                    // TODO: borrowFromNearestNode();
                
                    // Nesse caso, descemos um elemento do topo para uma das folhas.
                    Index parentElementToLowerDown = null;
                    int positionOfTakenElement = 0;
                    if (positionInRegardsToTheParentNode > 1) {
                        positionOfTakenElement = positionInRegardsToTheParentNode - 1;
                    }
                    parentElementToLowerDown = parentNode1.data[positionOfTakenElement];
                    
                    // Sempre fazemos o troca-troca com o elemento à esquerda.
                    int positionOfElementToMergeWith = positionInRegardsToTheParentNode - 1;
                    if (positionInRegardsToTheParentNode == 0) positionOfElementToMergeWith = 1;
                    BNode sidenode = parentNode1.next[positionOfElementToMergeWith];

                    // Fazer merge nos dois nodes, mais o node de cima.
                    targetNode.data[targetPosition] = null;
                    targetNode.dataSize--;

                    {
                        for (int i = 0, j = 0; i < ordem; i++) {
                                if(targetNode.data[i] == null)
                                {
                                    if(sideNode.data[j] != null)
                                    {
                                    targetNode.data[i] = sideNode.data[j];
                                    sideNode.data[j] = null;
                                    sideNode.dataSize--;
                                    targetNode.dataSize++;
                                    j++;
                                    }
                                    else
                                    {
                                        targetNode.data[i] = parentElementToLowerDown;
                                        break;
                                    }
                                }
                        }
                    }

                    // Sortear a folha para garantir que tudo está em ordem.
                    // Insertion sort.
                    for (int i = 1; i < targetNode.dataSize; i++) {
                        Index a = targetNode.data[i];
                        int j = i;
                        while ((j > 0) && (a.id < targetNode.data[j - 1].id)) {
                            targetNode.data[j] = targetNode.data[j - 1];
                            j-= 1;
                        }
                        targetNode.data[j] = a;
                    }

                    parentNode1.data[positionOfTakenElement] = null;
                    parentNode1.dataSize--;
                    // Re-ordenar Next e Data
                    for (int i = 0; i < ordem - 1; i++) {
                        if(parentNode1.data[i] == null) 
                        {
                            parentNode1.data[i] = parentNode1.data[i+1];
                            parentNode1.data[i+1] = null;
                        }
                    }

                    parentNode1.next[positionInRegardsToTheParentNode] = null;
                    parentNode1.leavesNum--;

                    for (int i = 0; i < ordem; i++) {
                        if(parentNode1.next[i] == null) 
                        {
                            parentNode1.next[i] = parentNode1.next[i+1];
                            parentNode1.next[i+1] = null;
                        }
                    }
                }
            }
        }

        /*  #############################################################
            ###         Casos 2.x = O nó não é uma folha.             ###
            ############################################################# */

                // TODO Casos 2.x

        return null;
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

    // Retorna o BNode que contém uma determinada chave.
    public BNode huntNode (long key)
    {
        BNode returnNode = null;
        boolean found = false;
        int i = 0;

        for (Index index : data) {
            if(index.id == key)
            {
                returnNode = this;
                found = true;
                return returnNode;
            }
        }

        while(found == false && i < this.leavesNum)
        {
            if (key < returnNode.data[i].id)
            {
                return returnNode.next[i].huntNode(key);
            }
            else i += 1;
        }

        return returnNode;
    }

}

