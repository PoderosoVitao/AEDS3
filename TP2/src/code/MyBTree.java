package src.code;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

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

    // Inserir indice na árvore
    public void insert(Index indice) {
        this.head.insert(indice);
    }

    public int remove(long id)
    {
        return remove(id, head);
    }

    // Remover indice na árvore carregada em memória principal
    public int remove(long id, BNode target)
    {
        if(target == null) return 0;

        int pos = 0;
        for (Index index : target.data) {
            if(pos == target.dataSize) return remove(id, target.next[pos]); // No more indexes. Check the last 'next[]' bnode
            if(index == null || id < index.id) return remove(id, target.next[pos]); // It's, maybe, on the Bnode to the LEFT of this index.
            if(id == index.id) return target.remove(pos); // Found!
            pos++; // Check the next Index...
        }

        return remove(id, target.next[pos]); // It's, maybe, on the last B-tree. Unless it's null.
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


    // Export tree to a file.
    public void export(String filepath)
    {
        Arquivo header = new Arquivo(filepath);
        try {
            header.openEdit();
            header.dosOUT.writeInt(this.head.ordem);
            head.exportNode(header);

        } catch (Exception e) {
            MyIO.println("Erro metodo Export MyBTree! Nome foi digitado corretamente?");
            header.close();
        } finally {
            header.close();
        }
        header.close();
    }

    // Import from a file.
    public static MyBTree importTree(String filepath)
    {
        long byteCounter = 0;
        Arquivo header = new Arquivo(filepath);
        File file = new File(filepath);
        long arqLength = file.length();
        file = null;

        MyBTree returnTree = null;

        try {

            header.openEdit();
            int order = header.dosIN.readInt();
            returnTree = new MyBTree(order);
            byteCounter += 4;
            Index tempIndex = new Index(0, 0);

            while(byteCounter < arqLength)
            {
                tempIndex.id = header.dosIN.readLong();
                tempIndex.byteOffset = header.dosIN.readLong();
                byteCounter += 16; // 1 long + 1 long

                returnTree.insert(tempIndex);
            }
            returnTree.camninha();

        } catch (Exception e) {
            MyIO.println("Erro metodo Import MyBTree!");
        }
        header.close();
        return returnTree;
    }

    // Metodo de caminhamento. Imprime todos os IDs na árvore.
    public void camninha()
    {
        this.head.caminha();
    }

}


