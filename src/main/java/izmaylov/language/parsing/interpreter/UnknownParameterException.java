package izmaylov.language.parsing.interpreter;

public class UnknownParameterException extends ExecutionErrorException {
    private final String name;

    UnknownParameterException(int lineNumber, String name) {
        super(lineNumber);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
