package izmaylov.language.parsing.parser;

import izmaylov.language.parsing.lexer.Token;
import izmaylov.language.parsing.lexer.TokenType;
import izmaylov.language.parsing.parser.ast.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void parseNumber() throws SyntaxErrorException {
        Parser parser = new Parser();
        Program program = parser.parse(Collections.singletonList(new Token("12", TokenType.NUMBER)));

        assertTrue(program.getExpression() instanceof ConstantExpression);

        ConstantExpression expression = (ConstantExpression) program.getExpression();

        assertFalse(expression.getMinus());
        assertEquals(12, expression.getNumber());
    }

    @Test
    public void parseNegativeNumber() throws SyntaxErrorException {
        Parser parser = new Parser();

        Program program = parser.parse(Arrays.asList(
                new Token("-", TokenType.OPERATION),
                new Token("13", TokenType.NUMBER))
        );

        assertTrue(program.getExpression() instanceof ConstantExpression);

        ConstantExpression expression = (ConstantExpression) program.getExpression();

        assertTrue(expression.getMinus());
        assertEquals(13, expression.getNumber());
    }

    @Test
    public void parseBinarySimpleExpression() throws SyntaxErrorException {
        Parser parser = new Parser();

        Program program = parser.parse(Arrays.asList(
                new Token("(", TokenType.LEFT_PARENTHESIS),
                new Token("12", TokenType.NUMBER),
                new Token("%", TokenType.OPERATION),
                new Token("-", TokenType.OPERATION),
                new Token("4", TokenType.NUMBER),
                new Token(")", TokenType.RIGHT_PARENTHESIS)
        ));

        assertTrue(program.getExpression() instanceof BinaryExpression);

        BinaryExpression expression = (BinaryExpression) program.getExpression();

        assertEquals("%", expression.getOperation());

        assertTrue(expression.getLeftExpression() instanceof ConstantExpression);
        assertTrue(expression.getRightExpression() instanceof ConstantExpression);

        ConstantExpression left = (ConstantExpression) expression.getLeftExpression();
        ConstantExpression right = ((ConstantExpression) expression.getRightExpression());

        assertFalse(left.getMinus());
        assertTrue(right.getMinus());

        assertEquals(12, left.getNumber());
        assertEquals(4, right.getNumber());
    }

    @Test
    public void complexBinaryExpression() throws SyntaxErrorException {
        Parser parser = new Parser();

        Program program = parser.parse(Arrays.asList(
                new Token("(", TokenType.LEFT_PARENTHESIS),
                new Token("1", TokenType.NUMBER),
                new Token("+", TokenType.OPERATION),
                new Token("(", TokenType.LEFT_PARENTHESIS),
                new Token("-", TokenType.OPERATION),
                new Token("1", TokenType.NUMBER),
                new Token("*", TokenType.OPERATION),
                new Token("200", TokenType.NUMBER),
                new Token(")", TokenType.RIGHT_PARENTHESIS),
                new Token(")", TokenType.RIGHT_PARENTHESIS)
        ));

        assertTrue(program.getExpression() instanceof BinaryExpression);

        BinaryExpression expression = ((BinaryExpression) program.getExpression());

        assertTrue(expression.getLeftExpression() instanceof ConstantExpression);
        assertTrue(expression.getRightExpression() instanceof BinaryExpression);

        ConstantExpression left = ((ConstantExpression) expression.getLeftExpression());
        BinaryExpression right = ((BinaryExpression) expression.getRightExpression());

        assertEquals(1, left.getNumber());
        assertFalse(left.getMinus());

        assertTrue(right.getRightExpression() instanceof ConstantExpression);
        assertTrue(right.getLeftExpression() instanceof ConstantExpression);

        ConstantExpression c1 = ((ConstantExpression) right.getLeftExpression());
        ConstantExpression c2 = ((ConstantExpression) right.getRightExpression());

        assertTrue(c1.getMinus());
        assertEquals(1, c1.getNumber());

        assertFalse(c2.getMinus());
        assertEquals(200, c2.getNumber());
    }

    @Test
    public void ifExpression() throws SyntaxErrorException {
        Parser parser = new Parser();

        Program program = parser.parse(Arrays.asList(
                new Token("[", TokenType.LEFT_SQUARE_BRACKET),
                new Token("1", TokenType.NUMBER),
                new Token("]", TokenType.RIGHT_SQUARE_BRACKET),
                new Token("?", TokenType.THEN_SIGN),
                new Token("{", TokenType.LEFT_BRACE),
                new Token("0", TokenType.NUMBER),
                new Token("}", TokenType.RIGHT_BRACE),
                new Token(":", TokenType.ELSE_SIGN),
                new Token("{", TokenType.LEFT_BRACE),
                new Token("-", TokenType.OPERATION),
                new Token("1", TokenType.NUMBER),
                new Token("}", TokenType.RIGHT_BRACE)
        ));

        assertTrue(program.getExpression() instanceof IfExpression);

        IfExpression ifExpression = ((IfExpression) program.getExpression());

        assertTrue(ifExpression.getCondition() instanceof ConstantExpression);
        assertTrue(ifExpression.getThenBranch() instanceof ConstantExpression);
        assertTrue(ifExpression.getElseBranch() instanceof ConstantExpression);

        ConstantExpression condition = (ConstantExpression) ifExpression.getCondition();
        ConstantExpression thenBranch = (ConstantExpression) ifExpression.getThenBranch();
        ConstantExpression elseBranch = (ConstantExpression) ifExpression.getElseBranch();

        assertFalse(condition.getMinus());
        assertFalse(thenBranch.getMinus());
        assertTrue(elseBranch.getMinus());

        assertEquals(1, condition.getNumber());
        assertEquals(1, elseBranch.getNumber());
        assertEquals(0, thenBranch.getNumber());
    }

    @Test
    public void ifExpressionAsChild() throws SyntaxErrorException {
        Parser parser = new Parser();

        Program program = parser.parse(Arrays.asList(
                new Token("(", TokenType.LEFT_PARENTHESIS),
                new Token("[", TokenType.LEFT_SQUARE_BRACKET),
                new Token("1", TokenType.NUMBER),
                new Token("]", TokenType.RIGHT_SQUARE_BRACKET),
                new Token("?", TokenType.THEN_SIGN),
                new Token("{", TokenType.LEFT_BRACE),
                new Token("0", TokenType.NUMBER),
                new Token("}", TokenType.RIGHT_BRACE),
                new Token(":", TokenType.ELSE_SIGN),
                new Token("{", TokenType.LEFT_BRACE),
                new Token("-", TokenType.OPERATION),
                new Token("1", TokenType.NUMBER),
                new Token("}", TokenType.RIGHT_BRACE),
                new Token("*", TokenType.OPERATION),
                new Token("1", TokenType.NUMBER),
                new Token(")", TokenType.RIGHT_PARENTHESIS)
        ));

        assertTrue(program.getExpression() instanceof BinaryExpression);
        BinaryExpression binaryExpression = (BinaryExpression) program.getExpression();

        assertTrue(binaryExpression.getRightExpression() instanceof ConstantExpression);
        assertTrue(binaryExpression.getLeftExpression() instanceof IfExpression);
    }

    @Test
    public void functionsDefinitions() throws SyntaxErrorException {
        Parser parser = new Parser();

        Program program = parser.parse(Arrays.asList(
                new Token("a", TokenType.IDENTIFIER),
                new Token("(", TokenType.LEFT_PARENTHESIS),
                new Token("b", TokenType.IDENTIFIER),
                new Token(",", TokenType.DELIMITER),
                new Token("c", TokenType.IDENTIFIER),
                new Token(")", TokenType.RIGHT_PARENTHESIS),
                new Token("=", TokenType.OPERATION),
                new Token("{", TokenType.LEFT_BRACE),
                new Token("1", TokenType.NUMBER),
                new Token("}", TokenType.RIGHT_BRACE),
                new Token("\n", TokenType.EOL),
                new Token("1", TokenType.NUMBER)
        ));

        assertEquals(1, program.getFunctionDefinitions().size());
        assertEquals("a", program.getFunctionDefinitions().get(0).getName());
        assertEquals(2, program.getFunctionDefinitions().get(0).getParameters().size());
        assertEquals("b", program.getFunctionDefinitions().get(0).getParameters().get(0));
        assertEquals("c", program.getFunctionDefinitions().get(0).getParameters().get(1));
        assertTrue(program.getFunctionDefinitions().get(0).getBody() instanceof ConstantExpression);
    }

    @Test
    public void functionCall() throws SyntaxErrorException {
        Parser parser = new Parser();

        Program program = parser.parse(Arrays.asList(
                new Token("a", TokenType.IDENTIFIER),
                new Token("(", TokenType.LEFT_PARENTHESIS),
                new Token("(", TokenType.LEFT_PARENTHESIS),
                new Token("1", TokenType.NUMBER),
                new Token("+", TokenType.OPERATION),
                new Token("2", TokenType.NUMBER),
                new Token(")", TokenType.RIGHT_PARENTHESIS),
                new Token(",", TokenType.DELIMITER),
                new Token("1", TokenType.NUMBER),
                new Token(")", TokenType.RIGHT_PARENTHESIS)
        ));

        assertTrue(program.getExpression() instanceof CallExpression);
        CallExpression call = (CallExpression) program.getExpression();

        assertEquals("a", call.getName());
        assertEquals(2, call.getArguments().size());
        assertTrue(call.getArguments().get(0) instanceof BinaryExpression);
        assertTrue(call.getArguments().get(1) instanceof ConstantExpression);
    }

    @Test
    public void identifier() throws SyntaxErrorException {
        Parser parser = new Parser();

        Program program = parser.parse(Collections.singletonList(
                new Token("a", TokenType.IDENTIFIER)
        ));

        assertTrue(program.getExpression() instanceof Identifier);
        Identifier identifier = (Identifier) program.getExpression();

        assertEquals("a", identifier.getName());
    }

    @Test(expected = SyntaxErrorException.class)
    public void syntaxErrorBecauseOfBrackets() throws SyntaxErrorException {
        new Parser().parse(Arrays.asList(
                new Token("(", TokenType.LEFT_PARENTHESIS),
                new Token(")", TokenType.RIGHT_PARENTHESIS),
                new Token(")", TokenType.RIGHT_PARENTHESIS)
        ));
    }

    @Test(expected = SyntaxErrorException.class)
    public void syntaxErrorBecauseParsing() throws SyntaxErrorException {
        Parser parser = new Parser();

        parser.parse(Arrays.asList(
                new Token("(", TokenType.LEFT_PARENTHESIS),
                new Token("1", TokenType.NUMBER),
                new Token("+", TokenType.OPERATION),
                new Token("(", TokenType.LEFT_PARENTHESIS),
                new Token("-", TokenType.OPERATION),
                new Token("1", TokenType.NUMBER),
                new Token("*", TokenType.OPERATION),
                new Token(")", TokenType.RIGHT_PARENTHESIS),
                new Token(")", TokenType.RIGHT_PARENTHESIS)
        ));
    }
}