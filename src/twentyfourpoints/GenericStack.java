package twentyfourpoints;

import java.util.ArrayList;

/**
 * Implementation of a generic stack with the use of the ArrayList class to represent the actual stack.
 * @author Sandeep Bindra
 * @version 2.0
 */
public class GenericStack<E> implements GenericStackInterface<E> {

    private ArrayList<E> stack;

    /**
     * Create an empty stack.
     */
    public GenericStack() {
        this.stack = new ArrayList<E>();
    }

    /**
     * @return The number of items currently stored on the stack.
     */
    public int getSize() {
        return this.stack.size();
    }

    /**
     * Retrieves the element at the top of the stack without removing it.
     * @return The element at the top of the stack.
     * @exception IndexOutOfBoundsException Thrown when an invalid index is passed to the get() method.
     */
    public E peek() throws IndexOutOfBoundsException {
        return this.stack.get(this.stack.size() - 1);
    }

    /**
     * Pushes an element onto the stack.
     * @param element The element to be placed on the stack.
     */
    public void push(E element) {
        this.stack.add(element);
    }

    /**
     * Removes and returns the element at the top of the stack.
     * @return The element at the top of the stack.
     */
    public E pop() throws StackException {
        try {
            return this.stack.remove(this.stack.size() - 1);
        } catch (IndexOutOfBoundsException err) {
            throw new StackException("Empty Stack");
        }
    }

    /**
     * Tells us if the stack is empty or not.
     * @return True if the stack is empty, otherwise false.
     */
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }
}