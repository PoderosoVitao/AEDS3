package src.code;

public class MyDLL {

    private Node head;
    private int amount;

    public MyDLL()
    {
        head = null;
        amount = 0;
    }
    public MyDLL(Model a)
    {
        head = new Node(a);
        amount = 1;
    }

    // Finds a given node based off of its ID
    public Node seekInDLL(String id)
    {
        if(this.amount == 0) return null;
        return seekInDLL(id, head);
    }
    // Overload of seekInDLL
    public Node seekInDLL(String id, Node node)
    {
        if(node.getData().getVideo_id().equals(id)) return node;
        if(node.getNext() == null) return null;
        return seekInDLL(id, node.getNext());
    }
    // Finds a given node based off of its position
    public Node seekInDLL(int n)
    {
        if(this.amount == 0) return null;
        int i = 0;
        Node temp = head;
        while(i < n)
        {
            n++;
            temp = temp.getNext();
        }
        return temp;
    }

    
    // Adiciona sempre ao início. Por enquanto.
    public void addToDLL (Model a)
    {
        Node temp =  new Node(a);
        if(head == null) head = temp;
        else
        {
            temp.setNP(head, null);
            head.setPrev(temp);
            head = temp;
        }
        this.amount++;
    }

    public boolean updateDLL (String id, Model a)
    {
        Node target = seekInDLL(id);
        if(target == null) return false;
        target.setData(a);
        return true;
    }

    public Model popDLL (String id)
    {
        Node target = seekInDLL(id);
        if(target == null) return null;

        // Mend the hole in the fabric of reality
        target.getNext().setPrev(target.getPrev());
        target.getPrev().setNext(target.getNext());

        this.amount--;
        return target.getData();
    }

    public Model readID (String id)
    {
        Node target = seekInDLL(id);
        if(target == null) return null;
        return target.getData();
    }

    public void readIntervalInclusive (int a, int b)
    {
        int smaller = (a < b) ? a : b;
        int larger  = (a > b) ? a : b;
        Node temp = seekInDLL(smaller);
        while (smaller < larger && temp != null)
        {
            temp.getData().printToString();
            temp = temp.getNext();
            smaller++;
        }
    }

}

class Node {

    private Model data;
    private Node next;
    private Node prev;
    private Node up;
    private Node down;

    public Node ()
    {
        data = null;
        next = null;
        prev = null;
        up = null;
        down = null;
    }

    public Node (Model data)
    {
        this.data = data;
        next = null;
        prev = null;
        up = null;
        down = null;
    }

    public void setNP (Node next, Node prev)
    {
        this.next = next;
        this.prev = prev;
    }

    public void setData(Model data) {
        this.data = data;
    }
    public void setDown(Node down) {
        this.down = down;
    }
    public void setNext(Node next) {
        this.next = next;
    }
    public void setPrev(Node prev) {
        this.prev = prev;
    }
    public void setUp(Node up) {
        this.up = up;
    }

    public Model getData() {
        return data;
    }
    public Node getDown() {
        return down;
    }
    public Node getNext() {
        return next;
    }
    public Node getPrev() {
        return prev;
    }
    public Node getUp() {
        return up;
    }
}