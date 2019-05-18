package izmaylov.language.parsing.parser.ast;

import java.util.Collections;
import java.util.List;

public class Program {
    private final Expression expression;
    private final List<FunctionDefinition> functionDefinitions;

    public Expression getExpression() {
        return expression;
    }

    public Program(Expression expression, List<FunctionDefinition> functionDefinitions) {
        this.expression = expression;
        this.functionDefinitions = functionDefinitions;
    }

    public List<FunctionDefinition> getFunctionDefinitions() {
        return Collections.unmodifiableList(functionDefinitions);
    }
}
