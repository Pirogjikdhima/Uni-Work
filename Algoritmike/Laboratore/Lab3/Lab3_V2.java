package Laboratore.Lab3;

public class Lab3_V2 {

    public static void main(String[] args) {
        MathSET<Integer> mathSet = new MathSET<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        for (int i = 1; i < 7; i++) {
            mathSet.add(i);
        }
        mathSet.add(10);

        System.out.println("Universi: " + mathSet.universe);
        System.out.println("Seti: " + mathSet.set);
        System.out.println("Complementari: " + mathSet.complement().set);

        mathSet.delete(2);
        mathSet.delete(5);
        mathSet.delete(3);

        System.out.println("Pas Fshirjes");
        System.out.println("Universi: " + mathSet.universe);
        System.out.println("Seti: " + mathSet.set);
        System.out.println("Complementari: " + mathSet.complement().set);

        MathSET<Integer> anothermathSet = new MathSET<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        for (int i = 1; i < 3; i++) {
            anothermathSet.add(i);
        }
        mathSet.union(anothermathSet);
        System.out.println("Universi: " + anothermathSet.universe);
        System.out.println("Seti: " + anothermathSet.set);
        System.out.println("Union: " + mathSet.set);

    }
}
