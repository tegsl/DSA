
package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 0.50;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 16;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 12;

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    private static double lambda;
    private static int length;
    private static int lengthOfChains;

    private int size;
    AbstractIterableMap<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!

    /**
     * Constructs a new ChainedHashMap with default resizing load factor threshold,
     * default initial chain count, and default initial chain capacity.
     */
    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    /**
     * Constructs a new ChainedHashMap with the given parameters.
     *
     * @param resizingLoadFactorThreshold the load factor threshold for resizing. When the load factor
     *                                    exceeds this value, the hash table resizes. Must be > 0.
     * @param initialChainCount the initial number of chains for your hash table. Must be > 0.
     * @param chainInitialCapacity the initial capacity of each ArrayMap chain created by the map.
     *                             Must be > 0.
     */
    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        lambda = resizingLoadFactorThreshold;
        length = initialChainCount;
        chains = createArrayOfChains(length);
        lengthOfChains = chainInitialCapacity;
        size = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    @Override
    public V get(Object key) {
        if (key == null && chains[0] != null) {
            for (Entry<K, V> entries : chains[0]) {
                if (Objects.equals(entries.getKey(), null)) {
                    return entries.getValue();
                }
            }
            return null;
        }
        if (chains[Math.abs(key.hashCode())%length] != null) {
            for (Entry<K, V> entries : chains[Math.abs(key.hashCode())%length]) {
                if (Objects.equals(entries.getKey(), key)) {
                    return entries.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if ((size) >= (lambda * length)) {
            ChainedHashMap<K, V> newAndResized = new ChainedHashMap<>(lambda, length * 2, lengthOfChains);
            for (AbstractIterableMap<K, V> entries : this.chains) {
                if (entries != null) {
                    for (Entry<K, V> entriesInEntries : entries) {
                        if (entriesInEntries.getKey() == null) {
                            if (newAndResized.chains[0] == null) {
                                newAndResized.chains[0] = createChain(lengthOfChains);
                            }
                            newAndResized.chains[0].put(entriesInEntries.getKey(), entriesInEntries.getValue());
                        } else if (newAndResized.chains[Math.abs(entriesInEntries.getKey().hashCode()) %
                            (length)] == null) {
                            newAndResized.chains[Math.abs(entriesInEntries.getKey().hashCode()) %
                                (length)] = createChain(lengthOfChains);
                        }
                        if (entriesInEntries.getKey() != null) {
                            newAndResized.chains[Math.abs(entriesInEntries.getKey().hashCode()) % (length)]
                                .put(entriesInEntries.getKey(), entriesInEntries.getValue());
                        }
                    }
                }
                this.chains = newAndResized.chains;
            }
        }
        if (Objects.equals(key, null)) {
            if (Objects.equals(chains[0], null)) {
                chains[0] = createChain(lengthOfChains);
                size++;
                return chains[0].put(null, value);
            }
            if (chains[0].containsKey(null)) {
                return chains[0].put(null, value);
            }
            size++;
            return chains[0].put(null, value);
        }  else if (Objects.equals(chains[Math.abs(key.hashCode()) % length], null)) {
            chains[Math.abs(key.hashCode()) % length] = createChain(lengthOfChains);
        }
        if (chains[Math.abs(key.hashCode()) % length].containsKey(key)) {
            return chains[Math.abs(key.hashCode()) % length].put(key, value);
        }
        size++;
        return chains[Math.abs(key.hashCode()) % length].put(key, value);
    }


    @Override
    public V remove(Object key) {
        if (Objects.equals(key, null)) {
            if (chains[0].containsKey(null)) {
                size--;
                return chains[0].remove(null);
            }
        } else if (!Objects.equals(chains[Math.abs(key.hashCode()) % length], null)) {
            if (chains[Math.abs(key.hashCode()) % length].containsKey(key)) {
                size--;
                return chains[Math.abs(key.hashCode()) % length].remove(key);
            }
        }
        return null;
    }

    @Override
    public void clear() {
        for (int i = 0; i < length; i++) {
            if (!Objects.equals(chains[i], null)) {
                chains[i].clear();
            }
        }
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            if (!Objects.equals(chains[0], null) && chains[0].containsKey(null)) {
                return true;
            }
            return false;
        }
        if (/*chains[Math.abs(key.hashCode())%length] != null */ !Objects.equals(chains
            [Math.abs(key.hashCode())%length], null) &&
            chains[Math.abs(key.hashCode())%length].containsKey(key))
        {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ChainedHashMapIterator<>(this.chains);
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private AbstractIterableMap<K, V>[] chains;
        // You may add more fields and constructor parameters
        private Iterator<Map.Entry<K, V>> currIterator;
        private int currIndex;

        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains) {
            this.chains = chains;
            currIndex = -1;
            currIterator = null;
        }
        // private int currChainReturn() {
        //     if (currIndex == length) {
        //         currIndex = 0;
        //     }
        //         for (int i = currIndex; i < chains.length; i++) {
        //             if (chains[i] != null) {
        //                 currIndex = i;
        //                 return i;
        //             }
        //         }
        //         for (int i = 0; i < currIndex; i++) {
        //             if (chains[i] != null) {
        //                 currIndex = i;
        //                 return i;
        //             }
        //         }
        //         return -1;
        // }
        public boolean hasNext() {
            if (Objects.equals(null, currIterator) || !(currIterator.hasNext())) {
                for (int i = currIndex+1; i < chains.length; i++) {
                    System.out.printf("" + i);
                    if (!Objects.equals(chains[i], null)) {
                        currIterator = chains[i].iterator();
                        currIndex = i;
                        if (currIterator.hasNext()) {
                            return true;
                        }
                    }
                }
            }
            if (currIterator != null && currIterator.hasNext()) {
                return true;
            }
            currIterator = null;
            return false;
        }


        @Override
        public Map.Entry<K, V> next() {
            if (hasNext() && currIterator.hasNext()) {
                return currIterator.next();
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
