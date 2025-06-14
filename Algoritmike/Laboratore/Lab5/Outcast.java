package Laboratore.Lab5;

public class Outcast {
    private final WordNet wordNet;

    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    public String outcast(String[] nouns) {
        if (nouns == null) throw new IllegalArgumentException("Argument cannot be null");
        int maxDistance = -1;
        String outcast = null;

        for (String nounA : nouns) {
            int distanceSum = 0;
            for (String nounB : nouns) {
                if (!nounA.equals(nounB)) {
                    distanceSum += wordNet.distance(nounA, nounB);
                }
            }
            if (distanceSum > maxDistance) {
                maxDistance = distanceSum;
                outcast = nounA;
            }
        }
        return outcast;
    }
}