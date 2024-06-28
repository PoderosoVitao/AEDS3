package src.code;

// Nossso dicionário é uma Função Hash dividida pela letra inicial.
// Temos 1000 buckets por posição inicial.
// Temos 65536 posições iniciais.
// Total = 65.536.000 buckets diferentes.
// O valor numérico de um caractére é sua própria posição.
// Isso ocupa bastante espaço, mas a vantagem é uma pesquisa extermamente rapida.

// Interface com o LZW
public class DictionaryHash {
    
    public DictionaryHeadNode head;
    int bucketAmount;
    int elemNum;

    public DictionaryHash()
    {
        head = new DictionaryHeadNode(this);
        elemNum = 0;
        bucketAmount = 1000;
    }

    public DictionaryHash(int amountOfBuckets)
    {
        head = new DictionaryHeadNode(this);
        elemNum = 0;
        bucketAmount = amountOfBuckets;
    }

    // Add all chars from inclusive (char) 0 to (char) 65535
    // Popular essa estrutura toda custa 300 MegaBytes~
    public void populate()
    {
        for (int i = 0; i < 65536; i++) {
            String a = "";
            a += (char) i;
            this.insert(a);
        }
    }

    // Insere uma String na árvore.
    public void insert(String a)
    {
        if(head.child == null) head.child = new DictionaryHashNode[65536];

        if(a.length() < 1) return;

        char toInsert = a.charAt(0);
        if(head.child[(int) toInsert] == null){
           head.child[(int) toInsert] = new DictionaryHashNode(a, elemNum, bucketAmount);
        }
        if(a.length() > 1)  head.child[(int) toInsert].insert(a, hashFunction(a));

        elemNum++;
    }

    // Procura uma String na árvore e retorna o código.
    public int search(String a)
    {
        if(a.length() < 1) return -1;
        if(head == null) return -1; 
        if(head.child == null) return -1;
        char toInsert = a.charAt(0);
        if(head.child[(int) toInsert] == null) return -1;
        if(a.length() == 1) return head.child[(int) toInsert].code;
        return head.child[(int) toInsert].search(a, hashFunction(a));
    }

    // Função hash: Soma todos os algarismos e tira o modulo do tamanho dos buckets.
    // Exemplo: A+a+B para 1000 buckets = (65 + 97 + 66) % 1000
    private int hashFunction(String a)
    {
        int aLength = a.length();
        int sum = 0;
        for (int i = 0; i < aLength; i++) sum += (int) a.charAt(i);
        return sum % bucketAmount;
    }
}

// Interface entre o DictionaryHash e os nós individuais. Permite a pesquisa em O(1) do primeiro caractere.
class DictionaryHeadNode
{
    static DictionaryHash dicionario;
    DictionaryHashNode[] child;

    public DictionaryHeadNode(DictionaryHash dicionario)
    {
        child = new DictionaryHashNode[65536];
        DictionaryHeadNode.dicionario = dicionario;
        DictionaryHashNode.dicionario = DictionaryHeadNode.dicionario;
    }
}

// Nós individuais. Permite a pesquisa em O(1) baseada em Hash. Interface entre os Buckets e o HeadNode.
class DictionaryHashNode
{
    static DictionaryHash dicionario;
    String str;
    int code;
    Bucket[] buckets;

    public DictionaryHashNode(String a, int b, int c)
    {
        str = a;
        code = b;
        buckets = new Bucket[c];
    }

    public void insert(String str, int function)
    {   
        if(buckets[function] == null) buckets[function] = new Bucket();
        buckets[function].insert(str, DictionaryHashNode.dicionario.elemNum);
    }

    public int search(String str, int function)
    {
        if(buckets[function] == null) return -1;
        return buckets[function].search(str);
    }
}

// Buckets individuais. Permite a pesquisa em O(N) sequencial e linear. Interface entre Nós e o Dicionario.
class Bucket
{
    DictionaryNode head;
    DictionaryNode tail;
    int BucketSize;

    public Bucket()
    {
        head = null;
        tail = head;
        BucketSize = 0;
    }
    public void insert(String a, int elemNum)
    {
        if(head == null){
            head = new DictionaryNode(a, elemNum);
            tail = head;
            BucketSize++;
        }
        else
        {
            tail.next = new DictionaryNode(a, elemNum);
            tail = tail.next;
            BucketSize++;
        } 
    }

    public int search(String a)
    {
        DictionaryNode buffer = head;
        while(buffer != null && !(buffer.str.equals(a))) buffer = buffer.next;
        if(buffer == null ){
            return -1;
        }
        return buffer.code;
    }
}

// Implementação de Lista Encadeada para os Buckets.
class DictionaryNode
{
    String str;
    int code;
    DictionaryNode next;

    public DictionaryNode()
    {
        str = "";
        code = -1;
        next = null;
    }

    public DictionaryNode(String a, int b)
    {
        str = a;
        code = b;
        next = null;
    }
}
