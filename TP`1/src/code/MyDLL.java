package src.code;

public class MyDLL {

    protected Node head;
    protected Node tail;
    private int amount;

    public MyDLL()
    {
        head = null;
        tail = head;
        amount = 0;
    }
    public MyDLL(Model a)
    {
        head = new Node(a);
        tail = head;
        amount = 1;
    }

    public int getSize() {
        return amount;
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

    
    // Adiciona ao inicio
    public void addToDLL (Model a)
    {
        Node temp =  new Node(a);
        if(head == null)
        {
            head = temp;
            tail = head;
        }
        else
        {
            temp.setNP(head, null);
            head.setPrev(temp);
            head = temp;
        }
        this.amount++;
    }

    // Adiciona ao final
    public void addToDLLEnd (Model a)
    {
        Node temp =  new Node(a);
        if(head == null)
        {
            head = temp;
            tail = head;
        }
        if(tail == null)
        {
            tail = head;
            while(tail.getNext() != null)
            {
                tail = tail.getNext();
            }
        }
        if(tail != null)
        {
            temp.setNP(null, tail);
            tail.setNext(temp);
            tail = temp;
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

    // METHOD CRASHES AT TAIL OR HEAD. DO NOT USE THIS.
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

    // METHOD CRASHES AT TAIL OR HEAD. DO NOT USE THIS.
    public Model popDLL (int id)
    {
        Node target = seekInDLL(id);
        if(target == null) return null;

        // Mend the hole in the fabric of reality
        target.getNext().setPrev(target.getPrev());
        target.getPrev().setNext(target.getNext());

        this.amount--;
        return target.getData();
    }

    public Model popDLLStart ()
    {
        if(head != null)
        {
            Node target = head;
            if(head == tail)
            {
                head = null; tail = null;
                this.amount--;
                return target.getData();
            }
            head.getNext().setPrev(null);
            head = head.getNext();

            this.amount--;
            return target.getData();
        }
        else return null;
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


    // Merge two DLLs together. The tail of THIS points to the head of the last, and vice versa.

    public void mergeDLLs (MyDLL otherDLL)
    {
        if(this.head != null)
        {
            this.tail.setNext(otherDLL.head);
            if(otherDLL.head != null)
            {
                otherDLL.head.setPrev(this.tail);
                otherDLL.head = null;
            }
            if(otherDLL.tail != null) this.tail = otherDLL.tail;

            this.amount += otherDLL.amount;
        }
        else if(otherDLL.head != null)
        {
            this.head = otherDLL.head;
            this.tail = otherDLL.tail;
            this.amount = otherDLL.amount;
        }
        else; //Both DLLs are null. Nothing happens.
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