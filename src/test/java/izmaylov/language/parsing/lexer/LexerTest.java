package izmaylov.language.parsing.lexer;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LexerTest {
    @Test
    public void tokenizeDigits() {
        Lexer lexer = new Lexer();

        List<Token> res = lexer.tokenize("123456124");
        assertEquals(1, res.size());
        assertEquals("123456124", res.get(0).getValue());
        assertEquals(TokenType.NUMBER, res.get(0).getType());
    }

    @Test
    public void tokenizeNegativeNumber() {
        Lexer lexer = new Lexer();

        List<Token> res = lexer.tokenize("-1124512415");
        assertEquals(2, res.size());
        assertEquals("-", res.get(0).getValue());
        assertEquals(TokenType.OPERATION, res.get(0).getType());

        assertEquals("1124512415", res.get(1).getValue());
        assertEquals(TokenType.NUMBER, res.get(1).getType());
    }

    @Test
    public void tokenizeSimpleExpression() {
        Lexer lexer = new Lexer();

        List<Token> res = lexer.tokenize("12+13");
        assertEquals(3, res.size());
        assertEquals("12", res.get(0).getValue());
        assertEquals("+", res.get(1).getValue());
        assertEquals("13", res.get(2).getValue());

        assertEquals(TokenType.NUMBER, res.get(0).getType());
        assertEquals(TokenType.OPERATION, res.get(1).getType());
        assertEquals(TokenType.NUMBER, res.get(2).getType());
    }

    @Test
    public void parenthesis() {
        Lexer lexer = new Lexer();

        List<Token> res = lexer.tokenize("(12)");
        assertEquals(3, res.size());

        assertEquals("(", res.get(0).getValue());
        assertEquals("12", res.get(1).getValue());
        assertEquals(")", res.get(2).getValue());

        assertEquals(TokenType.LEFT_PARENTHESIS, res.get(0).getType());
        assertEquals(TokenType.NUMBER, res.get(1).getType());
        assertEquals(TokenType.RIGHT_PARENTHESIS, res.get(2).getType());
    }

    @Test
    public void squareBrackets() {
        Lexer lexer = new Lexer();

        List<Token> res = lexer.tokenize("[12]");
        assertEquals(3, res.size());

        assertEquals("[", res.get(0).getValue());
        assertEquals("12", res.get(1).getValue());
        assertEquals("]", res.get(2).getValue());

        assertEquals(TokenType.LEFT_SQUARE_BRACKET, res.get(0).getType());
        assertEquals(TokenType.NUMBER, res.get(1).getType());
        assertEquals(TokenType.RIGHT_SQUARE_BRACKET, res.get(2).getType());
    }

    @Test
    public void eol() {
        Lexer lexer = new Lexer();

        List<Token> res = lexer.tokenize("1\n2\n");
        assertEquals(4, res.size());

        assertEquals("1", res.get(0).getValue());
        assertEquals("\n", res.get(1).getValue());
        assertEquals("2", res.get(2).getValue());
        assertEquals("\n", res.get(3).getValue());

        assertEquals(TokenType.NUMBER, res.get(0).getType());
        assertEquals(TokenType.EOL, res.get(1).getType());
        assertEquals(TokenType.NUMBER, res.get(2).getType());
        assertEquals(TokenType.EOL, res.get(3).getType());

    }

    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedOperation() {
        new Lexer().tokenize("a b c");
    }
}