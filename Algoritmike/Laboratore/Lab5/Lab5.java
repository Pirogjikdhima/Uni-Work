package Laboratore.Lab5;


public class Lab5 {
    public static void main(String[] args) {
        String synsetsFile = "src/Laboratore/Lab5/synsets.txt";
        String hypernymsFile = "src/Laboratore/Lab5/hypernyms.txt";

        try {
            System.out.println("Testing WordNet implementation...\n");

            // Test WordNet construction
            System.out.println("1. Testing WordNet construction...");
            WordNet wordnet = new WordNet(synsetsFile, hypernymsFile);
            System.out.println("✓ WordNet successfully constructed\n");

            // Test isNoun() method
            System.out.println("2. Testing isNoun() method...");
            String[] testNouns = {"AND_gate", "logic_gate", "worm", "computer", "nonexistent_word"};
            for (String noun : testNouns) {
                System.out.printf("Is '%s' a noun? %b%n", noun, wordnet.isNoun(noun));
            }
            System.out.println();

            // Test distance() method
            System.out.println("3. Testing distance() method...");
            String[][] distancePairs = {
                    {"AND_gate", "logic_gate"},
                    {"worm", "bird"},
                    {"computer", "internet"},
                    {"dog", "cat"}
            };
            for (String[] pair : distancePairs) {
                if (wordnet.isNoun(pair[0]) && wordnet.isNoun(pair[1])) {
                    int distance = wordnet.distance(pair[0], pair[1]);
                    System.out.printf("Distance between '%s' and '%s': %d%n",
                            pair[0], pair[1], distance);
                }
            }
            System.out.println();

            // Test sca() method
            System.out.println("4. Testing sca() method...");
            for (String[] pair : distancePairs) {
                if (wordnet.isNoun(pair[0]) && wordnet.isNoun(pair[1])) {
                    String ancestor = wordnet.sca(pair[0], pair[1]);
                    System.out.printf("Shortest common ancestor of '%s' and '%s': %s%n",
                            pair[0], pair[1], ancestor);
                }
            }
            System.out.println();

            // Test Outcast class
            System.out.println("5. Testing Outcast class...");
            Outcast outcast = new Outcast(wordnet);

            // Test multiple outcast scenarios
            String[][] outcastTests = {
                    {"AND_gate", "logic_gate", "computer", "calculator"},
                    {"dog", "cat", "bird", "house"},
                    {"water", "fire", "earth", "wind", "heart"},
            };

            for (String[] nouns : outcastTests) {
                // First verify all nouns exist in the WordNet
                boolean allNounsValid = true;
                for (String noun : nouns) {
                    if (!wordnet.isNoun(noun)) {
                        System.out.printf("Warning: '%s' not found in WordNet%n", noun);
                        allNounsValid = false;
                        break;
                    }
                }

                if (allNounsValid) {
                    String outcastNoun = outcast.outcast(nouns);
                    System.out.print("Outcast among [");
                    for (int i = 0; i < nouns.length; i++) {
                        System.out.print(nouns[i] + (i < nouns.length - 1 ? ", " : ""));
                    }
                    System.out.println("] is: " + outcastNoun);
                }
            }

            // Test error handling
            System.out.println("\n6. Testing error handling...");
            try {
                wordnet.distance(null, "computer");
                System.out.println("❌ Should throw IllegalArgumentException for null input");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Correctly handled null input in distance()");
            }

            try {
                wordnet.sca("nonexistent", "computer");
                System.out.println("❌ Should throw IllegalArgumentException for non-existent noun");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Correctly handled non-existent noun in sca()");
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}