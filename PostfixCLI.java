import java.util.Scanner;

public class PostfixCLI {
    public static void main(String[] args) {
        System.out.println("Enter postfix expression to evaluate, 'exit' to exit.");
        Scanner in = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(">>> ");
                String input = in.nextLine();

                if (input.equals("exit")) {
                    break;
                }

                Expression expression = Expression.parsePostOrder(input);
                System.out.format("%s = %.4f\n",
                                  expression.toString(),
                                  expression.evaluate());
            } catch (InvalidExpressionException e) {
                System.out.println("Invalid expression: " + e.getMessage());
                continue;
            } catch (RuntimeException e) {
                System.out.println("Runtime error: " + e.getMessage());
                continue;
            } catch (Exception e) {
                System.out.println("Unknown error: " + e.getMessage());
                continue;
            }
        }
    }
}
