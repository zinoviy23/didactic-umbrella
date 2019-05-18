package izmaylov.language.parsing.lexer;

public enum TokenType {
    NUMBER, IDENTIFIER, OPERATION,
    LEFT_PARENTHESIS, RIGHT_PARENTHESIS,
    LEFT_SQUARE_BRACKET, RIGHT_SQUARE_BRACKET,
    EOL;

    public static boolean isOpenBracket(TokenType tokenType) {
        return tokenType == LEFT_PARENTHESIS || tokenType == LEFT_SQUARE_BRACKET;
    }

    public static boolean isCloseFor(TokenType open, TokenType close) {
        return open == LEFT_PARENTHESIS && close == RIGHT_PARENTHESIS ||
                open == LEFT_SQUARE_BRACKET && close == RIGHT_SQUARE_BRACKET;
    }

    public static boolean isBracket(TokenType tokenType) {
        return tokenType == LEFT_SQUARE_BRACKET || tokenType == RIGHT_PARENTHESIS ||
                tokenType == LEFT_PARENTHESIS || tokenType == RIGHT_SQUARE_BRACKET;
    }
}
