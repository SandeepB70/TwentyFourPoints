package twentyfourpoints;

/**
 * An interface for the custom made generic stack.
 * @author Sandeep Bindra
 */
public interface GenericStackInterface<E> {

    /**
     * @return The number of objects in this stack.
     */
    public int getSize();

    /**
     * @return Reference to the top element of the stack without removing it.
     */
    public E peek();

    /**
     * Pushes an element onto the top of the stack.
     * @param obj
     */
    public void push(E obj);

    /**
     * Removes and returns the element at the top of the stack.
     * @return The element at the top of the stack.
     */
    public E pop();

    /**
     * @return True if the stack is empty, otherwise false.
     */
    public boolean isEmpty();
}

