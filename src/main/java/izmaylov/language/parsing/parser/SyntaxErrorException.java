package izmaylov.language.parsing.parser;

public class SyntaxErrorException extends Exception {
    public SyntaxErrorException() {
    }

    public SyntaxErrorException(String message) {
        super(message);
    }
}
