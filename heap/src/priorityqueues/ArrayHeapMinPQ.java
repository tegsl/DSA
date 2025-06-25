package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;
    List<PriorityNode<T>> items;

    int currSize;

    HashMap<T, Integer> map;
    public ArrayHeapMinPQ() {
        map = new HashMap<>();
        currSize = 1;
        items = new ArrayList<>(1);
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> temp = items.get(a);
        PriorityNode<T> temp2 = items.get(b);
        items.set(a, temp2);
        items.set(b, temp);
    }

    @Override
    public void add(T item, double priority) {
        if (Objects.equals(item, null)) {
            throw new IllegalArgumentException();
        }
        if (!map.containsKey(item)) {
            PriorityNode<T> itemNamed = new PriorityNode<>(item, priority);
            if (map.size() == 0) {
                items.add(START_INDEX, itemNamed);
                map.put(item,  0);
            } else if (items.size() == currSize) {
                List<PriorityNode<T>> itemsNew = new ArrayList<>(currSize*2);
                currSize = currSize*2;
                itemsNew.addAll(items);
                if (contains(item)) {
                    throw new IllegalArgumentException();
                }
                itemsNew.add(itemNamed);
                items = itemsNew;
                map.put(item, size());
                int currIndex = items.size()-1;
                percolateUp(currIndex);
            }  else {
                items.add(itemNamed);
                map.put(item, size());
                int currIndex = items.size() - 1;
                percolateUp(currIndex);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (map.size() == 0) {
            throw new NoSuchElementException();
        }
        return items.get(START_INDEX).getItem();

    }

    @SuppressWarnings("checkstyle:WhitespaceAround")
    @Override
    public T removeMin() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        T removedVal = peekMin();
        if (map.size() == 1) {
            items.remove(0);
            map.remove(removedVal);
            return removedVal;
        } else if (map.size() == 2) {
            swap(0, 1);
            items.remove(1);
            map.remove(removedVal);
            return removedVal;
        }
        map.remove(removedVal);
        swap(0, map.size());
        map.put(items.get(0).getItem(), 0);
        items.remove(map.size());
        int currInd = 0;
        percolateDown(currInd);
        return removedVal;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (contains(item)) {
            int indexOfChange = map.get(item);
            PriorityNode<T> old = new PriorityNode<>(item, priority);
            items.set(indexOfChange, old);
            percolateUp(indexOfChange);
            percolateDown(indexOfChange);
        }
        else {
            throw new NoSuchElementException();
        }
    }

    private void percolateUp(int currIndex) {
        while (items.get(currIndex).getPriority() < items.get((currIndex-1)/2).getPriority()) {
            swap(currIndex, (currIndex-1)/2);
            map.put(items.get((currIndex-1)/2).getItem(), (currIndex-1)/2);
            map.put(items.get(currIndex).getItem(), (currIndex));
            currIndex = (currIndex-1)/2;
        }
    }

    private void percolateDown(int currInd) {
        while ((currInd*2+1 < (size()) && (items.get(currInd).getPriority() > items.get(currInd*2+1).getPriority()))
            || ((currInd*2+2 < (size())) && (items.get(currInd).getPriority() > items.get(currInd*2+2)
            .getPriority()))) {
            if ((currInd*2+2 < (size()))) {
                double minVal;
                minVal = Math.min(items.get(currInd*2+1).getPriority(), items.get(currInd*2+2)
                    .getPriority());
                if ((items.get(currInd*2+1).getPriority()) == minVal) {
                    swap(currInd, currInd*2+1);
                    map.put(items.get(currInd*2+1).getItem(), currInd*2+1);
                    map.put(items.get(currInd).getItem(), currInd);
                    currInd = currInd*2+1;
                } else {
                    swap(currInd, currInd*2+2);
                    map.put(items.get(currInd*2+2).getItem(), currInd*2+2);
                    map.put(items.get(currInd).getItem(), (currInd));
                    currInd = currInd*2+2;
                }
            } else {
                swap(currInd, currInd * 2 + 1);
                map.put(items.get(currInd*2+1).getItem(), currInd*2+1);
                map.put(items.get(currInd).getItem(), (currInd));
                currInd = currInd * 2 + 1;
            }
        }


    }
    @Override
    public int size() {
        return map.size();
    }
}
