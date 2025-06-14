package Laboratore.Lab4;

import edu.princeton.cs.algs4.Digraph;

public class Lab4 {

    public static void main(String[] args) {

        Digraph G = new Digraph(4);
        G.addEdge(0, 1);
        G.addEdge(1, 2);
        G.addEdge(2, 0);
        G.addEdge(2, 3);

        OddLengthDirectedCycle finder = new OddLengthDirectedCycle(G);
        if (finder.hasOddCycle()) {
            System.out.println("Graph has an odd-length directed cycle.");
        } else {
            System.out.println("Graph does not have an odd-length directed cycle.");
        }
    }
}