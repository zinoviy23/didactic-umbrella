package izmaylov.language.parsing;

import izmaylov.language.parsing.interpreter.*;
import izmaylov.language.parsing.lexer.Lexer;
import izmaylov.language.parsing.lexer.LexerErrorException;
import izmaylov.language.parsing.lexer.Token;
import izmaylov.language.parsing.parser.Parser;
import izmaylov.language.parsing.parser.SyntaxErrorException;
import izmaylov.language.parsing.parser.ast.Program;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter a program(Ctrl+D end of input): ");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String code = reader.lines().collect(Collectors.joining("\n"));

        try {
            Lexer lexer = new Lexer();
            List<Token> tokens = lexer.tokenize(code);

            Parser parser = new Parser();
            Program program = parser.parse(tokens);

            Interpreter interpreter = new Interpreter();
            System.out.println("output: " + interpreter.execute(program));
        } catch (SyntaxErrorException | LexerErrorException e) {
            System.out.println("SYNTAX ERROR");
        } catch (UnknownParameterException e) {
            System.out.printf("PARAMETER NOT FOUND %s:%d\n", e.getName(), e.getLineNumber());
        } catch (UnknownFunctionException e) {
            System.out.printf("FUNCTION NOT FOUND %s:%d\n", e.getFunctionName(), e.getLineNumber());
        } catch (ArgumentsNumberMismatchException e) {
            System.out.printf("ARGUMENT NUMBER MISMATCH %s:%d\n", e.getFunctionName(), e.getLineNumber());
        } catch (RuntimeErrorException e) {
            System.out.printf("RUNTIME ERROR %s:%d\n", e.getExpression(), e.getLineNumber());
        }
    }
}
