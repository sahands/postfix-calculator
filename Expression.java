import java.util.Stack;
import java.util.Scanner;

public abstract class Expression {
    public abstract double evaluate();

    public String toPostOrder() {
        return this.toString();
    }

    public String toInOrder() {
        return this.toString();
    }

    /*
     * Parse an expresssion tree given a string and return the resulting tree.
     * InvalidExpressionException is raised in cases of invalid input.
     */
    public static Expression parsePostOrder(String expression)
            throws InvalidExpressionException {
        Scanner tokenizer = new Scanner(expression);
        Stack<Expression> stack = new Stack<Expression>();
        while (tokenizer.hasNext()) {
            String token = tokenizer.next();
            try {
                double number = Double.parseDouble(token);
                stack.push(new Number(number));
            } catch (NumberFormatException e) {
                // If control reaches here it's because the token is not a
                // number, so it must be an operator.
                if (stack.size() < 2) {
                    throw new InvalidExpressionException(
                            "Not enough parameters for " + token);
                }
                Expression right = stack.pop();
                Expression left = stack.pop();
                stack.push(BinaryOperator.fromSymbol(token, left, right));
            }
        }

        if (stack.size() != 1) {
            throw new InvalidExpressionException("Not enough operators.");
        }

        // The single item left on the stack is the root of the tree.
        return stack.pop();
    }
}

class Number extends Expression {
    double number;

    public Number(double num) {
        super();
        this.number = num;
    }

    public double evaluate() {
        return this.number;
    }

    public String toString() {
        return Double.toString(this.number);
    }
}

abstract class BinaryOperator extends Expression {
    Expression left;
    Expression right;

    protected abstract String getOperatorSymbol();

    public BinaryOperator(Expression left, Expression right) {
        super();
        this.left = left;
        this.right = right;
    }

    public String toString() {
        return this.toInOrder();
    }

    public String toInOrder() {
        return "(" + this.left.toInOrder() + " " + this.getOperatorSymbol()
                + " " + this.right.toInOrder() + ")";
    }

    public String toPostOrder() {
        return this.left.toPostOrder() + " " + this.right.toPostOrder() + " "
                + this.getOperatorSymbol();
    }

    public static BinaryOperator fromSymbol(String symbol, Expression left,
            Expression right) throws InvalidExpressionException {
        if (symbol.equals("+")) {
            return new AddOperator(left, right);
        } else if (symbol.equals("-")) {
            return new SubtractOperator(left, right);
        } else if (symbol.equals("*")) {
            return new MultiplyOperator(left, right);
        } else if (symbol.equals("/")) {
            return new DivideOperator(left, right);
        } else {
            throw new InvalidExpressionException("Invalid operator: " + symbol);
        }
    }
}

final class AddOperator extends BinaryOperator {
    protected AddOperator(Expression left, Expression right) {
        super(left, right);
    }

    protected String getOperatorSymbol() {
        return "+";
    }

    public double evaluate() {
        return this.left.evaluate() + this.right.evaluate();
    }
}

final class SubtractOperator extends BinaryOperator {
    protected SubtractOperator(Expression left, Expression right) {
        super(left, right);
    }

    protected String getOperatorSymbol() {
        return "-";
    }

    public double evaluate() {
        return this.left.evaluate() - this.right.evaluate();
    }
}

final class MultiplyOperator extends BinaryOperator {
    protected MultiplyOperator(Expression left, Expression right) {
        super(left, right);
    }

    protected String getOperatorSymbol() {
        return "*";
    }

    public double evaluate() throws RuntimeException {
        return this.left.evaluate() * this.right.evaluate();
    }
}

final class DivideOperator extends BinaryOperator {
    protected DivideOperator(Expression left, Expression right) {
        super(left, right);
    }

    protected String getOperatorSymbol() {
        return "/";
    }

    public double evaluate() {
        double left = this.left.evaluate();
        double right = this.right.evaluate();
        if (right == 0) {
            throw new RuntimeException("Division by zero in " + this.toString());
        }
        return left / right;
    }
}
