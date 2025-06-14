package Stack.Arraystack;

public class Queue_2Stack<AnyType> {
    ArrayStack<AnyType> front;
    ArrayStack<AnyType> back;

    public Queue_2Stack() {
        front = new ArrayStack<>();
        back = new ArrayStack<>();
    }

    public void enqueue(AnyType x) {
        back.push(x);
    }

    public void dequeue() {
        if (front.isEmpty()) {
            if (back.isEmpty()) {
                System.out.println("Queue eshte bosh");
                return;
            }
            while (!back.isEmpty()) front.push(back.topAndPop());

        }
        front.pop();
    }

    public AnyType getFront() {
        if (front.isEmpty()) {
            if (back.isEmpty()) {
                System.out.println("Queue eshte bosh");
                return null;
            }
            while (!back.isEmpty()) front.push(back.topAndPop());
        }
        return front.top();
    }

    public void makeEmpty() {
        front.makeEmpty();
        back.makeEmpty();
    }
}
