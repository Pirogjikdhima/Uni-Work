package Stack.Arraystack;


public class Test {
    public static void main(String[] args) {
        ArrayStack<Integer> array = new ArrayStack<>();
        array.push(1);
        array.push(8);
        array.push(6);
        array.push(0);
        array.push(2);
        array.push(4);
        array.push(5);
        array.push(7);
        array.push(7);
        array.printStack();
        array.pushMiddle(99);
        array.printStack();
        array.popMiddle();
        array.printStack();
        System.out.println(array.findMiddle());
    }

}
