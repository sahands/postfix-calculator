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
            if(tokenizer.hasNextDouble()) {
                stack.push(new Number(tokenizer.nextDouble()));
            } else {
                String token = tokenizer.next();
                stack.push(Operator.fromSymbol(token, stack));
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
    double value;

    public Number(double value) {
        this.value = value;
    }

    public double evaluate() {
        return this.value;
    }

    public String toString() {
        return Double.toString(this.value);
    }
}


abstract class Operator extends Expression {
    String symbol;

    public Operator(String symbol) {
        this.symbol = symbol;
    }

    public static Operator fromSymbol(String symbol, Stack<Expression> parseStack)
        throws InvalidExpressionException {
        if (symbol.equals("+")) {
            return new AddOperator(parseStack);
        } else if (symbol.equals("-")) {
            return new SubtractOperator(parseStack);
        } else if (symbol.equals("*")) {
            return new MultiplyOperator(parseStack);
        } else if (symbol.equals("/")) {
            return new DivideOperator(parseStack);
        } else {
            throw new InvalidExpressionException("Invalid operator: " + symbol);
        }
    }
}

abstract class BinaryOperator extends Operator {
    Expression left;
    Expression right;

    public BinaryOperator(String symbol, Stack<Expression> parseStack) {
        super(symbol);
        if(parseStack.size() < 2) {
            throw new InvalidExpressionException("Not enough parameters for operator " +
                    symbol);
        }
        this.right = parseStack.pop();
        this.left = parseStack.pop();
    }

    public String toString() {
        return this.toInOrder();
    }

    public String toInOrder() {
        return "(" + this.left.toInOrder() + " " + this.symbol
                + " " + this.right.toInOrder() + ")";
    }

    public String toPostOrder() {
        return this.left.toPostOrder() + " " + this.right.toPostOrder() + " "
                + this.symbol;
    }

}

final class AddOperator extends BinaryOperator {
    protected AddOperator(Stack<Expression> parseStack) {
        super("+", parseStack);
    }

    public double evaluate() {
        return this.left.evaluate() + this.right.evaluate();
    }
}

final class SubtractOperator extends BinaryOperator {
    protected SubtractOperator(Stack<Expression> parseStack) {
        super("-", parseStack);
    }

    public double evaluate() {
        return this.left.evaluate() - this.right.evaluate();
    }
}

final class MultiplyOperator extends BinaryOperator {
    protected MultiplyOperator(Stack<Expression> parseStack) {
        super("*", parseStack);
    }

    public double evaluate() {
        return this.left.evaluate() * this.right.evaluate();
    }
}

final class DivideOperator extends BinaryOperator {
    protected DivideOperator(Stack<Expression> parseStack) {
        super("/", parseStack);
    }

    public double evaluate() throws DivideByZeroException {
        double left = this.left.evaluate();
        double right = this.right.evaluate();
        if (right == 0) {
            throw new DivideByZeroException("Division by zero in " + this.toString());
        }
        return left / right;
    }
}
