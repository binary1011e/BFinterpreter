package BrainF;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Lox main class for interpreter
 */
public class BF {
    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Use format: jlox [script] or jlox [path]");
            System.exit(64);
        } else if (args.length == 1) {
            // runs file using path
            runFile(args[0]);
        } else {
            // runs from terminal
            runPrompt();
        }
    }
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            System.out.print("-> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
        }
    }

    // Actual run function
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        // Splits up the line into tokens
        List<Token> tokens = scanner.scanTokens();
        // Parses the tokens into an AST (Abstract Syntax Tree)
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();
        System.out.println();
        if (hadError) return;
        // Interpreter interprets the expression
        interpreter.interpret(statements);
    }

    // Error reporting
    static void error(int line, String message) {
        report(line, "", message);
    }

    // Error printing
    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    // Error with a specific token
    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at " + token.current, message);
        }
    }

}