import java.util.Stack;

public class Calculation {
    public static double evaluate(String expression) {
        // Remove any spaces from the expression
        expression = expression.replaceAll("\\s+", "");

        // Create a stack to hold numbers
        Stack<Double> numbers = new Stack<>();
        // Create a stack to hold operators
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (Character.isDigit(ch)) {
                StringBuilder numBuilder = new StringBuilder();
                numBuilder.append(ch);
                // Continue reading digits to form the complete number
                while (i + 1 < expression.length() && (Character.isDigit(expression.charAt(i + 1)) || expression.charAt(i + 1) == '.')) {
                    numBuilder.append(expression.charAt(++i));
                }
                // Convert the string to a double and push it to the numbers stack
                numbers.push(Double.parseDouble(numBuilder.toString()));
            } else if (ch == '(') {
                // If the character is an opening parenthesis, push it to the operators stack
                operators.push(ch);
            } else if (ch == ')') {
                // If the character is a closing parenthesis, evaluate all expressions inside the parenthesis
                while (operators.peek() != '(') {
                    double result = performOperation(operators.pop(), numbers.pop(), numbers.pop());
                    numbers.push(result);
                }
                operators.pop(); // Pop the opening parenthesis
            } else if (isOperator(ch)) {
                // If the character is an operator, evaluate expressions with higher precedence first
                while (!operators.isEmpty() && hasHigherPrecedence(ch, operators.peek())) {
                    double result = performOperation(operators.pop(), numbers.pop(), numbers.pop());
                    numbers.push(result);
                }
                operators.push(ch); // Push the current operator to the operators stack
            } else if (ch == '√') {
                // If the character is square root, evaluate the square root and push it to the numbers stack
                StringBuilder numBuilder = new StringBuilder();
                i++; // Move to the next character after the square root symbol
                // Read the number inside the parentheses
                while (i < expression.length() && expression.charAt(i) != ')') {
                    numBuilder.append(expression.charAt(i++));
                }
                // Convert the string to a double and compute the square root
                double operand = Double.parseDouble(numBuilder.toString().substring(1)); // Exclude the opening parenthesis
                double result = Math.sqrt(operand);
                numbers.push(result);
            }
        }

        // Evaluate remaining expressions in the stack
        while (!operators.isEmpty()) {
            double result = performOperation(operators.pop(), numbers.pop(), numbers.pop());
            numbers.push(result);
        }

        // The final result is on top of the numbers stack
        return numbers.pop();
    }

    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private static boolean hasHigherPrecedence(char op1, char op2) {
        return (op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-');
    }

    private static double performOperation(char operator, double operand2, double operand1) {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return operand1 / operand2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}
