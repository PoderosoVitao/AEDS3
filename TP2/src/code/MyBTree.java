package src.code;

public class MyBTree {

    public BNode head;
    public int height;

    public MyBTree(){}
    public MyBTree(int order)
    {
        this.height = 1;
        this.head = new BNode(order, this);
    }

    // Makes a new BTree out of a BNode
    public MyBTree(BNode node)
    {
        this.head = node;
        int i = 0;
        BNode tempnode  = head;
        while (tempnode != null)
        {
            i++;
            tempnode = tempnode.next[0];
        }
        this.height = i;
    }

    // Inserir indice na arovre.
    public int insert(Index index)
    {
        return insert(index, head, null);
    }

    // Inserir indice na árvore carregada em memória principal
    public int insert(Index index, BNode target, BNode parent)
    {
        if(target.leavesNum != 0) // We only insert on leaves.
        {
            int i = 0;
            while(target.data[i] != null && index.id > target.data[i].id)
            {
                i++;
            }
            return insert(index, target.next[i], target);
        }
        
        else return target.insert(index, parent);
    }

    // Busca recursiva de índice com base no ID
    public Index search(long id)
    {
        return search(id, this.head);
    }

    // Busca recursiva de índice com base no ID
    public Index search(long id, BNode thisNode)
    {
        if(thisNode == null) return null;

        int pos = 0;
        for (Index index : thisNode.data) {
            if(pos == thisNode.dataSize) return search(id, thisNode.next[pos]); // No more indexes. Check the last 'next[]' bnode
            if(index == null || id < index.id) return search(id, thisNode.next[pos]); // It's, maybe, on the Bnode to the LEFT of this index.
            if(id == index.id) return index; // Found!
            pos++; // Check the next Index...
        }

        return search(id, thisNode.next[pos]); // It's, maybe, on the last B-tree. Unless it's null.
    }
}


