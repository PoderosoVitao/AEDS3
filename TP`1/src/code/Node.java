package src.code;

public class Node {

    private Model data;
    private Node next;
    private Node prev;

    public Node ()
    {
        data = null;
        next = null;
        prev = null;
    }

    public Node (Model data)
    {
        this.data = data;
        next = null;
        prev = null;
    }

    public void setNP (Node next, Node prev)
    {
        this.next = next;
        this.prev = prev;
    }

    public void setData(Model data) {
        this.data = data;
    }
    public void setNext(Node next) {
        this.next = next;
    }
    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Model getData() {
        return data;
    }
    public Node getNext() {
        return next;
    }
    public Node getPrev() {
        return prev;
    }
}