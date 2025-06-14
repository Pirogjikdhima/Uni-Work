package LinkedList;

public class LinkedListIterator<AnyType extends Comparable<AnyType>> {
    public ListNode<AnyType> current; //percakton pozicionin aktual

    public LinkedListIterator() {
        current = null;
    }

    public LinkedListIterator(ListNode<AnyType> node) {
        current = node;
    }

    public boolean isValid() {
        return current != null;
    }

    public AnyType retrieve() {
        if (isValid()) {
            return current.element;
        } else {
            return null;
        }
    }

    public void advance() {
        if (isValid()) {
            current = current.next;
        }
    }
}
