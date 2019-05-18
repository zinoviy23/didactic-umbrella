package izmaylov.language.parsing;

import izmaylov.language.parsing.interpreter.Interpreter;
import izmaylov.language.parsing.lexer.Lexer;
import izmaylov.language.parsing.parser.Parser;
import izmaylov.language.parsing.parser.SyntaxErrorException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntegrationTests {
    private void execute(String expression, int result) throws SyntaxErrorException {
        assertEquals(expression + " != " + result, result, new Interpreter()
                .execute(new Parser()
                        .parse(new Lexer().tokenize(expression))));
    }

    @Test
    public void simpleExpression() throws SyntaxErrorException {
        execute("(2*2)", 4);
    }

    @Test
    public void complexExpression() throws SyntaxErrorException {
        execute("(-3*((1>0)*(3=3)))", -3);
    }

    @Test
    public void fromTask1() throws SyntaxErrorException {
        execute("(2+2)", 4);
    }

    @Test
    public void fromTask2() throws SyntaxErrorException {
        execute("(2+((3*4)/5))", 4);
    }

    @Test
    public void fromTask3() throws SyntaxErrorException {
        execute("[((10+20)>(20+10))]?{1}:{0}", 0);
    }

    @Test
    public void nestedIf() throws SyntaxErrorException {
        execute("[(10<20)]?{[(10<15)]?{12}:{18}}:{[(10<5)]?{3}:{8}}", 12);
    }
}
