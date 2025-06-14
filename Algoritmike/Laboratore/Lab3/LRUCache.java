package Laboratore.Lab3;

public class LRUCache<Item> {

    private DoublyLinkedList<Item> doublyLinkedList;
    private SeparateChainingHashTable<Item, DoublyLinkedList<Item>.DoubleNode> table;
    private int capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.doublyLinkedList = new DoublyLinkedList<>();
        this.table = new SeparateChainingHashTable<>();
    }

    public int size() {
        return doublyLinkedList.size();
    }

    public void access(Item item) {

        if (table.contains(item)) {
            DoublyLinkedList<Item>.DoubleNode node = table.get(item);
            doublyLinkedList.removeItemWithNode(node);
            table.put(item, doublyLinkedList.insertAtTheBeginningAndReturnNode(item));
        } else {
            if (size() >= capacity) {
                Item removed = remove();
                System.out.println("Removed: " + removed);
            }
            DoublyLinkedList<Item>.DoubleNode newNode = doublyLinkedList.insertAtTheBeginningAndReturnNode(item);
            table.put(item, newNode);
        }
    }

    public Item remove() {
        if (doublyLinkedList.isEmpty()) {
            return null;
        }
        Item leastRecentlyUsedItem = doublyLinkedList.removeFromTheEnd();
        table.delete(leastRecentlyUsedItem);

        return leastRecentlyUsedItem;
    }
}