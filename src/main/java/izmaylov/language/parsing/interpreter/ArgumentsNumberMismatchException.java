package izmaylov.language.parsing.interpreter;

public class ArgumentsNumberMismatchException extends ExecutionErrorException {
    private final String functionName;

    ArgumentsNumberMismatchException(int lineNumber, String functionName) {
        super(lineNumber);
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }
}
