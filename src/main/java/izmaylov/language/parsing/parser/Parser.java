package izmaylov.language.parsing.parser;

import izmaylov.language.parsing.lexer.Token;
import izmaylov.language.parsing.lexer.TokenType;
import izmaylov.language.parsing.parser.ast.*;

import java.util.*;

public class Parser {
    private List<Token> tokens;

    private Map<Integer, Integer> bracketsMatching;

    private List<Integer> eolIndices;

    public Program parse(List<Token> tokens) throws SyntaxErrorException {
        this.tokens = tokens;
        initializeBracketsMatching();
        initializeEolIndices();

        ParsingInfo<List<FunctionDefinition>> functionDefinitions = parseFunctionDefinitionBlock();

        ParsingInfo<Expression> expression = parseExpression(functionDefinitions.lastTokenIndex + 1);

        if (expression.lastTokenIndex + 1 != tokens.size())
            throw new SyntaxErrorException();

        return new Program(expression.result, functionDefinitions.result);
    }

    private static class ParsingInfo<T> {
        T result;
        int lastTokenIndex;

        ParsingInfo(T result, int lastTokenIndex) {
            this.result = result;
            this.lastTokenIndex = lastTokenIndex;
        }
    }

    private ParsingInfo<List<FunctionDefinition>> parseFunctionDefinitionBlock() throws SyntaxErrorException {
        int beginIndex = 0;
        List<FunctionDefinition> functionDefinitions = new ArrayList<>();

        for (Integer eolIndex : eolIndices) {
            ParsingInfo<FunctionDefinition> functionDefinition = parseFunctionDefinition(beginIndex);

            if (functionDefinition.lastTokenIndex + 1 != eolIndex) {
                throw new SyntaxErrorException();
            }

            beginIndex = functionDefinition.lastTokenIndex + 2;
            functionDefinitions.add(functionDefinition.result);
        }

        return new ParsingInfo<>(
                functionDefinitions,
                beginIndex - 1
        );
    }

    private ParsingInfo<FunctionDefinition> parseFunctionDefinition(int beginIndex) throws SyntaxErrorException {
        if (tokens.get(beginIndex).getType() != TokenType.IDENTIFIER) {
            throw new SyntaxErrorException("Function needs name!");
        }

        String name = tokens.get(beginIndex).getValue();

        if (tokens.size() <= beginIndex + 5) {
            throw new SyntaxErrorException("Function needs parameters and body!");
        }

        if (tokens.get(beginIndex + 1).getType() != TokenType.LEFT_PARENTHESIS) {
            throw new SyntaxErrorException("Expected (");
        }

        int paramsBegin = beginIndex + 1;
        int paramsEnd = bracketsMatching.get(paramsBegin);

        if (paramsBegin == paramsEnd - 1) {
            throw new SyntaxErrorException("Needs at least 1 parameter");
        }

        List<String> parameters = new ArrayList<>();
        for (int i = paramsBegin + 1; i < paramsEnd; i++) {
            if (i % 2 == 0) {
                if (tokens.get(i).getType() != TokenType.IDENTIFIER) {
                    throw new SyntaxErrorException("Expects an identifier");
                }

                parameters.add(tokens.get(i).getValue());
            }

            if (i % 2 == 1 && tokens.get(i).getType() != TokenType.DELIMITER) {
                throw new SyntaxErrorException("Expects a comma");
            }
        }

        if (tokens.size() <= paramsEnd + 3) {
            throw new SyntaxErrorException("Function need body!");
        }

        if (tokens.get(paramsEnd + 1).getType() != TokenType.OPERATION
                || !tokens.get(paramsEnd + 1).getValue().equals("=")) {

            throw new SyntaxErrorException("Expected =");
        }

        ParsingInfo<Expression> body = parseBody(paramsEnd + 2);

        return new ParsingInfo<>(
                new FunctionDefinition(name, parameters, body.result),
                body.lastTokenIndex
        );
    }

    private ParsingInfo<Expression> parseBody(int beginIndex) throws SyntaxErrorException {
        if (beginIndex >= tokens.size()) {
            throw new SyntaxErrorException("Expected {");
        }

        if (tokens.get(beginIndex).getType() != TokenType.LEFT_BRACE) {
            throw new SyntaxErrorException("Expected {");
        }

        int bodyEnd = bracketsMatching.get(beginIndex);
        if (beginIndex == bodyEnd - 1) {
            throw new SyntaxErrorException("Body cannot be empty!");
        }

        ParsingInfo<Expression> bodyInfo = parseExpression(beginIndex + 1);

        if (bodyEnd != bodyInfo.lastTokenIndex + 1) {
            throw new SyntaxErrorException();
        }

        return new ParsingInfo<>(
                bodyInfo.result,
                bodyEnd
        );
    }

