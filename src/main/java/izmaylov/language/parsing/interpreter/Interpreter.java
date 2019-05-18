package izmaylov.language.parsing.interpreter;

import izmaylov.language.parsing.parser.ast.BinaryExpression;
import izmaylov.language.parsing.parser.ast.ConstantExpression;
import izmaylov.language.parsing.parser.ast.Expression;
import izmaylov.language.parsing.parser.ast.Program;

public class Interpreter {
    public int execute(Program program) {
        return execute(program.getExpression());
    }

    private int execute(Expression expression) {
        if (expression instanceof BinaryExpression) {
            return executeBinaryExpression((BinaryExpression) expression);
        }
        if (expression instanceof ConstantExpression) {
            return executeConstantExpression((ConstantExpression) expression);
        }

        throw new AssertionError("Cannot be here now");
    }

    private int executeBinaryExpression(BinaryExpression expression) {
        return BinaryOperations
                .getOperation(expression.getOperation())
                .applyAsInt(
                        execute(expression.getLeftExpression()),
                        execute(expression.getRightExpression())
                );
    }

    private int executeConstantExpression(ConstantExpression expression) {
        return expression.getMinus()
                ? -expression.getNumber()
                : expression.getNumber();
    }
}
