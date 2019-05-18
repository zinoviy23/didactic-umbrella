package izmaylov.language.parsing.interpreter;

import izmaylov.language.parsing.parser.ast.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class InterpreterTest {
    @Test
    public void numberInterpretation() {
        ConstantExpression expression = new ConstantExpression(false, 10, 1);
        
        Interpreter interpreter = new Interpreter();
        int result = interpreter.execute(new Program(expression, Collections.emptyList()));
        
        assertEquals(10, result);
    }

    @Test
    public void negativeNumberInterpretation() {
        ConstantExpression expression = new ConstantExpression(true, 11, 1);

        Interpreter interpreter = new Interpreter();
        int result = interpreter.execute(new Program(expression, Collections.emptyList()));

        assertEquals(-11, result);
    }

    @Test
    public void binaryExpression() {
        ConstantExpression left = new ConstantExpression(false, 13, 1);
        ConstantExpression right = new ConstantExpression(false, 10, 1);

        Interpreter interpreter = new Interpreter();
        int result = interpreter.execute(new Program(new BinaryExpression(left, right, "%", 1),
                Collections.emptyList()));

        assertEquals(3, result);
    }

    @Test
    public void complexExpression() {
        BinaryExpression left = new BinaryExpression(
                new ConstantExpression(true, 3, 1),
                new ConstantExpression(false, 10, 1),
                "*",
                1
        );
        ConstantExpression right = new ConstantExpression(false, 2, 1);

        Interpreter interpreter = new Interpreter();
        int result = interpreter.execute(new Program(new BinaryExpression(left, right, "/", 1),
                Collections.emptyList()));

        assertEquals(-15, result);
    }

    @Test
    public void ifExpressionTrue() {
        IfExpression expression = new IfExpression(
                new BinaryExpression(
                        new ConstantExpression(false, 1, 1),
                        new ConstantExpression(false, 0, 1),
                        ">",
                        1),
                new ConstantExpression(true, 1, 1),
                new ConstantExpression(false, 1, 1),
                1
        );

        Interpreter interpreter = new Interpreter();
        int result = interpreter.execute(new Program(expression, Collections.emptyList()));

        assertEquals(-1, result);
    }

    @Test
    public void ifExpressionFalse() {
        IfExpression expression = new IfExpression(
                new BinaryExpression(
                        new ConstantExpression(false, 1, 1),
                        new ConstantExpression(false, 0, 1),
                        "<",
                        1),
                new ConstantExpression(true, 1, 1),
                new ConstantExpression(false, 1, 1),
                1
        );

        Interpreter interpreter = new Interpreter();
        int result = interpreter.execute(new Program(expression, Collections.emptyList()));

        assertEquals(1, result);
    }

    @Test
    public void simpleProgramWithFunction() {
        FunctionDefinition functionDefinition = new FunctionDefinition(
            "sum",
                Arrays.asList("a", "b"),
                new BinaryExpression(
                        new Identifier("a", 1),
                        new Identifier("b", 1),
                        "+",
                        1
                )
        );

        Interpreter interpreter = new Interpreter();
        int result = interpreter.execute(new Program(new BinaryExpression(
                new CallExpression(
                        "sum",
                        Arrays.asList(
                                new ConstantExpression(false, 1, 1),
                                new ConstantExpression(false,2, 1)
                        ),
                        1
                ),
                new ConstantExpression(true, 10, 1),
                "*",
                1
            ), Collections.singletonList(functionDefinition)));

        assertEquals(-30, result);
    }

    @Test(expected = RuntimeErrorException.class)
    public void divisionByZero() {
        Interpreter interpreter = new Interpreter();
        BinaryExpression expression = new BinaryExpression(
                new ConstantExpression(false, 1, 1),
                new ConstantExpression(false, 0, 1),
                "/",
                1
        );

        interpreter.execute(new Program(expression, Collections.emptyList()));
    }

    @Test(expected = UnknownParameterException.class)
    public void unknownParameter() {
        Interpreter interpreter = new Interpreter();
        BinaryExpression expression = new BinaryExpression(
                new ConstantExpression(false, 1, 1),
                new Identifier("a", 1),
                "/",
                1
        );

        interpreter.execute(new Program(expression, Collections.emptyList()));
    }

    @Test(expected = UnknownFunctionException.class)
    public void unknownFunction() {
        Interpreter interpreter = new Interpreter();
        BinaryExpression expression = new BinaryExpression(
                new ConstantExpression(false, 1, 1),
                new CallExpression("a",
                        Collections.singletonList(
                                new ConstantExpression(false, 1, 1)), 1),
                "/",
                1
        );

        interpreter.execute(new Program(expression, Collections.emptyList()));
    }

    @Test(expected = ArgumentsNumberMismatchException.class)
    public void mismatchParameter() {
        FunctionDefinition functionDefinition = new FunctionDefinition(
                "a",
                Arrays.asList("b", "c"),
                new ConstantExpression(false, 0, 1)
        );

        Interpreter interpreter = new Interpreter();
        BinaryExpression expression = new BinaryExpression(
                new ConstantExpression(false, 1, 2),
                new CallExpression("a",
                        Collections.singletonList(
                                new ConstantExpression(false, 1, 2)
                        ),
                        2),
                "/",
                1
        );

        interpreter.execute(new Program(expression, Collections.singletonList(functionDefinition)));
    }
}