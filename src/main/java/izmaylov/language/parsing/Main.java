package izmaylov.language.parsing;

import izmaylov.language.parsing.interpreter.Interpreter;
import izmaylov.language.parsing.lexer.Lexer;
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

        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.tokenize(code);

        try {
            Parser parser = new Parser();
            Program program = parser.parse(tokens);

            Interpreter interpreter = new Interpreter();
            System.out.println("output: " + interpreter.execute(program));
        } catch (SyntaxErrorException e) {
            e.printStackTrace();
        }
    }
}
