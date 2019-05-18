package izmaylov.language.parsing.interpreter;

public class RuntimeErrorException extends ExecutionErrorException {
    private final String expression;

    RuntimeErrorException(int lineNumber, String expression) {
        super(lineNumber);
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }
}
