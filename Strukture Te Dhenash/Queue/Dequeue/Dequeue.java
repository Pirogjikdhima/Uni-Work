package Queue.Dequeue;

public class Dequeue<AnyType> {
    private DoubleListNode<AnyType> front;
    private DoubleListNode<AnyType> rear;

    public Dequeue() {
        front = rear = null;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public AnyType getFront() {
        if (isEmpty()) {
            System.out.println("Dequeue is empty");
            return null;
        }
        return front.element;
    }

    public AnyType getRear() {
        if (isEmpty()) {
            System.out.println("Dequeue is empty");
            return null;
        }
        return rear.element;
    }

    public void deleteFront() {
        if (isEmpty()) {
            System.out.println("Dequeue eshte bosh");
            return;
        }
        if (front == rear) {
            front = rear = null;
            return;
        }
        front = front.next;
        front.prev = null;
    }

    public void deleteRear() {
        if (isEmpty()) {
            System.out.println("Dequeue eshte bosh");
            return;
        }
        if (front == rear) {
            front = rear = null;
            return;
        }
        rear = rear.prev;
        rear.next = null;
    }

    public void insertFront(AnyType x) {
        if (isEmpty()) {
            front = rear = new DoubleListNode<>(x);
        } else {
            front = front.prev = new DoubleListNode<>(x, null, front);
        }
    }

    public void insertRear(AnyType x) {
        if (isEmpty()) {
            front = rear = new DoubleListNode<>(x);
        } else {
            rear = rear.next = new DoubleListNode<>(x, rear, null);
        }
    }

    public void printDequeue() {
        DoubleListNode<AnyType> current = front;
        while (current != rear.next) {
            System.out.print(current.element + " ");
            current = current.next;
        }
    }

    public static void main(String[] args) {
        Dequeue<Integer> deque = new Dequeue<>();
        deque.insertFront(3);
        deque.insertRear(4);
        deque.insertRear(7);
        deque.insertFront(10);
        deque.printDequeue();
        System.out.println("Front: " + deque.getFront() + " Rear: " + deque.getRear());
    }
}

class DoubleListNode<AnyType> {
    AnyType element;
    DoubleListNode<AnyType> prev;
    DoubleListNode<AnyType> next;

    public DoubleListNode(AnyType element) {
        this(element, null, null);
    }

    public DoubleListNode(AnyType element, DoubleListNode<AnyType> prev, DoubleListNode<AnyType> next) {
        this.element = element;
        this.prev = prev;
        this.next = next;
    }
}
