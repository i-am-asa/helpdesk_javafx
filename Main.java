import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {
    private TextField displayField;

    @Override
    public void start(Stage primaryStage) {
        try {
            GridPane gridPane = new GridPane();
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            displayField = new TextField();
            displayField.setEditable(false);
            displayField.setAlignment(Pos.CENTER_RIGHT);
            gridPane.add(displayField, 0, 0, 4, 1);

            // Create buttons for digits, operations, and equals
            createButtons(gridPane);

            // Load CSS file
            Scene scene = new Scene(gridPane, 300, 400);

            // Adjust the path to the CSS file relative to the classpath
            String cssPath = "calculator_styles.css";
            URL cssUrl = getClass().getResource(cssPath);
            scene.getStylesheets().add(cssUrl.toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle("JavaFX Calculator");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createButtons(GridPane gridPane) {
        // Create clear button
        Button clearButton = new Button("C");
        clearButton.getStyleClass().add("clear-button");
        clearButton.setOnAction(e -> clearDisplay());
        gridPane.add(clearButton, 0, 1); // Place clearButton in row 0, column 0

        // Create buttons for arithmetic operations (+, -, =)
        String[] operations = {"+", "-", "="};
        for (int i = 0; i < operations.length; i++) {
            final String operation = operations[i];
            Button operationButton = new Button(operation);
            operationButton.getStyleClass().add("operation-button");
            if (!operation.equals("=")) {
                operationButton.setOnAction(e -> handleOperationButtonClick(operation));
            } else {
                operationButton.setOnAction(e -> evaluateExpression());
            }
            gridPane.add(operationButton, i + 1, 1); // Place operationButton in row 0, column i+1
        }

        // Create digits buttons (1-9, 0)
        int digit = 1;
        for (int row = 2; row <= 5; row++) {
            for (int col = 0; col < 3; col++) {
                Button digitButton;
                if (digit <= 9) {
                    digitButton = new Button(Integer.toString(digit));
                } else if (digit == 10) {
                    digitButton = new Button("0");
                } else {
                    break;
                }
                digitButton.getStyleClass().add("digit-button");
                digitButton.setOnAction(e -> handleDigitButtonClick(digitButton.getText()));
                gridPane.add(digitButton, col, row); // Place digitButton in row, column
                digit++;
            }
        }

        // Create decimal button
        Button decimalButton = new Button(".");
        decimalButton.getStyleClass().add("decimal-button");
        decimalButton.setOnAction(e -> handleDecimalButtonClick());
        gridPane.add(decimalButton, 2, 5); // Place decimalButton in row 4, column 2

        // Create parentheses button
        Button parenthesesButton = new Button("(");
        parenthesesButton.getStyleClass().add("parentheses-button");
        parenthesesButton.setOnAction(e -> handleParenthesesButtonClick("("));
        gridPane.add(parenthesesButton, 3, 4); // Place parenthesesButton in row 3, column 3

        // Create closing parentheses button
        Button closingParenthesesButton = new Button(")");
        closingParenthesesButton.getStyleClass().add("parentheses-button");
        closingParenthesesButton.setOnAction(e -> handleParenthesesButtonClick(")"));
        gridPane.add(closingParenthesesButton, 3, 5); // Place closingParenthesesButton in row 4, column 3

        // Create buttons for arithmetic operations (*, /)
        String[] otherOperations = {"*", "/"};
        for (int i = 0; i < otherOperations.length; i++) {
            final String operation = otherOperations[i];
            Button operationButton = new Button(operation);
            operationButton.getStyleClass().add("operation-button");
            operationButton.setOnAction(e -> handleOperationButtonClick(operation));
            gridPane.add(operationButton, 3, i + 2); // Place operationButton in row i+1, column 3
        }
        Button sqrtButton = new Button("√");
        sqrtButton.getStyleClass().add("operation-button");
        sqrtButton.setOnAction(e -> handleSquareRootButtonClick());
        gridPane.add(sqrtButton, 1, 5); // Place sqrtButton in row 2, column 4
    }

    // Define the handler method for the square root button
    private void handleSquareRootButtonClick() {
        String expression = displayField.getText();
        // Append the square root symbol and an opening parenthesis to the expression
        expression += "√(";
        displayField.setText(expression);
    }

    private void handleDigitButtonClick(String digit) {
        displayField.appendText(digit);
    }

    private void handleOperationButtonClick(String operation) {
        if (operation.equals("√")) {
            handleSquareRootButtonClick();
        } else {
            displayField.appendText(" " + operation + " ");
        }
    }

    private void handleDecimalButtonClick() {
        displayField.appendText(".");
    }

    private void handleParenthesesButtonClick(String parentheses) {
        displayField.appendText(parentheses);
    }

    private void evaluateExpression() {
        String expression = displayField.getText();
        double result = Calculation.evaluate(expression);
        displayField.setText(Double.toString(result));
    }

    private void clearDisplay() {
        displayField.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
