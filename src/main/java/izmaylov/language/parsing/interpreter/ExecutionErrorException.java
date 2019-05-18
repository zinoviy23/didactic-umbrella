package izmaylov.language.parsing.interpreter;

public class ExecutionErrorException extends RuntimeException {
    private final int lineNumber;

    ExecutionErrorException(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
