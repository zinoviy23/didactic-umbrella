package izmaylov.language.parsing.lexer;

public enum TokenType {
    NUMBER, IDENTIFIER, OPERATION,
    LEFT_PARENTHESIS, RIGHT_PARENTHESIS,
    LEFT_SQUARE_BRACKET, RIGHT_SQUARE_BRACKET,
    LEFT_BRACE, RIGHT_BRACE,
    THEN_SIGN, ELSE_SIGN,
    EOL;

    public static boolean isOpenBracket(TokenType tokenType) {
        return tokenType == LEFT_PARENTHESIS || tokenType == LEFT_SQUARE_BRACKET || tokenType == LEFT_BRACE;
    }

    public static boolean isCloseFor(TokenType open, TokenType close) {
        return open == LEFT_PARENTHESIS && close == RIGHT_PARENTHESIS ||
                open == LEFT_SQUARE_BRACKET && close == RIGHT_SQUARE_BRACKET ||
                open == LEFT_BRACE && close == RIGHT_BRACE;
    }

    public static boolean isBracket(TokenType tokenType) {
        return tokenType == LEFT_SQUARE_BRACKET || tokenType == RIGHT_PARENTHESIS ||
                tokenType == LEFT_PARENTHESIS || tokenType == RIGHT_SQUARE_BRACKET ||
                tokenType == LEFT_BRACE || tokenType == RIGHT_BRACE;
    }
}