    private ParsingInfo<Expression> parseExpression(int beginIndex) throws SyntaxErrorException {
        if (tokens.get(beginIndex).getType() == TokenType.OPERATION && tokens.get(beginIndex).getValue().equals("-")) {
            if (beginIndex + 1 >= tokens.size())
                throw new SyntaxErrorException();

            if (tokens.get(beginIndex + 1).getType() != TokenType.NUMBER)
                throw new SyntaxErrorException();

            return new ParsingInfo<>(
                    new ConstantExpression(true, Integer.parseInt(tokens.get(beginIndex + 1).getValue())),
                    beginIndex + 1
            );
        }

        switch (tokens.get(beginIndex).getType()) {
            case NUMBER:
                return new ParsingInfo<>(
                        new ConstantExpression(false, Integer.parseInt(tokens.get(beginIndex).getValue())),
                        beginIndex
                );

            case LEFT_PARENTHESIS:
                return parseBinaryExpression(beginIndex);

            case LEFT_SQUARE_BRACKET:
                return parseIfExpression(beginIndex);
        }

        throw new SyntaxErrorException("Unexpected token: " + tokens.get(beginIndex));
    }

    private ParsingInfo<Expression> parseBinaryExpression(int beginIndex) throws SyntaxErrorException {
        if (tokens.get(beginIndex).getType() != TokenType.LEFT_PARENTHESIS)
            throw new SyntaxErrorException();

        int endIndex = bracketsMatching.get(beginIndex);

        if (beginIndex == endIndex - 1)
            throw new SyntaxErrorException();

        ParsingInfo<Expression> leftExpressionInfo = parseExpression(beginIndex + 1);

        int operationIndex = leftExpressionInfo.lastTokenIndex + 1;

        if (operationIndex == endIndex)
            throw new SyntaxErrorException();

        ParsingInfo<Expression> rightExpressionInfo = parseExpression(operationIndex + 1);

        if (rightExpressionInfo.lastTokenIndex + 1 != endIndex)
            throw new SyntaxErrorException();

        return new ParsingInfo<>(
                new BinaryExpression(
                        leftExpressionInfo.result,
                        rightExpressionInfo.result,
                        tokens.get(operationIndex).getValue()
                ), endIndex
        );
    }

    private ParsingInfo<Expression> parseIfExpression(int beginIndex) throws SyntaxErrorException {
        if (tokens.get(beginIndex).getType() != TokenType.LEFT_SQUARE_BRACKET) {
            throw new SyntaxErrorException();
        }

        int conditionEndIndex = bracketsMatching.get(beginIndex);

        if (conditionEndIndex >= tokens.size() || tokens.get(conditionEndIndex + 1).getType() != TokenType.THEN_SIGN) {
            throw new SyntaxErrorException("? expected");
        }

        if (conditionEndIndex == beginIndex - 1) {
            throw new SyntaxErrorException("Condition cannot be empty");
        }

        ParsingInfo<Expression> conditionInfo = parseExpression(beginIndex + 1);

        if (conditionInfo.lastTokenIndex + 1 != conditionEndIndex) {
            throw new SyntaxErrorException();
        }

        int thenBegin = conditionEndIndex + 2;

        if (tokens.size() <= thenBegin) {
            throw new SyntaxErrorException("Expects if body");
        }

        // In grammar there should be (, but in tests was {, so I choose {

        ParsingInfo<Expression> thenBranchInfo = parseBody(thenBegin);

        if (tokens.size() <= thenBranchInfo.lastTokenIndex + 1 ||
                tokens.get(thenBranchInfo.lastTokenIndex + 1).getType() != TokenType.ELSE_SIGN) {
            throw new SyntaxErrorException();
        }

        int elseBegin = thenBranchInfo.lastTokenIndex + 2;

        ParsingInfo<Expression> elseBranchInfo = parseBody(elseBegin);

        return new ParsingInfo<>(
                new IfExpression(conditionInfo.result, thenBranchInfo.result, elseBranchInfo.result),
                elseBranchInfo.lastTokenIndex
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

    private void initializeEolIndices() {
        eolIndices = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == TokenType.EOL) {
                eolIndices.add(i);
            }
        }
    }


}
