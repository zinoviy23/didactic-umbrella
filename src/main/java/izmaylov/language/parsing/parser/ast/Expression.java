package izmaylov.language.parsing.parser.ast;

public abstract class Expression {
    private int lineNumber;

    protected Expression(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * Line number, where this expression is defined
     * @return line number
     */
    public int getLineNumber() {
        return lineNumber;
    }
}
