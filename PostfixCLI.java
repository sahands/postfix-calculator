import java.util.Scanner;

public class PostfixCLI {
    private static String promptForInput(Scanner in) {
        System.out.print(">>> ");
        return in.nextLine();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter postfix expression to evaluate, 'exit' to exit.");
        while (true) {
            try {
                String input = promptForInput(in);
                if (input.equals("exit")) {
                    break;
                }

                Expression expression = Expression.parsePostOrder(input);
                System.out.format("%s = %.4f\n",
                                  expression.toString(),
                                  expression.evaluate());
            } catch (InvalidExpressionException e) {
                System.out.println("Invalid expression: " + e.getMessage());
            } catch (DivideByZeroException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
