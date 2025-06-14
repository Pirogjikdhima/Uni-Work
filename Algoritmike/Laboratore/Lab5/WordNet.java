package Laboratore.Lab5;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordNet {
    private final Map<String, Set<Integer>> nounToSynsets;
    private final Map<Integer, String> synsetIdToSynset;
    private final ShortestCommonAncestor sca;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException("Arguments cannot be null");

        nounToSynsets = new HashMap<>();
        synsetIdToSynset = new HashMap<>();
        In synsetsIn = new In(synsets);
        In hypernymsIn = new In(hypernyms);

        while (synsetsIn.hasNextLine()) {
            String[] fields = synsetsIn.readLine().split(",");
            int id = Integer.parseInt(fields[0]);
            String synset = fields[1];
            synsetIdToSynset.put(id, synset);
            for (String noun : synset.split(" ")) {
                nounToSynsets.computeIfAbsent(noun, k -> new HashSet<>()).add(id);
            }
        }

        Digraph digraph = new Digraph(synsetIdToSynset.size());
        while (hypernymsIn.hasNextLine()) {
            String[] fields = hypernymsIn.readLine().split(",");
            int synsetId = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                digraph.addEdge(synsetId, Integer.parseInt(fields[i]));
            }
        }

        sca = new ShortestCommonAncestor(digraph);
    }

    public Iterable<String> nouns() {
        return nounToSynsets.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("Argument cannot be null");
        return nounToSynsets.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException("Arguments cannot be null");
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("Nouns must be in WordNet");
        return sca.lengthSubset(nounToSynsets.get(nounA), nounToSynsets.get(nounB));
    }

    public String sca(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException("Arguments cannot be null");
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("Nouns must be in WordNet");
        int ancestorId = sca.ancestorSubset(nounToSynsets.get(nounA), nounToSynsets.get(nounB));
        return synsetIdToSynset.get(ancestorId);
    }
}