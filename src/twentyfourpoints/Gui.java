package twentyfourpoints;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

/**
 * Represents the GUI of the twenty four point game. The goal of the game is to create a
 * mathematical expression that evaluates to 24 using the values of the four cards given.
 * The expression can consist of addition, subtraction, multiplcation, and/or division.
 * Parentheses can also be used to change the order of operations
 * (ex. addition before multiplication, such as (1+2)*3).
 * The expression used should NOT contain any spaces, otherwise a warning will be issued.
 * If the user chooses, they can change the set of cards dealt by hitting the refresh button.
 * Numbered cards have the same value as their number, Jack is worth 11, Queen is worth 12,
 * King is worth 13, and Ace is worth 1.
 * Please note that debug statements were left in but commented out and tagged with "Q_DEBUG".
 * Enjoy!
 * @author Sandeep Bindra
 * @version 2.0
 */

public class Gui extends Application {
    private ArrayList<Integer> deck;
    //Used to display the four cards the user will be working with.
    private ImageView cardView1;
    private ImageView cardView2;
    private ImageView cardView3;
    private ImageView cardView4;
    //Used to take in the user's calculation based on the four cards
    private TextField calculation;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Responsible for displaying the GUI and calling the appropriate handlers for when
     * the user interacts with different components.
     * @param primaryStage Will hold all the containers/buttons and allows them to be displayed
     */
    public void start(Stage primaryStage) {

        deck = new ArrayList<>(52);
        calculation = new TextField("Enter calculation here");
        //If the user clicks on the TextField input box, clear it if "Enter calculation here" is still present.
        calculation.setOnMouseClicked(mouseEvent -> {
            if(calculation.getText().equals("Enter calculation here")) {
                calculation.clear();
            }
        });

        /**
         * Load up all the cards and then shuffle them
         */
        for (int i = 0; i < 52; ++i) {
            deck.add(i, (i+1));
        }
        Collections.shuffle(deck);

        /**
         * Button used to get a new set of four cards.
         */
        Button refresh = new Button("Refresh Cards");
        refresh.setOnAction(new RefreshClickHandler());

        /**
         * Button used to calculate the user's total and check if it equal to 24.
         * Displays an alert if the user has used numbers not corresponding
         * to the values of the four cards displayed.
         */
        Button calcButton = new Button("Calculate");
        calcButton.setOnAction(new CalculationHandler());
        HBox calculateBox = new HBox(10, calculation, calcButton);
        calculateBox.setAlignment(Pos.CENTER);

        //Represents each of the four cards to be displayed.
        Image card1 = new Image("file:cards/" + deck.get(0) + ".png");
        Image card2 = new Image("file:cards/" + deck.get(1) + ".png");
        Image card3 = new Image("file:cards/" + deck.get(2) + ".png");
        Image card4 = new Image("file:cards/" + deck.get(3) + ".png");

        cardView1 = new ImageView(card1);
        cardView2 = new ImageView(card2);
        cardView3 = new ImageView(card3);
        cardView4 = new ImageView(card4);

        HBox cardsBox = new HBox(10, cardView1, cardView2, cardView3, cardView4);
        cardsBox.setPadding(new Insets(10));
        cardsBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(refresh, cardsBox, calculateBox);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox);

        //Display the GUI
        primaryStage.setTitle("24 Point Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }//End start()

    /**
     * Used to select a new set of four cards whenever the "New Cards" button is clicked.
     */
    private class RefreshClickHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent handle) {

            Collections.shuffle(deck);

            Image card1 = new Image("file:cards/" + deck.get(0) + ".png");
            Image card2 = new Image("file:cards/" + deck.get(1) + ".png");
            Image card3 = new Image("file:cards/" + deck.get(2) + ".png");
            Image card4 = new Image("file:cards/" + deck.get(3) + ".png");

