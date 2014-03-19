import java.util.Scanner;

// A very simple command line post-order calculator
public class PostfixCLI {
    public static void main(String[] args) {
        // Keep reading lines of input each line of input is a post-order
        // exression For example: 2 4 * 3 + which evaluates to (2 * 4) + 3 = 11
        // Output the result of evaluating the expression or print an error if
        // it's an invalid expression with a helpful message. If the user
        // enters "exit" then exit. Output should be of the format:
        // ((2 * 4) + // 3) = 11
        Scanner in = new Scanner(System.in);
        while(true) {
            try {
                System.out.print(">>> ");  // Prompt for input
                String input = in.nextLine();

                if(input.equals("exit")) {
                    break;
                }

                // Let's build a tree
                Expression expression = Expression.parsePostOrder(input);
                // Use the tree here to evaluate
                System.out.println(expression + " = " + expression.evaluate());
            } catch(InvalidExpressionException e) {
                System.out.println("Invalid expression: " + e.getMessage());
                continue;
            } catch(RuntimeException e) {
                System.out.println("Runtime error: " + e.getMessage());
                continue;
            } catch(Exception e) {
                System.out.println("Unknown error: " + e.getMessage());
                continue;
            }
        }
    }
}
