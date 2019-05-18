package izmaylov.language.parsing.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Lexer {
    private String currentText;

    public List<Token> tokenize(String text) {
        currentText = Objects.requireNonNull(text, "text cannot be null");

        List<Token> tokens = new ArrayList<>();

        for (int i = 0; i < text.length();) {
            int tokenEnd;
            if (LexerUtils.isCharacter(charAt(i))) {
                tokenEnd = findTokenEnd(i, LexerUtils::isCharacter);

                tokens.add(new Token(substring(i, tokenEnd), TokenType.IDENTIFIER));
            } else if (Character.isDigit(charAt(i))) {
                tokenEnd = findTokenEnd(i, Character::isDigit);

                tokens.add(new Token(substring(i, tokenEnd), TokenType.NUMBER));
            } else if (LexerUtils.isOperator(charAt(i))) {
                tokenEnd = i + 1;

                tokens.add(new Token(substring(i, i + 1), TokenType.OPERATION));
            } else if (LexerUtils.isParenthesis(charAt(i)) != null) {
                tokenEnd = i + 1;
                tokens.add(new Token(substring(i, i + 1), LexerUtils.isParenthesis(charAt(i))));
            } else if (LexerUtils.isSquareBracket(charAt(i)) != null) {
                tokenEnd = i + 1;
                tokens.add(new Token(substring(i, i + 1), LexerUtils.isSquareBracket(charAt(i))));
            } else if (LexerUtils.isEOL(charAt(i))) {
                tokenEnd = i + 1;
                tokens.add(new Token(substring(i, i + 1), TokenType.EOL));
            } else {
                throw new UnsupportedOperationException("Unsupported tokens!");
            }

            i = tokenEnd;
        }

        return tokens;
    }

    private char charAt(int index) {
        return currentText.charAt(index);
    }

    private String substring(int startIndex, int endIndex) {
        return currentText.substring(startIndex, endIndex);
    }

    private int findTokenEnd(int from, Predicate<Character> predicate) {
        int end = from + 1;
        while (end < currentText.length() && predicate.test(currentText.charAt(end))) {
            end++;
        }

        return end;
    }
}
