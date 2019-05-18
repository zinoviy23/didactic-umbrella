package izmaylov.language.parsing.lexer;

public class LexerErrorException extends RuntimeException {
    public LexerErrorException() {
    }

    LexerErrorException(String message) {
        super(message);
    }
}
