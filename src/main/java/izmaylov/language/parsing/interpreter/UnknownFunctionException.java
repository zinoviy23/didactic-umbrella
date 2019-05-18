package izmaylov.language.parsing.interpreter;

public class UnknownFunctionException extends ExecutionErrorException {
    private final String functionName;

    UnknownFunctionException(int lineNumber, String functionName) {
        super(lineNumber);
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }
}
