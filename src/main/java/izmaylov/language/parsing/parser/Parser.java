package izmaylov.language.parsing.parser;

import izmaylov.language.parsing.lexer.Token;
import izmaylov.language.parsing.lexer.TokenType;
import izmaylov.language.parsing.parser.ast.BinaryExpression;
import izmaylov.language.parsing.parser.ast.ConstantExpression;
import izmaylov.language.parsing.parser.ast.Expression;
import izmaylov.language.parsing.parser.ast.Program;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Parser {
    private List<Token> tokens;

    private Map<Integer, Integer> bracketsMatching;

    public Program parse(List<Token> tokens) throws SyntaxErrorException {
        this.tokens = tokens;
        initializeBracketsMatching();

        ParsingInfo expression = parseExpression(0);

        if (expression.lastTokenIndex + 1 != tokens.size())
            throw new SyntaxErrorException();

        return new Program(expression.result);
    }

    private static class ParsingInfo {
        Expression result;
        int lastTokenIndex;

        ParsingInfo(Expression result, int lastTokenIndex) {
            this.result = result;
            this.lastTokenIndex = lastTokenIndex;
        }
    }

    private ParsingInfo parseExpression(int beginIndex) throws SyntaxErrorException {
        if (tokens.get(beginIndex).getType() == TokenType.OPERATION && tokens.get(beginIndex).getValue().equals("-")) {
            if (beginIndex + 1 >= tokens.size())
                throw new SyntaxErrorException();

            if (tokens.get(beginIndex + 1).getType() != TokenType.NUMBER)
                throw new SyntaxErrorException();

            return new ParsingInfo(
                    new ConstantExpression(true, Integer.parseInt(tokens.get(beginIndex + 1).getValue())),
                    beginIndex + 1
            );
        }

        switch (tokens.get(beginIndex).getType()) {
            case NUMBER:
                return new ParsingInfo(
                        new ConstantExpression(false, Integer.parseInt(tokens.get(beginIndex).getValue())),
                        beginIndex
                );

            case LEFT_PARENTHESIS:
                return parseBinaryExpression(beginIndex);
        }

        throw new SyntaxErrorException("Unexpected token: " + tokens.get(beginIndex));
    }

    private ParsingInfo parseBinaryExpression(int beginIndex) throws SyntaxErrorException {
        if (tokens.get(beginIndex).getType() != TokenType.LEFT_PARENTHESIS)
            throw new SyntaxErrorException();

        int endIndex = bracketsMatching.get(beginIndex);

        if (beginIndex == endIndex - 1)
            throw new SyntaxErrorException();

        ParsingInfo leftExpressionInfo = parseExpression(beginIndex + 1);

        int operationIndex = leftExpressionInfo.lastTokenIndex + 1;

        if (operationIndex == endIndex)
            throw new SyntaxErrorException();

        ParsingInfo rightExpressionInfo = parseExpression(operationIndex + 1);

        if (rightExpressionInfo.lastTokenIndex + 1 != endIndex)
            throw new SyntaxErrorException();

        return new ParsingInfo(
                new BinaryExpression(
                        leftExpressionInfo.result,
                        rightExpressionInfo.result,
                        tokens.get(operationIndex).getValue()
                ), endIndex
        );
    }

    private void initializeBracketsMatching() throws SyntaxErrorException {
        LinkedList<Integer> brackets = new LinkedList<>();
        bracketsMatching = new HashMap<>();

        for (int i = 0; i < tokens.size(); i++) {
            if (!TokenType.isBracket(tokens.get(i).getType()))
                continue;

            if (TokenType.isOpenBracket(tokens.get(i).getType())) {
                brackets.push(i);
            } else if (!brackets.isEmpty() &&
                    TokenType.isCloseFor(tokens.get(brackets.peek()).getType(), tokens.get(i).getType())) {

                int openIndex = brackets.pop();
                bracketsMatching.put(openIndex, i);
            } else {
                throw new SyntaxErrorException();
            }
        }

        if (!brackets.isEmpty())
            throw new SyntaxErrorException();
    }


}
