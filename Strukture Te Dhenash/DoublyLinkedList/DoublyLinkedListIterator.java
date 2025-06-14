package DoublyLinkedList;

public class DoublyLinkedListIterator<AnyType extends Comparable<?>> {
    DoublyListNode<AnyType> current;

    public DoublyLinkedListIterator() {
        current = null;
    }

    public DoublyLinkedListIterator(DoublyListNode<AnyType> node) {
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

    public void previous() {
        if (isValid()) {
            current = current.prev;
        }
    }
}