            cardView1.setImage(card1);
            cardView2.setImage(card2);
            cardView3.setImage(card3);
            cardView4.setImage(card4);
        }
    }//End class RefreshClickHandler

    /**
     * Evaluates the mathematical expression the user entered.
     */
    private class CalculationHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent handle) {
            String expression = calculation.getText();
            expression.trim();

            char[] checkExpr = expression.toCharArray();
            //Ensure that only numbers and parentheses are entered.
            for (char variable : checkExpr) {
                if(!Character.isDigit(variable)) {
                    if ( !checkOperator(variable) ) {
                        String msg = ("Only parentheses, numbers, +, -, *, and / operators can be entered."
                                        + " Please make sure there are no spaces.");
                        displayAlert(msg, "Error", Alert.AlertType.ERROR);
                        return;
                    }
                }
            }//End for loop

            //Q_Check: Use the method if it works for all error Alert types
            //If the numbers used in the calculation do not match with the value of the cards displayed,
            //print an error.
            if(!checkNumbers(expression)) {
                String msg = ("Numbers do not match with values of cards shown");
                displayAlert(msg, "Error", Alert.AlertType.ERROR);
                return;
            }

            //Check if the expression used is equal to 24
            Expression check = new Expression(expression);
            try {
                if (check.evaluate() == 24) {
                    String msg = ("Correct! Your expression equals 24.");
                    displayAlert(msg, "Correct!", Alert.AlertType.INFORMATION);
                } else {
                    String msg = ("Sorry, your expression does not evaluate to 24.");
                    displayAlert(msg, "Incorrect", Alert.AlertType.INFORMATION);
                }
            }
            catch (NumberFormatException err) {
                String msg = "Error with processing of numbers.";
                displayAlert(msg, "Error", Alert.AlertType.ERROR);
            }
            catch (StackException err) {
                String msg = "Error with order of operators/operands";
                displayAlert(msg, "Error", Alert.AlertType.ERROR);
            }
            catch(ExpressionException err) {
                String msg = "Unknown operator found";
                displayAlert(msg, "Error", Alert.AlertType.ERROR);
            }
        }
    }//End class CalculationHandler

    /**
     * Used by the CalculationHandler to check if the user has only used the
     * four numbers that correspond with the value of the cards drawn.
     * @param expression The mathematical expression being checked
     * @return True if the numbers used in the calculation match, otherwise false.
     */
    private boolean checkNumbers(String expression) {
        //Represents the values of the four cards being displayed.
        ArrayList<Integer> validNumbers = new ArrayList<>();
        //Represents the four numbers the user put in their calculation
        ArrayList<Integer> calcNumbers = new ArrayList<>();

        for (int i = 0; i < 4; ++i) {
            int number = deck.get(i) % 13;
            //If this is a King, we need to set it's value to thirteen
            if (number == 0) {
                number = 13;
            }
            validNumbers.add(number);
        }
        //System.out.println(expression); //Q_DEBUG
        //Obtain the numbers used in the calculation by splitting the
        //expression on parentheses and any of the valid operators.
        String[] tokens = expression.split("[()\\-\\+\\*\\/]");

        if (tokens.length < 4) {
            String msg = ("Four numbers must be used");
            displayAlert(msg, "Invalid Expression", Alert.AlertType.ERROR);
            return false;
        }

        //Add each of the numbers obtained to calcNumbers
        for (String token: tokens) {
            //System.out.println(token); //Q_DEBUG
            //Skip any empty strings that may be left because of split()
            if ( token.isEmpty() ) {
                //System.out.println("Skipped"); //Q_DEBUG
                continue;
            }
            //System.out.println("Adding " + token); Q_DEBUG
            calcNumbers.add(Integer.parseInt(token));
        }

        //Sort both lists and ensure the user has only used the values of the cards
        //being displayed.
        Collections.sort(validNumbers);
        Collections.sort(calcNumbers);

        //System.out.println("Valid: " + validNumbers); //Q_DEBUG
        //System.out.println("calculated: " + calcNumbers); //Q_DEBUG
        return validNumbers.equals(calcNumbers);

    }

    /**
     * Displays a custom error message using an Alert
     * @param msg The message to be displayed within the alert
     * @param alertType An enum from Alert.AlertType to determine what type of alert will show up
     * @param title The title of the alert window displayed
     */
    private void displayAlert(String msg, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(msg);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            alert.close();
        }
    }

    /**
     * Checks if the character being passed is one of the valid operators or a parenthesis
     * @param operator The character to be checked.
     * @return true if the character is a valid operator or parenthesis, otherwise false.
     */
    private boolean checkOperator(char operator) {
        switch (operator) {
            case '+':
                return true;
            case '-':
                return true;
            case '*':
                return true;
            case '/':
                return true;
            case '(':
                return true;
            case ')':
                return true;
            default:
                return false;
        }
    }
}
