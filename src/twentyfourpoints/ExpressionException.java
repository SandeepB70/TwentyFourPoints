package twentyfourpoints;

/**
 * A custom exception made if there is an error when converting the infix expression to postfix within the Expression class.
 * @author Sandeep Bindra
 * @version 2.0
 */
public class ExpressionException extends RuntimeException {
    public ExpressionException(String error) {
        super(error);
    }
}