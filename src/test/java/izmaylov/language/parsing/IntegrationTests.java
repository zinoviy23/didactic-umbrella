package izmaylov.language.parsing;

import izmaylov.language.parsing.interpreter.*;
import izmaylov.language.parsing.lexer.Lexer;
import izmaylov.language.parsing.lexer.LexerErrorException;
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

    @Test
    public void fromTask4() throws SyntaxErrorException {
        execute("g(x)={(f(x)+f((x/2)))}\n" +
                "f(x)={[(x>1)]?{(f((x-1))+f((x-2)))}:{x}}\n" +
                "g(10)", 60);
    }

    @Test
    public void factorialCheck() throws SyntaxErrorException {
        execute("f(x)={[(x=0)]?{1}:{(f((x-1))*x)}}\n" +
                "f(6)", 720);
    }

    @Test
    public void fibCycle() throws SyntaxErrorException {
        execute("f(x)={f_help(x,1,0)}\n" +
                "f_help(x,a,b)={[(x=0)]?{b}:{f_help((x-1),(a+b),a)}}\n" +
                "f(12)", 144);
    }

    @Test
    public void recursiveSum() throws SyntaxErrorException {
        execute("sum(a,b)={[(b=0)]?{a}:{(sum(a,(b-1))+1)}}\n" +
                "sum(100,1000)", 1100);
    }

    @Test
    public void recursivePow() throws SyntaxErrorException {
        execute("pow(a,b)={[(b=0)]?{1}:{helper(sq(pow(a,(b/2))),a,b)}}\n" +
                "sq(a)={(a*a)}\n" +
                "helper(pw,a,b)={[((b%2)=1)]?{(pw*a)}:{pw}}\n" +
                "pow(2,11)", 2048);
    }

    @Test(expected = LexerErrorException.class)
    public void lexerError() throws SyntaxErrorException {
        execute("1 + 2 + 3 + 4 + 5", 1);
    }

    @Test(expected = SyntaxErrorException.class)
    public void syntaxError() throws SyntaxErrorException {
        execute("1+2", 3);
    }

    @Test(expected = UnknownParameterException.class)
    public void parameterNotFound() throws SyntaxErrorException {
        execute("f(x)={y}\n" +
                "f(10)", 12);
    }

    @Test(expected = UnknownFunctionException.class)
    public void functionNotFound() throws SyntaxErrorException {
        execute("g(x)={f(x)}\n" +
                "g(10)", 10);
    }

    @Test(expected = ArgumentsNumberMismatchException.class)
    public void mismatch() throws SyntaxErrorException {
        execute("g(x)={(x+1)}\n" +
                "g(10,20)", 13);
    }

    @Test(expected = RuntimeErrorException.class)
    public void runtimeError() throws SyntaxErrorException {
        execute("g(a,b)={(a/b)}\n" +
                "g(10,0)", 3);
    }
}
