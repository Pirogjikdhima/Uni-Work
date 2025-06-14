package Seminare.Sem8;

import java.util.ArrayList;
import java.util.List;

public class Degrees {
    private int[] indegrees;
    private int[] outdegrees;
    private List<Integer> sources;
    private List<Integer> sinks;
    private boolean isMap;

    Degrees(Digraph digraph) {
        indegrees = new int[digraph.V()];
        outdegrees = new int[digraph.V()];
        sources = new ArrayList<>();
        sinks = new ArrayList<>();
        isMap = true;

        for (int vertex = 0; vertex < digraph.V(); vertex++) {
            for (int neighbor : digraph.adj(vertex)) {
                indegrees[neighbor]++;
                outdegrees[vertex]++;
            }
        }

        for (int vertex = 0; vertex < digraph.V(); vertex++) {
            if (indegrees[vertex] == 0) {
                sources.add(vertex);
            }

            if (outdegrees[vertex] == 0) {
                sinks.add(vertex);
            }

            if (outdegrees[vertex] != 1) {
                isMap = false;
            }
        }
    }

    public int indegree(int vertex) {
        return indegrees[vertex];
    }

    public int outdegree(int vertex) {
        return outdegrees[vertex];
    }

    public Iterable<Integer> sources() {
        return sources;
    }

    public Iterable<Integer> sinks() {
        return sinks;
    }

    public boolean isMap() {
        return isMap;
    }

}