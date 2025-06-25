package deques;

/**
 * @see Deque
 */
public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    Node<T> front;
    Node<T> back;
    // Feel free to add any additional fields you may need, though.
    // Node<T> sentinalFront;
    // Node<T> sentinalBack;


    public LinkedDeque() {

        size = 0;
        front = new Node<>(null);
        front.prev = null;
        back = new Node<>(null);
        back.next = null;
        front.next = back;
        back.prev = front;

    }

    public void addFirst(T item) {
        size += 1;
        Node<T> newNode = new Node<>(item);
        newNode.next = front.next;
        newNode.next.prev = newNode;
        front.next = newNode;
        newNode.prev = front;
    }

    public void addLast(T item) {
        size += 1;
        Node<T> newNode = new Node<>(item);
        newNode.prev = back.prev;
        newNode.prev.next = newNode;
        back.prev = newNode;
        newNode.next = back;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T returnVal = front.next.value;
        front.next = front.next.next;
        front.next.prev = front;
        return returnVal;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T returnVal = back.prev.value;
        back.prev = back.prev.prev;
        back.prev.next = back;
        return returnVal;
    }

    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        Node<T> reference;
        if (index <= size/2) {
            int count = 0;
            reference = front.next;
            while (count != index) {
                reference = reference.next;
                count++;
            }
        } else {
            reference = back.prev;
            int count = size-1;
            while (count != index) {
                reference = reference.prev;
                count--;
            }
        }
        return reference.value;
    }

    public int size() {
        return size;
    }
}
