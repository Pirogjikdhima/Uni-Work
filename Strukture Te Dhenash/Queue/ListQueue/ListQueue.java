package Queue.ListQueue;

public class ListQueue<AnyType> {
    private ListNode<AnyType> front;
    private ListNode<AnyType> back;

    public ListQueue() {
        front = back = null;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public void enqueue(AnyType x) {
        if (isEmpty()) {
            back = front = new ListNode<AnyType>(x);
        } else back = back.next = new ListNode<AnyType>(x);
    }

    public AnyType dequeue() {
        if (isEmpty()) {
            System.out.println("Bosh");
        }
        AnyType returnvalue = front.element;
        front = front.next;
        return returnvalue;
    }

    public AnyType getFront() {
        if (isEmpty()) {
            System.out.println("Bosh");
        }
        return front.element;
    }

    public void makeEmpty() {
        front = back = null;
    }

    public void printQueue() {
        ListNode<AnyType> aktual = front;
        while (aktual != back.next) {
            System.out.println(aktual.element + " ");
            aktual = aktual.next;
        }
        System.out.println();
    }

    public int size() {
        int size = 0;
        ListQueue<AnyType> temp = new ListQueue<>();
        while (!isEmpty()) {
            temp.enqueue(dequeue());
            size++;
        }
        while (!temp.isEmpty()) enqueue(temp.dequeue());

        return size;
    }

    public void reverseQueue() {
        ListQueue<AnyType> tempQueue = new ListQueue<>();
        int size = size();

        for (int i = 0; i < size; i++) {
            for (int j = i; j < size - 1; j++) {
                enqueue(dequeue());
            }
            tempQueue.enqueue(dequeue());
        }
        while (!tempQueue.isEmpty()) {
            this.enqueue(tempQueue.dequeue());
        }

    }

    public static void main(String[] args) {
        ListQueue<Integer> list = new ListQueue<>();
        for (int i = 0; i <= 9; i++)
            list.enqueue(i);
        list.printQueue();
        list.reverseQueue();
        list.printQueue();

    }

}

class ListNode<AnyType> {
    AnyType element;
    ListNode<AnyType> next;

    public ListNode(AnyType el) {
        this(el, null);
    }

    public ListNode(AnyType el, ListNode<AnyType> n) {
        element = el;
        next = n;
    }
}