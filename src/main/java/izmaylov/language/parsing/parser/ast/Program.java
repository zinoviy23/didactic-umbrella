package izmaylov.language.parsing.parser.ast;

public class Program extends ASTNode {
    private final Expression expression;

    public Expression getExpression() {
        return expression;
    }

    public Program(Expression expression) {
        this.expression = expression;
    }
}
