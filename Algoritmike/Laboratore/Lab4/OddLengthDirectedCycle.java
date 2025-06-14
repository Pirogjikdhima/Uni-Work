package Laboratore.Lab4;

import edu.princeton.cs.algs4.Digraph;

public class OddLengthDirectedCycle {
    private final boolean[] marked;
    private final int[] vertex;
    private boolean hasOddCycle;

    public OddLengthDirectedCycle(Digraph G) {
        marked = new boolean[G.V()];
        vertex = new int[G.V()];
        hasOddCycle = false;

        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v, 0);
            }
        }
    }

    private void dfs(Digraph G, int v, int c) {
        marked[v] = true;
        vertex[v] = c;

        for (int w : G.adj(v)) {
            if (hasOddCycle) return;

            if (!marked[w]) {
                dfs(G, w, 1 - c);
            } else if (vertex[w] == vertex[v]) {
                hasOddCycle = true;
                return;
            }
        }
    }

    public boolean hasOddCycle() {
        return hasOddCycle;
    }
}