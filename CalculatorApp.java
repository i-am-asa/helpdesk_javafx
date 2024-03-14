import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CalculatorApp extends Application {

    private TextField display;
    private double firstOperand = 0;
    private char currentOperation = ' ';

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        display = new TextField();
        display.setEditable(false);

        GridPane grid = new GridPane();
        grid.setHgap(5); // Horizontal gap between buttons
        grid.setVgap(5); // Vertical gap between buttons
        grid.setPadding(new Insets(10, 10, 10, 10)); // Padding around the grid

        int row = 1;
        int col = 0;

        Button[] numberButtons = createNumberButtons();
        Button[] operationButtons = createOperationButtons();
        Button[] additionalButtons = createAdditionalButtons();

        // Adding buttons to the grid
        for (int i = 9; i >= 0; i--) {
            grid.add(numberButtons[i], col, row);
            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }

        for (int i = 4; i >= 0; i--) {
            grid.add(operationButtons[i], col, row);
            col++;
        }

        col = 0; // Resetting column count
        row++; // Moving to the next row for additional operation buttons

        for (int i = 0; i < 3; i++) {
            grid.add(additionalButtons[i], col, row);
            col++;
        }

        // Equals button
        Button equalsButton = createOperationButton("=");
        equalsButton.setOnAction(new OperationHandler('='));
        grid.add(equalsButton, col, row);

        // Clear button
        Button clearButton = createClearButton("C");
        clearButton.setOnAction(new ClearHandler());
        grid.add(clearButton, 2, row);

        // Display
        grid.add(display, 0, 0, 4, 1);

        // Set background color for buttons
        setButtonColors(numberButtons);
        setButtonColors(operationButtons);
        setButtonColors(additionalButtons);
        setButtonColors(new Button[]{equalsButton, clearButton});

        Scene scene = new Scene(grid, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Advanced Calculator");
        primaryStage.show();
    }

    private Button[] createNumberButtons() {
        Button[] numberButtons = new Button[10];
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = createNumberButton(Integer.toString(i));
        }
        return numberButtons;
    }

    private Button createNumberButton(String text) {
        Button button = new Button(text);
        button.setOnAction(new ButtonHandler());
        return button;
    }

    private Button[] createOperationButtons() {
        Button[] operationButtons = new Button[5];
        operationButtons[0] = createOperationButton("+");
        operationButtons[1] = createOperationButton("-");
        operationButtons[2] = createOperationButton("*");
        operationButtons[3] = createOperationButton("/");
        operationButtons[4] = createOperationButton("=");
        return operationButtons;
    }

    private Button createOperationButton(String text) {
        Button button = new Button(text);
        button.setOnAction(new OperationHandler(text.charAt(0)));
        return button;
    }

    private Button[] createAdditionalButtons() {
        Button[] additionalButtons = new Button[3];
        additionalButtons[0] = createOperationButton("(");
        additionalButtons[1] = createOperationButton(")");
        additionalButtons[2] = createOperationButton("^");
        return additionalButtons;
    }

    private Button createClearButton(String text) {
        Button button = new Button(text);
        return button;
    }

    private class ButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Button btn = (Button) event.getSource();
            String text = btn.getText();
            if (text.matches("[0-9.]")) {
                display.appendText(text);
            }
        }
    }

    private class OperationHandler implements EventHandler<ActionEvent> {
        private char operation;

        public OperationHandler(char operation) {
            this.operation = operation;
        }

        @Override
        public void handle(ActionEvent event) {
            switch (operation) {
                case '+':
                case '-':
                case '*':
                case '/':
                    handleBinaryOperation(operation);
                    break;
                case '=':
                    handleEqualsOperation();
                    break;
                // Additional operations
                case '(':
                    display.appendText("(");
                    break;
                case ')':
                    display.appendText(")");
                    break;
                case '^':
                    handlePowerOperation();
                    break;
            }
        }

        private void handleBinaryOperation(char operation) {
            if (!display.getText().isEmpty()) {
                firstOperand = Double.parseDouble(display.getText());
                currentOperation = operation;
                display.clear();
            }
        }

        private void handleEqualsOperation() {
            if (!display.getText().isEmpty()) {
                double secondOperand = Double.parseDouble(display.getText());
                switch (currentOperation) {
                    case '+':
                        display.setText(String.valueOf(firstOperand + secondOperand));
                        break;
                    case '-':
                        display.setText(String.valueOf(firstOperand - secondOperand));
                        break;
                    case '*':
                        display.setText(String.valueOf(firstOperand * secondOperand));
                        break;
                    case '/':
                        if (secondOperand != 0) {
                            display.setText(String.valueOf(firstOperand / secondOperand));
                        } else {
                            display.setText("Error");
                        }
                        break;
                }
                currentOperation = ' ';
            }
        }

        private void handlePowerOperation() {
            if (!display.getText().isEmpty()) {
                double base = firstOperand;
                int exponent = Integer.parseInt(display.getText());
                double result = Math.pow(base, exponent);
                display.setText(String.valueOf(result));
            }
        }
    }

    private class ClearHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            display.clear();
            currentOperation = ' ';
        }
    }

    private void setButtonColors(Button[] buttons) {
        for (Button button : buttons) {
            button.setStyle("-fx-background-color: green;");
        }
    }
}