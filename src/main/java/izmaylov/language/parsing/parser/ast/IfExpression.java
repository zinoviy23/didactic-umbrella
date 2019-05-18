package izmaylov.language.parsing.parser.ast;

public class IfExpression extends Expression {
    private final Expression condition;
    private final Expression thenBranch;
    private final Expression elseBranch;

    public IfExpression(Expression condition, Expression thenBranch, Expression elseBranch, int lineNumber) {
        super(lineNumber);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public Expression getCondition() {
        return condition;
    }

    public Expression getThenBranch() {
        return thenBranch;
    }

    public Expression getElseBranch() {
        return elseBranch;
    }

    @Override
    public String toString() {
        return String.format("[%s]?{%s}:{%s}", condition, thenBranch, elseBranch);
    }
}
