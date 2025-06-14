package Stack.ListStack;

public class ListStack<AnyType> {
    private ListNode<AnyType> topOfStack = null;

    public boolean isEmpty() {
        return topOfStack == null;
    }

    public void makeEmpty() {
        topOfStack = null;
    }

    public void push(AnyType x) {
        topOfStack = new ListNode<AnyType>(x, topOfStack);
    }

    public void pop() {
        if (isEmpty()) {
            System.out.println("ListStack pop");
            return;
        }
        topOfStack = topOfStack.next;
    }

    public AnyType top() {
        if (isEmpty()) {
            System.out.println("ListStack top");
            return null;
        }
        return topOfStack.element;
    }

    public AnyType topAndPop() {
        if (isEmpty()) {
            System.out.println("ListStack topAndPop");
            return null;
        }
        AnyType topItem = topOfStack.element;
        topOfStack = topOfStack.next;
        return topItem;
    }

    public void PutOnTopKthElemet(int k) {
        ListStack<AnyType> temp_list = new ListStack<>();
        int size = -1;
        while (!isEmpty()) {
            temp_list.push(topAndPop());
            size++;
        }

        AnyType top = null;

        for (int i = 0; i <= size; i++) {
            if (i == k) {
                top = temp_list.topAndPop();
                continue;
            }
            push(temp_list.topAndPop());
        }
        push(top);
    }

    public void printStack() {
        ListStack<AnyType> temp_list = new ListStack<>();
        while (!isEmpty()) {
            temp_list.push(topAndPop());
        }
        while (!temp_list.isEmpty()) {
            System.out.print(temp_list.top() + " ");
            push(temp_list.topAndPop());
        }
    }

    public static void main(String[] args) {
        ListStack<Integer> list = new ListStack<>();
        for (int i = 0; i < 9; i++) {
            list.push(i);
        }
        list.printStack();
        list.PutOnTopKthElemet(0);
        System.out.println();
        list.printStack();

    }

}

class ListNode<AnyType> {
    public AnyType element;
    public ListNode next;

    public ListNode(AnyType theElement) {
        this(theElement, null);
    }

    public ListNode(AnyType theElement, ListNode<AnyType> n) {
        element = theElement;
        next = n;
    }
}