package DoublyLinkedList;

import LinkedList.ListNode;

public class DoublyLinkedList<AnyType extends Comparable<AnyType>> {
    private DoublyListNode<AnyType> header;
    private DoublyListNode<AnyType> trailer;

    public DoublyLinkedList() {
        header = new DoublyListNode<AnyType>(null);
        trailer = new DoublyListNode<AnyType>(null);
        header.next = trailer;
        trailer.prev = header;
    }

    public void printList() {
        if (header.next == trailer) System.out.print("Empty list");
        else {
            DoublyListNode<AnyType> itr = header.next;
            while (itr != trailer) {
                System.out.print(itr.element + " ");
                itr = itr.next;
            }
        }
        System.out.println();
    }

    public void shtoNeFund(AnyType n) {
        DoublyListNode<AnyType> new_node = new DoublyListNode<AnyType>(n, trailer, null);
        trailer.prev.next = new_node;
        new_node.prev = trailer.prev;
        trailer.prev = new_node;
    }

    public void ndrrovend(DoublyListNode<AnyType> current) {

        DoublyListNode<AnyType> previous = current.prev;
        DoublyListNode<AnyType> nextcurrent = current.next;
        DoublyListNode<AnyType> afternext = current.next.next;

        if (afternext == null) {
            System.out.println("Nuk mund te nderrohet");
            return;
        }
        current.next = afternext;
        current.prev = nextcurrent;
        nextcurrent.next = current;
        nextcurrent.prev = previous;
        previous.next = nextcurrent;
        afternext.prev = current;
    }

    public void shtopaprishurrenditjen(AnyType K) {

        DoublyListNode<AnyType> previous = header;
        DoublyListNode<AnyType> current = header.next;

        while ((current.next != trailer) && (current.element.compareTo(K) < 0)) {
            previous = current;
            current = current.next;
        }
        if (current.next != trailer) previous.next = current.prev = new DoublyListNode<AnyType>(K, current, previous);
        else current.next = trailer.prev = new DoublyListNode<AnyType>(K, trailer, current);
    }

}


