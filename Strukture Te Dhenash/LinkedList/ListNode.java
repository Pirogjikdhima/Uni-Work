package LinkedList;


public class ListNode<AnyType extends Comparable<AnyType>> {
    public AnyType element;
    public ListNode<AnyType> next;

    public ListNode(AnyType element) {
        this(element, null);
    }

    public ListNode(AnyType e, ListNode<AnyType> n) {
        this.element = e;
        this.next = n;
    }
}
