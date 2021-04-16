package twentyfourpoints;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Represents a algebraic expression in infix and is capable of
 * converting it to postfix and evaluating the expression.
 * @author Sandeep Bindra
 * @version 2.0
 */
public class Expression {

    private String infix; //Stores the algebraic expression as a string

    /**
     * Constructor to create an algebraic expression based on the String passed
     * @param expression The algebraic expression
     */
    public Expression(String expression) {
        this.infix = expression;
    }

    /**
     * Used to convert the infix expression to a postfix expression. Numbers are separated by ';'
     * so it is clear if they are single or multi-digit numbers
     * @return The postfix version of the infix expression.
     */
    public ArrayList<String> infixToPostfix() throws ExpressionException {

        ArrayList<String> postFix = new ArrayList<String>(); //Represents the postfix expression

        GenericStack<Character> stack = new GenericStack<Character>(); //The stack used to assemble the postfix expression.

        char[] expression = this.infix.toCharArray(); //Obtain each individual char of 'infix'

        int index = 0; //Current position within the 'expression' array

        /**
         * Go through each char of 'infix' and
         * follow the infix to postfix algorithm to obtain the postfix expression.
         */
        while (index < expression.length) {
            char token = expression[index];
            if (Character.isDigit(token)) {
                postFix.add(Character.toString(token));
                //Check if the token at the next position within the expression (if it exists) is NOT a number,
                //in which case this is a single digit number.
                if (index + 1 < expression.length) {
                    if (!Character.isDigit(expression[index + 1])) {
                        postFix.add(";");
                    }
                } else {
                    postFix.add(";");
                }
            } else if (token == '(') { //This is an open parenthesis so it just gets pushed onto the stack
                stack.push(token);
            } else if (token == '+' || token == '-' || token == '*' || token == '/') {
                if (stack.isEmpty()) {
                    stack.push(token);
                } else {
                        //Pop tokens of equal or greater precedence than this operator
                        //and add them to the postFix expression.
                        LinkedList<Character> popped = checkStack(token, stack);
                        if (popped.size() > 0) {
                            while(!popped.isEmpty()) {
                                postFix.add(Character.toString(popped.remove()));
                            }
                        }
                }
            } else if (token == ')') {
                while(!stack.isEmpty()) {
                    //Keep popping off the stack until we reached a matching '('
                    if(stack.peek() == '(') {
                        stack.pop(); //No longer need the '(' since we reached it's matching ')' and we stop here.
                        break;
                    } else {
                        postFix.add(Character.toString(stack.pop()));
                    }
                }
            }
            ++index;
        }//End while loop

        //Append remaining content of stack to postFix expression
        while(!stack.isEmpty()) {
            postFix.add(Character.toString(stack.pop()));
        }
        return postFix;
    }//End infixToPostfix

    /**
     * Helper method used when an operator has been found in the infix expression. Operator of
     * equal or greater precedence will continue to be popped until one of lesser precedence has been found,
     * the stack is emptied, or a '(' is found.
     * @param operator The operator found in the postFix expression.
     * @param stack The stack used for storing operators and parenthesis.
     * @return All the operators popped off the stack in the form of a LinkedList representing a queue.
     */
    private LinkedList<Character> checkStack(char operator, GenericStack<Character> stack) throws ExpressionException {

        //Any values popped off the stack will be stored in this queue.
        LinkedList<Character> poppedQ = new LinkedList<>();
        //Variable used to check the next char on the stack if the stack is not empty.
        //char 'a' is just used to initialize it.
        char next = 'a';

        if (operator == '+' || operator == '-') {
            while(!stack.isEmpty()) {
                next = stack.peek();
                if (next == '*' || next == '/' || next == '+' || next == '-') {
                    poppedQ.add(stack.pop()); //Operators of equal or higher precedence should be popped
                } else if (next == '(') {
                    stack.push(operator);
                    return poppedQ;
                }
            }
            //If the stack is empty, push the operator on and continue.
            stack.push(operator);
            return poppedQ;
        } else if (operator == '*' || operator == '/') {
            while(!stack.isEmpty()) {
                next = stack.peek();
                if (next == '*' || next == '/') {
                    poppedQ.add(stack.pop());
                } else if (next == '+' || next == '-' || next == '(') { //Stop at a '+', '-', or '('
                    stack.push(operator);
                    return poppedQ;
                }
            }
            //If the stack is empty, push the operator on and continue.
            stack.push(operator);
            return poppedQ; //There are no operators of greater or equal precedence left or we have run into a '('
        }
        throw new ExpressionException("Unknown operator found");
    } //End getPrecedence()

    /**
     * Returns the result of the infix expression by first converting it to a postFix expression
     * and then evaluating it.
     * @return The result of the algebraic expression.
     */
    public int evaluate() throws NumberFormatException, StackException {
        int result = 0; //Stores the result that the algebraic expression evaluates to.

        if (this.infix == null) {
            System.out.println("Error: infix expression is null");
            return 0;
        }
        ArrayList<String> postFix = this.infixToPostfix(); //Obtain the postFix expression

        // Needed to represent the number we are currently getting from the postFix expression
        StringBuilder number = new StringBuilder();
        GenericStack<Integer> stack = new GenericStack<>();

        //Go through the postfix expression and obtain the result.
        for (String token : postFix) {
            if (Character.isDigit(token.charAt(0))) {
                number.append(token);
            } else if (token.equals(";")) { //A semicolon means the end of the previous number so we push it onto the stack
                stack.push(Integer.parseInt(number.toString()));
                number.delete(0, number.capacity()+1); //Clear out 'number' for any remaining numbers.
            } else {
                int num2 = stack.pop();
                int num1 = stack.pop();
                switch (token.charAt(0)) {
                    case '+': {
                        stack.push(num1 + num2);
                        break;
                    }
                    case '-': {
                        stack.push(num1 - num2);
                        break;
                    }
                    case '*': {
                        stack.push(num1 * num2);
                        break;
                    }
                    case '/': {
                        stack.push(num1 / num2);
                        break;
                    }
                }
            }
        } //End for loop
        result = stack.pop();
        return result;
    }//End evaluate()
}