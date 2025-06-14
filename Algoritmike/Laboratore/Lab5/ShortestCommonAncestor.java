package Laboratore.Lab5;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

import java.util.List;

public class ShortestCommonAncestor {
    private final Digraph digraph;

    public ShortestCommonAncestor(Digraph G) {
        if (G == null) throw new IllegalArgumentException("Graph cannot be null");
        this.digraph = new Digraph(G);
    }

    public int length(int v, int w) {
        return lengthSubset(List.of(v), List.of(w));
    }

    public int ancestor(int v, int w) {
        return ancestorSubset(List.of(v), List.of(w));
    }

    public int lengthSubset(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException("Arguments cannot be null");
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
        int minLength = Integer.MAX_VALUE;

        for (int i = 0; i < digraph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int length = bfsV.distTo(i) + bfsW.distTo(i);
                if (length < minLength) {
                    minLength = length;
                }
            }
        }
        return minLength == Integer.MAX_VALUE ? -1 : minLength;
    }

    public int ancestorSubset(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException("Arguments cannot be null");
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
        int minLength = Integer.MAX_VALUE;
        int ancestor = -1;

        for (int i = 0; i < digraph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int length = bfsV.distTo(i) + bfsW.distTo(i);
                if (length < minLength) {
                    minLength = length;
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }
}