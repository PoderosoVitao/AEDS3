package src.code;

// Nodes que guardam pares Int-Chars em vez de Models
// Usados exclusivamente para metodos de compressão.
public class NodeIntChar {
    
    private char charac;
    private int count;
    private NodeIntChar prev;
    private NodeIntChar left;
    private NodeIntChar right;

    public NodeIntChar ()
    {
        charac = '\0';
        count = 0;
        left = null;
        right = null;
        prev = null;
    }

    public NodeIntChar (char a, int b)
    {
        charac = a;
        count = b;
        right = null;
        prev = null;
    }

    public void setCharac(char charac) {
        this.charac = charac;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public void setLeft(NodeIntChar left) {
        this.left = left;
    }
    public void setRight(NodeIntChar right) {
        this.right = right;
    }
    public void setPrev(NodeIntChar prev) {
        this.prev = prev;
    }

    public char getCharac() {
        return charac;
    }
    public int getCount() {
        return count;
    }
    public NodeIntChar getLeft() {
        return left;
    }
    public NodeIntChar getPrev() {
        return prev;
    }
    public NodeIntChar getRight() {
        return right;
    }

    public static final void quicksort(NodeIntChar[] array)
    {
        quicksort(array, 0, (array.length - 1));
    }

    // Sorteia um array de NodeIntChars
    public static final void quicksort(NodeIntChar[] array, int low, int high)
    {
        if (low < high) {
            int pi = partition(array, low, high);
            quicksort(array, low, pi - 1);
            quicksort(array, pi + 1, high);
        }
    }

    public static final int partition (NodeIntChar[] array, int low, int high)
    {
        int pivot = array[high].getCount();
        int i = (low - 1);

        for (int j = low; j <= high - 1; j++) {
            if (array[j].getCount() < pivot) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return (i + 1);
    }

    public static final void swap(NodeIntChar[] array, int a, int b)
    {
        NodeIntChar temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

}
