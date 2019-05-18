package izmaylov.language.parsing.interpreter;

import izmaylov.language.parsing.parser.ast.BinaryExpression;
import izmaylov.language.parsing.parser.ast.ConstantExpression;
import izmaylov.language.parsing.parser.ast.Program;
import org.junit.Test;

import static org.junit.Assert.*;

public class InterpreterTest {
    @Test
    public void numberInterpretation() {
        ConstantExpression expression = new ConstantExpression(false, 10);
        
        Interpreter interpreter = new Interpreter();
        int result = interpreter.execute(new Program(expression));
        
        assertEquals(10, result);
    }

    @Test
    public void negativeNumberInterpretation() {
        ConstantExpression expression = new ConstantExpression(true, 11);

        Interpreter interpreter = new Interpreter();
        int result = interpreter.execute(new Program(expression));

        assertEquals(-11, result);
    }

    @Test
    public void binaryExpression() {
        ConstantExpression left = new ConstantExpression(false, 13);
        ConstantExpression right = new ConstantExpression(false, 10);

        Interpreter interpreter = new Interpreter();
        int result = interpreter.execute(new Program(new BinaryExpression(left, right, "%")));

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
        int result = interpreter.execute(new Program(new BinaryExpression(left, right, "/")));

        assertEquals(-15, result);
    }
}