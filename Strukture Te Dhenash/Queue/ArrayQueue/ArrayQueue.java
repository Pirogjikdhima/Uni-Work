package Queue.ArrayQueue;

public class ArrayQueue<AnyType> {
    private AnyType[] theArray;
    private int currentSize;
    private int front;
    private int back;

    private static final int DEFAULT_CAPACITY = 10;

    public ArrayQueue() {
        theArray = (AnyType[]) new Object[DEFAULT_CAPACITY];
        makeEmpty();
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public void makeEmpty() {
        currentSize = 0;
        front = 0;
        back = -1;
    }

    public AnyType dequeue() {
        if (isEmpty()) {
            System.out.println("ArrayQueue bosh");
            return null;
        }
        currentSize--;

        AnyType returnValue = theArray[front];
        front = increment(front);
        return returnValue;
    }

    public AnyType getFront() {
        if (isEmpty()) {
            System.out.println("ArrayQueue bosh");
            return null;
        }
        return theArray[front];
    }

    public void enqueue(AnyType x) {
        if (currentSize == theArray.length) doubleQueue();

        back = increment(back);
        theArray[back] = x;
        currentSize++;
    }

    private int increment(int x) {
        if (++x == theArray.length) x = 0;
        return x;
    }

    private void doubleQueue() {
        AnyType[] newArray;

        newArray = (AnyType[]) new Object[theArray.length * 2];

        for (int i = 0; i < currentSize; i++, front = increment(front))
            newArray[i] = theArray[front];

        theArray = newArray;
        front = 0;
        back = currentSize - 1;
    }


}