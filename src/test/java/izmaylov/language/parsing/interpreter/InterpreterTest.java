package izmaylov.language.parsing.interpreter;

import izmaylov.language.parsing.parser.ast.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class InterpreterTest {
    @Test
    public void numberInterpretation() {
        ConstantExpression expression = new ConstantExpression(false, 10);
        
        Interpreter interpreter = new Interpreter();
        int result = interpreter.execute(new Program(expression, Collections.emptyList()));
        
        assertEquals(10, result);
    }

    @Test
    public void negativeNumberInterpretation() {
        ConstantExpression expression = new ConstantExpression(true, 11);

        Interpreter interpreter = new Interpreter();
        int result = interpreter.execute(new Program(expression, Collections.emptyList()));

        assertEquals(-11, result);
    }

    @Test
    public void binaryExpression() {
        ConstantExpression left = new ConstantExpression(false, 13);
        ConstantExpression right = new ConstantExpression(false, 10);

        Interpreter interpreter = new Interpreter();
        int result = interpreter.execute(new Program(new BinaryExpression(left, right, "%"),
                Collections.emptyList()));

        assertEquals(3, result);
    }

    @Test
    public void complexExpression() {
        BinaryExpression left = new BinaryExpression(
                new ConstantExpression(true, 3),
                new ConstantExpression(false, 10),
                "*"
        );
        ConstantExpression right = new ConstantExpression(false, 2);

        Interpreter interpreter = new Interpreter();
        int result = interpreter.execute(new Program(new BinaryExpression(left, right, "/"),
                Collections.emptyList()));

        assertEquals(-15, result);
    }

    @Test
    public void ifExpressionTrue() {
        IfExpression expression = new IfExpression(
                new BinaryExpression(
                        new ConstantExpression(false, 1),
                        new ConstantExpression(false, 0),
                        ">"),
                new ConstantExpression(true, 1),
                new ConstantExpression(false, 1)
        );

        Interpreter interpreter = new Interpreter();
        int result = interpreter.execute(new Program(expression, Collections.emptyList()));

        assertEquals(-1, result);
    }

    @Test
    public void ifExpressionFalse() {
        IfExpression expression = new IfExpression(
                new BinaryExpression(
                        new ConstantExpression(false, 1),
                        new ConstantExpression(false, 0),
                        "<"),
                new ConstantExpression(true, 1),
                new ConstantExpression(false, 1)
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
                        new Identifier("a"),
                        new Identifier("b"),
                        "+"
                )
        );

        Interpreter interpreter = new Interpreter();
        int result = interpreter.execute(new Program(new BinaryExpression(
                new CallExpression(
                        "sum",
                        Arrays.asList(
                                new ConstantExpression(false, 1),
                                new ConstantExpression(false,2)
                        )
                ),
                new ConstantExpression(true, 10),
                "*"
            ), Collections.singletonList(functionDefinition)));

        assertEquals(-30, result);
    }
}