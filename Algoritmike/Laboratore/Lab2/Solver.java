package Laboratore.Lab2;/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;

import java.util.LinkedList;

public class Solver {
    private final int moves;
    private final Iterable<Board> solutionPath;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board current;
        private final SearchNode prev;
        private final int moves;
        private final int priority;

        public SearchNode(Board current, SearchNode prev) {
            this.current = current;
            this.prev = prev;
            this.moves = (prev == null) ? 0 : prev.moves + 1;
            this.priority = this.moves + current.distance;
        }

        @Override
        public int compareTo(SearchNode other) {
            return Integer.compare(this.priority, other.priority);
        }
    }

    public Solver(Board initial) {
        if (initial == null || !initial.isSolvable()) {
            throw new IllegalArgumentException();
        }
        MinPQ<SearchNode> queue = new MinPQ<>();
        queue.insert(new SearchNode(initial, null));
        SearchNode goal = null;

        while (!queue.isEmpty()) {

            SearchNode node = queue.delMin();

            if (node.current.isGoal()) {
                goal = node;
                break;
            }

            for (Board neighbor : node.current.neighbors()) {
                if (node.prev != null && neighbor.equals(node.prev.current)) {
                    continue;
                }
                queue.insert(new SearchNode(neighbor, node));
            }

        }

        assert goal != null;// Nuk mund te jete null nese board.isSolvable()==true

        this.moves = goal.moves;

        LinkedList<Board> path = new LinkedList<>();

        while (goal != null) {
            path.addFirst(goal.current);
            goal = goal.prev;
        }

        this.solutionPath = path;
    }

    public int moves() {
        return this.moves;
    }

    public Iterable<Board> solution() {
        return solutionPath;
    }

    public static void main(String[] args) {

        In in = new In("puzzle50.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        Board initial = new Board(tiles);

        Solver solver = new Solver(initial);

        System.out.println("Number of moves: " + solver.moves());
        System.out.println("Solution path:");
        for (Board board : solver.solution()) {
            System.out.println(board);
        }

    }
}