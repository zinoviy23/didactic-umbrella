package izmaylov.language.parsing.parser.ast;

public class BinaryExpression extends Expression {
    private final Expression leftExpression;
    private final Expression rightExpression;
    private final String operation;

    public BinaryExpression(Expression leftExpression, Expression rightExpression, String operation, int lineNumber) {
        super(lineNumber);
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.operation = operation;
    }

    public Expression getLeftExpression() {
        return leftExpression;
    }

    public Expression getRightExpression() {
        return rightExpression;
    }

    public String getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return "(" + leftExpression + operation + rightExpression + ")";
    }
}
