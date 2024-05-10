package src.code;
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
        rightNode.next[i-((ordem/2) + 1)] = this.next[i];
        if(rightNode.next[i-((ordem/2) + 1)] != null) rightNode.leavesNum++;

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
        if (targetNode == null) return returnIndex; // Doesn't exist. Return null.

        int targetPosition = 0;
        while (targetNode.data[targetPosition].id != key) targetPosition++;
        BNode parentNode1 = targetNode.findParent();
        int positionInRegardsToTheParentNode = 0;

        if(parentNode1 != null) while (parentNode1.next[positionInRegardsToTheParentNode]!= targetNode) positionInRegardsToTheParentNode++;

        returnIndex = targetNode.data[targetPosition];

        /*  ##############################################################
            ### Casos 1.x = O nó é uma folha, ou uma cabeça sem folhas ###
            ############################################################## */

        if(targetNode.isLeaf()){
            // Caso 1.1 = Remover o nó não faz com que a folha tenha menos do que ordem/2 elementos.
            // Ou estamos no nó cabeça sem folhas.
            if(targetNode.dataSize >= ordem / 2 || (parentNode1 == null && targetNode.leavesNum == 0)){
                int tempInt = 0;
                for (Index index : targetNode.data)
                {
                    if(index != null && index.id == key) targetNode.data[tempInt] = null;
                    tempInt++;
                }
                for (int i = 0; i < ordem - 1; i++) {
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
                    parentNode1.next[positionInRegardsToTheParentNode - 1].dataSize >= ordem/2 ){ 
                    sideNode = parentNode1.next[positionInRegardsToTheParentNode - 1];
                    // Pegar o elemento mais da direita.
                    Index tempIndex = null;
                    for (int i = 0; i < ordem; i++) {
                        if(sideNode.data[i] == null)
                        {
                            tempIndex = sideNode.data[i -1];
                            sideNode.data[i - 1] = null;
                            sideNode.dataSize -= 1;
                            break;
                        }
                    }

                    // Descer o elemento mais a esquerda do pai e substitui-lo pelo elemento do filho.
                    targetNode.data[targetPosition] = parentNode1.data[positionInRegardsToTheParentNode - 1];
                    parentNode1.data[positionInRegardsToTheParentNode - 1] = tempIndex;

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
                else if((positionInRegardsToTheParentNode) <= ordem && 
                    parentNode1.next[positionInRegardsToTheParentNode + 1] != null &&
                    parentNode1.next[positionInRegardsToTheParentNode + 1].dataSize >= ordem/2){
                    sideNode = parentNode1.next[positionInRegardsToTheParentNode + 1];
                    // Pegar o elemento mais da esquerda.
                    Index tempIndex = sideNode.data[0];
                    for (int i = 0; i < sideNode.dataSize; i++) {
                        if(sideNode.data[i + 1] != null) sideNode.data[i] = sideNode.data[i+1];
                        else sideNode.data[i] = null;
                    }
                    sideNode.dataSize--;
                    
                    // Descer o elemento mais a direita do pai e substitui-lo pelo elemento do filho.
                    targetNode.data[targetPosition] = parentNode1.data[positionInRegardsToTheParentNode];
                    parentNode1.data[positionInRegardsToTheParentNode] = tempIndex;

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
                    sideNode = parentNode1.next[positionOfElementToMergeWith];

                    // Fazer merge nos dois nodes, mais o node de cima.
                    targetNode.data[targetPosition] = null;
                    targetNode.dataSize--;
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
                                    targetNode.dataSize++;
                                    break;
                                }
                            }
                    
                    }

                    // Sortear a folha para garantir que tudo está em ordem.
                    // Insertion sort.
                    for (int i = 1; i < ordem; i++) {
                        Index a = targetNode.data[i];
                        int j = i;
                        while ((j > 0) && (a!= null && (targetNode.data[j-1] == null || a.id < targetNode.data[j - 1].id))) {
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

                    parentNode1.next[positionOfElementToMergeWith] = null;
                    parentNode1.leavesNum--;

                    // Caso o nó seja o nó cabeça e esteja sem elementos
                    if(parentNode1.dataSize == 0)
                    {
                        this.tree.head = targetNode;
                        this.tree.height--;
                    }
                    else{

                        for (int i = 0; i < ordem; i++) {
                            if(parentNode1.next[i] == null) 
                            {
                                parentNode1.next[i] = parentNode1.next[i+1];
                                parentNode1.next[i+1] = null;
                            }
                        }

                        // Garantir que o pai ainda tem o número minimo de registros
                        if(parentNode1.dataSize < (ordem/2 - 1)) parentNode1.merge();
                    }
                }
            }
        }

        /*  #############################################################
            ###         Casos 2.x = O nó não é uma folha              ###
            ############################################################# */
        
        else
        { // Caso 2.0 = Removemos o elemento e substituimos com um dos elementos do filho.
          // Sempre pegamos o elemento mais à direita do filho à esquerda.
            
          BNode sonNode = targetNode.next[targetPosition];

            // Garantir que o filho é folha.
            while(!sonNode.isLeaf())
            {
                int sonElementPos = 0;
                for (int i = 0; i < sonNode.ordem && sonNode.next[i] != null; i++) sonElementPos = i;
                sonNode = sonNode.next[sonElementPos];
            }

            // Pegar o elemento mais a direita do filho.
            int sonElementPos = 0;
            for (int i = 0; i < sonNode.dataSize && sonNode.data[i] != null; i++) sonElementPos = i;

            // Troca-troca
            targetNode.data[targetPosition] = sonNode.data[sonElementPos];
            sonNode.data[sonElementPos] = null;
            sonNode.dataSize--;

            if(sonNode.dataSize < (ordem/2 - 1)) sonNode.merge();
        }
    


        return returnIndex;
    }

    // Faz o merge do nó atual com outro nó.
    // Só chamamos esse metodo em nós com ordem menor do que ordem/2
    private void merge()
    {
        BNode parentNode = this.findParent();
        
        // Nó cabeça.
        if(parentNode == null)
        {  // Basta juntar todos os filhos.
           /* DEPRECATED CODE.
             if(this.dataSize == 0){
                boolean canMerge = true;
                for (BNode leafHead : this.next) {
                    if(leafHead.dataSize >= (ordem/2))
                    {
                        canMerge = false;
                        break;
                    }
                }
                if(canMerge == true){
                    BNode newHead = new BNode(ordem, this.tree);

                    // Pegar todos os nextPTR e DATA da esquerda, intercalando com o da head.
                    int tracksPrevHeadData = 0;
                    int keepsTrackOfHeadData = 0, keepsTrackOfHeadNextPTR = 0;
                    
                    // For each node of the previousHead 
                    for (BNode leafHead : this.next) {
                        if(leafHead != null)
                        {
                            // If it comes BEFORE the first element in our head.
                            if(this.data[tracksPrevHeadData] != null && (leafHead.data[dataSize - 1].id < this.data[tracksPrevHeadData].id))
                            {
                                for (Index index : leafHead.data) if(index != null) newHead.data[keepsTrackOfHeadData++] = index;
                                for (BNode nNode : leafHead.next) if(nNode != null) newHead.next[keepsTrackOfHeadNextPTR++] = nNode;  
                            }

                            // Otherwise...
                            else
                            {
                                // The head has elements remaining that weren't added...
                                if(this.data[tracksPrevHeadData] != null) newHead.data[keepsTrackOfHeadData++] = this.data[tracksPrevHeadData++];
                                // Otherwise, continue adding
                                else
                                {
                                    for (Index index : leafHead.data) if(index != null) newHead.data[keepsTrackOfHeadData++] = index;
                                    for (BNode nNode : leafHead.next) if(nNode != null) newHead.next[keepsTrackOfHeadNextPTR++] = nNode;
                                }
                            }
                        }
                    }
                    this.tree.height =- 1;
                    this.tree.head = newHead;
                }
            }*/
            if(this.dataSize == 0)
            {
                this.tree.head = this.next[0];
                this.tree.height--;
            }
        }

        // Nó não cabeça
        else
        {
            int positionInRegardsToTheParentNode = 0;
            while (parentNode.next[positionInRegardsToTheParentNode]!= this) positionInRegardsToTheParentNode++;
        
            BNode sideNode = null;
                // Caso 1.2 = O nó tem Datasize igual a ordem/2 ou menor.

                // Caso 1.2.1 -> Se o vizinho da esquerda poder emprestar um, rodamos a árvore com ele.
                if((positionInRegardsToTheParentNode - 1) >= 0 &&
                    parentNode.next[positionInRegardsToTheParentNode - 1] != null &&
                    parentNode.next[positionInRegardsToTheParentNode - 1].dataSize >= ordem/2 ){ 
                    sideNode = parentNode.next[positionInRegardsToTheParentNode - 1];
                    // Pegar o elemento mais da direita.
                    Index tempIndex = null;
                    BNode tempChildR = null;
                    for (int i = 0; i < ordem; i++) {
                        if(sideNode.data[i] == null)
                        {
                            tempIndex = sideNode.data[i -1];
                            sideNode.data[i - 1] = null;
                            tempChildR = sideNode.next[i];
                            sideNode.next[i] = null;
                            sideNode.dataSize -= 1;
                            sideNode.leavesNum -= 1;
                            break;
                        }
                    }

                    // Descer o elemento à direita no pai e substitui-lo pelo elemento do filho.
                    // Shift everything forwards
                    for (int i = this.dataSize; i > 0; i--) this.data[i] = this.data[i - 1];
                    for (int i = this.leavesNum; i > 0; i--) this.next[i] = this.next[i - 1];
                    this.data[0] = parentNode.data[positionInRegardsToTheParentNode - 1];
                    parentNode.data[positionInRegardsToTheParentNode - 1] = tempIndex;
                    this.next[0] = tempChildR;

                    this.dataSize++;
                    this.leavesNum++;
                }

                // Caso 1.2.2 -> Se o vizinho da direita poder emprestar um, rodamos a árvore com ele.
                else if((positionInRegardsToTheParentNode) <= ordem && 
                    parentNode.next[positionInRegardsToTheParentNode + 1] != null &&
                    parentNode.next[positionInRegardsToTheParentNode + 1].dataSize >= ordem/2){
                    sideNode = parentNode.next[positionInRegardsToTheParentNode + 1];
                    // Pegar o elemento mais da esquerda.
                    Index tempIndex = sideNode.data[0];
                    BNode tempChildL = sideNode.next[0];
                    for (int i = 0; i < ordem - 1; i++) {
                        if(sideNode.data[i + 1] != null) sideNode.data[i] = sideNode.data[i+1];
                        else sideNode.data[i] = null;
                        if(sideNode.next[i + 1] != null) sideNode.next[i] = sideNode.next[i+1];
                        else sideNode.next[i] = null;
                    }
                    sideNode.dataSize--;
                    sideNode.leavesNum--;
                    
                    // Descer o elemento à esquerda no pai e substitui-lo pelo elemento do filho.
                    int i = 0;
                    for (int j = 0; this.data[j] != null && j < ordem; j++) i = j + 1;
                    this.data[i] = parentNode.data[positionInRegardsToTheParentNode];
                    parentNode.data[positionInRegardsToTheParentNode] = tempIndex;
                    i = 0;
                    for (int j = 0; this.next[j] != null && j < ordem; j++) i = j + 1;
                    this.next[i] = tempChildL;

                    this.dataSize++;
                    this.leavesNum++;

                }
            
                // Caso 1.2.3 -> Nenhum dos vizinhos podem emprestar uma migalha de índice.
                else{

                    // Nesse caso, descemos um elemento do topo para uma das folhas.
                    Index parentElementToLowerDown = null;
                    int positionOfTakenElement = 0;
                    if (positionInRegardsToTheParentNode > 1) {
                        positionOfTakenElement = positionInRegardsToTheParentNode - 1;
                    }
                    parentElementToLowerDown = parentNode.data[positionOfTakenElement];
                    
                    // Sempre fazemos o troca-troca com o elemento à esquerda, se existir
                    int positionOfElementToMergeWith = positionInRegardsToTheParentNode - 1;
                    if (positionInRegardsToTheParentNode == 0) positionOfElementToMergeWith = 1;
                    sideNode = parentNode.next[positionOfElementToMergeWith];

                    // Fazer merge nos dois nodes, mais o node de cima.
                    // Da esquerda ou da direita?
                    
                    if(positionOfElementToMergeWith < positionInRegardsToTheParentNode)
                    { // Esquerda
                      // Shift everything forwards N times + 1 for the parent
                        for (int s = 0; s <= sideNode.dataSize; s++) {
                            for (int i = this.ordem; i > 0; i--) this.data[i] = this.data[i - 1];
                            for (int i = this.ordem + 1; i > 0; i--) this.next[i] = this.next[i - 1];
                        }
                      // Add sidenode + next ptrs
                        int s = 0;
                        for (; s <= sideNode.dataSize; s++) {
                            if(sideNode.data[s] != null){
                                this.data[s] = sideNode.data[s];
                                this.dataSize++;
                            }
                            if(sideNode.next[s] != null){
                                this.next[s] = sideNode.next[s];
                                this.leavesNum++;
                            }
                        }
                        this.data[s - 1] = parentElementToLowerDown;
                        parentNode.next[positionOfElementToMergeWith] = null;
                        parentNode.next[positionInRegardsToTheParentNode] = this;
                        parentNode.leavesNum--;

                    }
                    else
                    { // Direita
                      // Transfere o elemento do pai para baixo:
                        this.data[dataSize++] = parentElementToLowerDown;
                      // Transfere os elementos do sidenode:
                        for (int s = dataSize, i = 0; s < ordem - 1; i++, s++) {
                            this.data[s] = sideNode.data[i];
                            if(this.data[s] != null) this.dataSize++;
                            this.next[s] = sideNode.next[i];
                            if(this.next[s] != null) this.leavesNum++;
                        }
                        parentNode.next[positionOfElementToMergeWith] = null;
                        parentNode.next[positionInRegardsToTheParentNode] = this;
                        parentNode.leavesNum--;
                    }

                    parentNode.data[positionOfTakenElement] = null;
                    parentNode.dataSize--;
                    // Re-ordenar Next e Data
                    for (int i = 0; i < ordem - 1; i++) {
                        if(parentNode.data[i] == null) 
                        {
                            parentNode.data[i] = parentNode.data[i+1];
                            parentNode.data[i+1] = null;
                        }
                    }

                    for (int i = 0; i < ordem; i++) {
                        if(parentNode.next[i] == null) 
                        {
                            parentNode.next[i] = parentNode.next[i+1];
                            parentNode.next[i+1] = null;
                        }
                    }

                    // Garantir que o pai ainda tem o número minimo de registros
                    if(parentNode.dataSize < ordem/2 - 1) parentNode.merge();
                }

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
        int i = 0;

        for (Index index : data) {
            if(index == null) break;
            if(index.id == key)
            {
                returnNode = this;
                return returnNode;
            }
        }

        while(i < this.leavesNum)
        {
            if(this.data[i] == null)
            {
                if(this.next[i] != null) returnNode = this.next[i].huntNode(key);
                break;
            } 
            else if (key < this.data[i].id) 
            {returnNode = this.next[i].huntNode(key); break;}
            else i += 1;
        }
        return returnNode;
    }
}