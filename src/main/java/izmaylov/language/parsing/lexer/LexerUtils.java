package izmaylov.language.parsing.lexer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Util class for some checks from grammar
 */
class LexerUtils {
    private static Set<Character> operators = new HashSet<>(Arrays.asList(
            '+', '-', '*', '/', '%', '>', '<', '='
    ));

    static boolean isCharacter(char c) {
        return 'a' <= Character.toLowerCase(c) && c <= 'z' || c == '_';
    }

    static boolean isOperator(char c) {
        return operators.contains(c);
    }

    static TokenType isParenthesis(char c) {
        if (c == '(')
            return TokenType.LEFT_PARENTHESIS;
        else if (c == ')')
            return TokenType.RIGHT_PARENTHESIS;

        return null;
    }

    static TokenType isSquareBracket(char c) {
        if (c == '[')
            return TokenType.LEFT_SQUARE_BRACKET;
        else if (c == ']')
            return TokenType.RIGHT_SQUARE_BRACKET;

        return null;
    }

    static TokenType isBrace(char c) {
        if (c == '{')
            return TokenType.LEFT_BRACE;
        else if (c == '}')
            return TokenType.RIGHT_BRACE;

        return null;
    }

    static boolean isEOL(char c) {
        return c == '\n';
    }

    static boolean isThenSign(char c) {
        return c == '?';
    }

    static boolean isElseSign(char c) {
        return c == ':';
    }

    private LexerUtils() {}
}
