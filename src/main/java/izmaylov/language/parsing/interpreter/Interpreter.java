package izmaylov.language.parsing.interpreter;

import izmaylov.language.parsing.parser.ast.*;

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
        if (expression instanceof IfExpression) {
            return executeIfExpression((IfExpression) expression);
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

    private int executeIfExpression(IfExpression expression) {
        if (execute(expression.getCondition()) != 0) {
            return execute(expression.getThenBranch());
        } else {
            return execute(expression.getElseBranch());
        }
    }
}
