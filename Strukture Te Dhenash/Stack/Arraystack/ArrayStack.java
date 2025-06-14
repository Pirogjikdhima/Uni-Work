package Stack.Arraystack;


public class ArrayStack<AnyType> {
    private AnyType[] theArray;
    private int topOfStack;
    private static final int DEFAULT_CAPACITY = 10;

    public ArrayStack() {
        theArray = (AnyType[]) new Comparable[DEFAULT_CAPACITY];
        topOfStack = -1;
    }

    public boolean isEmpty() {
        return topOfStack == -1;
    }

    public void makeEmpty() {
        topOfStack = -1;
    }

    public void push(AnyType x) {
        if (topOfStack + 1 == theArray.length) {
            doubleArray();
        }
        theArray[++topOfStack] = x;
    }

    public AnyType top() {
        if (isEmpty()) {
            System.out.println("Stack bosh");
        }
        return theArray[topOfStack];
    }

    public void pop() {
        if (isEmpty()) {
            System.out.println("Stack bosh");
            return;
        }
        topOfStack--;
    }

    public AnyType topAndPop() {
        if (isEmpty()) {
            System.out.println("Stack bosh");
            return null;
        }
        return theArray[topOfStack--];
    }

    private void doubleArray() {
        AnyType[] newArray;
        newArray = (AnyType[]) new Comparable[theArray.length * 2];
        for (int i = 0; i < theArray.length; i++) {
            newArray[i] = theArray[i];
        }
        theArray = newArray;
    }

    public int gjatesia() {
        return topOfStack + 1;
    }

    public void printStack() {
        for (int i = topOfStack; i >= 0; i--) {
            System.out.print(theArray[i] + " ");
        }
        System.out.println();
    }

    public static ArrayStack<Integer> sortStack(ArrayStack<Integer> s) {
        ArrayStack<Integer> sortedStack = new ArrayStack<>();
        while (!s.isEmpty()) {
            int temp = s.topAndPop();
            while (!sortedStack.isEmpty() && sortedStack.top() > temp) {
                s.push(sortedStack.topAndPop());
            }
            sortedStack.push(temp);
        }
        return sortedStack;
    }

    public void deleteMiddle() {

        int size = this.gjatesia();

        ArrayStack<AnyType> temp = new ArrayStack<>();

        int i;

        if (size % 2 == 1) i = size - 1;
        else i = size;

        for (; i > size / 2; i--)
            temp.push(this.topAndPop());

        this.pop();
        while (!temp.isEmpty()) {
            this.push(temp.topAndPop());
        }

    }

    public void Copy(ArrayStack<AnyType> s2) {
        int size = s2.gjatesia();
        for (int i = s2.topOfStack; i >= 0; i--) {
            this.push(s2.theArray[size - 1 - i]);
        }
    }

    public static ArrayStack<Integer> getMaxBinaryStack(ArrayStack<Integer> s1, ArrayStack<Integer> s2) {
        int num1 = binaryToDecimal(s1);
        int num2 = binaryToDecimal(s2);
        return num1 >= num2 ? s1 : s2;
    }

    public static int binaryToDecimal(ArrayStack<Integer> stack) {
        int decimal = 0;
        int power = 0;
        ArrayStack<Integer> tempStack = new ArrayStack<>();
        tempStack.Copy(stack);

        while (!tempStack.isEmpty()) {
            int digit = tempStack.topAndPop();
            decimal += digit * Math.pow(2, power);
            power++;
        }
        return decimal;
    }

    public void PutOnTopKthElemet(int k) {
        if (k >= gjatesia()) {
            System.out.println("Elementi me indeks: " + k + " nuk ndodhet ne stive.Vendosni nje ideks tjeter.");
            return;
        }
        AnyType temp = theArray[k];
        for (int i = k; i != topOfStack; i++) {
            theArray[i] = theArray[i + 1];
        }
        topOfStack--;
        push(temp);
    }

    public static void DecimalToHex(int decimal) {
        int decimalNumber = decimal;
        ArrayStack<Integer> binaryStack = new ArrayStack<>();


        while (decimal > 0) {
            int remainder = decimal % 16;
            binaryStack.push(remainder);
            decimal /= 16;
        }

        ArrayStack<Character> hexStack = new ArrayStack<>();


        while (!binaryStack.isEmpty()) {
            hexStack.push(convertToHex(binaryStack.topAndPop()));
        }

        System.out.print("Numri " + decimalNumber + " në bazën 16 është: ");
        ArrayStack<Character> hexStack2 = new ArrayStack<>();
        while (!hexStack.isEmpty()) hexStack2.push(hexStack.topAndPop());
        hexStack2.printStack();
    }

    public static char convertToHex(int binaryDigit) {
        // Convert the binary digit to a hexadecimal character
        if (binaryDigit < 10) {
            return (char) (binaryDigit + '0');
        } else {
            return (char) (binaryDigit - 10 + 'A');
        }
    }

    public void popMiddle() {

        int size = this.gjatesia();

        ArrayStack<AnyType> temp = new ArrayStack<>();

        for (int i = size - 1; i > size / 2; i--)
            temp.push(this.topAndPop());

        this.pop();
        while (!temp.isEmpty()) {
            this.push(temp.topAndPop());
        }

    }

    public void pushMiddle(AnyType x) {

        int size = this.gjatesia();

        ArrayStack<AnyType> temp = new ArrayStack<>();

        for (int i = size - 1; i > size / 2; i--)
            temp.push(this.topAndPop());
        if (size % 2 == 0) {
            temp.push(this.topAndPop());
        }
        this.push(x);

        while (!temp.isEmpty()) {
            this.push(temp.topAndPop());
        }
    }

    public AnyType topAndPopMiddle() {
        int size = this.gjatesia();

        ArrayStack<AnyType> temp = new ArrayStack<>();

        for (int i = size - 1; i > size / 2; i--)
            temp.push(this.topAndPop());

        AnyType value = this.topAndPop();

        while (!temp.isEmpty()) {
            this.push(temp.topAndPop());
        }
        return value;
    }

    public AnyType findMiddle() {
        int size = this.gjatesia();

        ArrayStack<AnyType> temp = new ArrayStack<>();

        for (int i = size - 1; i > size / 2; i--)
            temp.push(this.topAndPop());

        AnyType value = this.top();

        while (!temp.isEmpty()) {
            this.push(temp.topAndPop());
        }
        return value;
    }

    public static void main(String[] args) {
        ArrayStack<Integer> array = new ArrayStack<>();
        for (int i = 0; i < 10; i++) {
            array.push(i);
        }
        System.out.println("Printojme mesin: " + array.findMiddle());

        System.out.println("Heqim mesin");
        array.popMiddle();
        array.printStack();


        System.out.println("Shtojme mesin:66");
        array.pushMiddle(66);
        array.printStack();

        System.out.println("Heqim mesin dhe kthejme vleren: " + array.topAndPopMiddle());
        array.printStack();

    }
}
