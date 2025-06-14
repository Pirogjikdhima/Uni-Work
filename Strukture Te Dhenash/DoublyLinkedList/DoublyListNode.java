package DoublyLinkedList;

public class DoublyListNode<AnyType extends Comparable<?>> {
    public AnyType element;
    public DoublyListNode<AnyType> next;
    public DoublyListNode<AnyType> prev;

    public DoublyListNode(AnyType element) {
        this.element = element;
        this.next = null;
        this.prev = null;
    }

    public DoublyListNode(AnyType e, DoublyListNode<AnyType> n, DoublyListNode<AnyType> p) {
        this.element = e;
        this.next = n;
        this.prev = p;
    }
}