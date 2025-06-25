package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    SimpleEntry<K, V>[] entries;
    int size;
    int currCap;

    // You may add extra fields or helper methods though!

    /**
     * Constructs a new ArrayMap with default initial capacity.
     */
    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
        size =0;
    }

    /**
     * Constructs a new ArrayMap with the given initial capacity (i.e., the initial
     * size of the internal array).
     *
     * @param initialCapacity the initial capacity of the ArrayMap. Must be > 0.
     */
    public ArrayMap(int initialCapacity) {
        this.entries = this.createArrayOfEntries(initialCapacity);
        currCap = initialCapacity;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code Entry<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     */
    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
        arrays and generics interact.
        */
        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    @Override
    public V get(Object key) {
        for (SimpleEntry<K, V> entry : entries) {
            if (entry != null && Objects.equals(entry.getKey(), (key))) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        SimpleEntry<K, V> entry = new SimpleEntry<K, V>(key, value);
        int counter = -1;
        V returnVal = null;
        if (size == currCap) {
            ArrayMap<K, V> newMap = new ArrayMap<>(currCap * 2);
            currCap = currCap*2;
            for (SimpleEntry<K, V> entrees : entries) {
                counter++;
                newMap.entries[counter] = entrees;
                if (newMap.entries[counter] != null && newMap.entries[counter].getKey().equals(key)) {
                    returnVal = newMap.entries[counter].getValue();
                    newMap.entries[counter].setValue(value);
                }
            }
            newMap.entries[currCap/2] = entry;
            size = size+1;
            this.entries = newMap.entries;
            return returnVal;
        } else {
            for (SimpleEntry<K, V> entrees: entries) {
                counter++;
                if (entrees != null && Objects.equals(entrees.getKey(), key)) {
                    returnVal = entrees.getValue();
                    this.entries[counter] = new SimpleEntry<>(key, value);
                    return returnVal;
                }
            }
        }
        entries[size] = entry;
        size = size + 1;
        return null;
    }

    @Override
    public V remove(Object key) {
        int curr = -1;
        for (SimpleEntry<K, V> entrees : entries) {
            curr++;
            if (entrees != null && Objects.equals(entrees.getKey(), key)) {
                V returnVal = entrees.getValue();
                this.entries[curr] = this.entries[size-1];
                this.entries[size-1] = null;
                size= size-1;
                return returnVal;
            }
        }
            return null;
        }

    @Override
    public void clear() {
        for (int i = 0; i < size(); i++) {
            entries[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (SimpleEntry<K, V> entrees : entries) {
            if (entrees != null && Objects.equals(entrees.getKey(), key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: You may or may not need to change this method, depending on whether you
        // add any parameters to the ArrayMapIterator constructor.
        return new ArrayMapIterator<>(this.entries);
    }

    private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final SimpleEntry<K, V>[] entries;
        // You may add more fields and constructor parameters
        private K next  = null;
        private V val = null;
        private int curr;
        public ArrayMapIterator(SimpleEntry<K, V>[] entries) {
            this.entries = entries;
            curr = 0;
        }

        @Override
        public boolean hasNext() {
            if (curr != entries.length) {
                return entries[curr] != null;
            } else {
                return false;
            }
        }

        @Override
        public Map.Entry<K, V> next() {
            if (hasNext()) {
                SimpleEntry<K, V> entry = new SimpleEntry<>(entries[curr].getKey(), entries[curr].getValue());
                curr++;
                return entry;
        }
            throw new NoSuchElementException();
    }
}
}
