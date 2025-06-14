package Laboratore.Lab3;

public class Lab3_V1 {

    public static void main(String[] args) {
        LRUCache<String> cache = new LRUCache<>(3);

        cache.access("A");
        cache.access("B");
        cache.access("C");

        System.out.println("Cache Size: " + cache.size());

        cache.access("A");

        cache.access("D");

        System.out.println("Cache Size after eviction: " + cache.size());

        cache.access("E");

        System.out.println("Cache Size after second eviction: " + cache.size());
    }
}