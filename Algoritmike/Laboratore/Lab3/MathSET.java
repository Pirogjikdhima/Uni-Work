package Laboratore.Lab3;

import java.util.HashSet;

public class MathSET<Key> {
    public Key[] universeArray;
    public HashSet<Key> universe;
    public HashSet<Key> set;

    public MathSET(Key[] universe) {
        this.universeArray = universe;
        this.universe = new HashSet<>();
        for (Key key : universe) {
            this.universe.add(key);
        }
        this.set = new HashSet<>();
    }

    public void add(Key key) {
        if (!universe.contains(key)) {
            throw new IllegalArgumentException("Celesi " + key + " nuk eshte pjese e universit");
        } else {
            set.add(key);
        }
    }

    public MathSET<Key> complement() {
        MathSET<Key> complement = new MathSET<>(this.universeArray);
        for (Key key : universe) {
            if (!set.contains(key)) {
                complement.add(key);
            }
        }
        return complement;

    }

    void union(MathSET<Key> a) {
        if (!universe.equals(a.universe)) {
            for (Key key : a.universe) {
                universe.add(key);
            }
        }
        for (Key key : a.set) {
            set.add(key);
        }
    }

    void intersection(MathSET<Key> a) {
        for (Key key : set) {
            if (!a.set.contains(key)) {
                set.remove(key);
            }
        }
    }

    void delete(Key key) {
        if (!universe.contains(key)) {
            throw new IllegalArgumentException("Celesi " + key + " nuk eshte pjese e universit");
        }
        if (set.contains(key)) {
            set.remove(key);
        } else {
            throw new IllegalArgumentException("Celesi " + key + " nuk eshte pjese e Setit");
        }

    }

    boolean contains(Key key) {
        return set.contains(key);
    }

    boolean isEmpty() {
        return set.isEmpty();
    }

    int size() {
        return set.size();
    }

}
