package Seminare.Sem10;

import java.util.HashSet;

public class MathSET<Key> {
    private final HashSet<Key> set; // Internal set for managing keys
    private final HashSet<Key> universe;

    public MathSET(Key[] universe) {
        this.universe = new HashSet<>();
        for (Key key : universe) {
            this.universe.add(key);
        }
        this.set = new HashSet<>();
    }

    public void add(Key key) {
        if (!universe.contains(key)) {
            throw new IllegalArgumentException("Key " + key + " does not belong to the universe");
        }
        set.add(key);
    }

    public MathSET<Key> complement() {
        MathSET<Key> complement = new MathSET<>((Key[]) this.universe.toArray());
        for (Key key : universe) {
            if (!set.contains(key)) {
                complement.add(key);
            }
        }
        return complement;
    }

    public void union(MathSET<Key> mathSetToUnite) {
        for (Key key : mathSetToUnite.set) {
            add(key);
        }
    }

    public void intersection(MathSET<Key> mathSetToIntersect) {
        set.retainAll(mathSetToIntersect.set);
    }

    public void delete(Key key) {
        set.remove(key);
    }

    public boolean contains(Key key) {
        return set.contains(key);
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public Iterable<Key> keys() {
        return set;
    }
}