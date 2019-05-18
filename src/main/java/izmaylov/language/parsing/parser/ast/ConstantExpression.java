package izmaylov.language.parsing.parser.ast;

public class ConstantExpression extends Expression {
    private final boolean minus;
    private final int number;

    public ConstantExpression(boolean minus, int number, int lineNumber) {
        super(lineNumber);
        this.minus = minus;
        this.number = number;
    }

    public boolean getMinus() {
        return minus;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return (minus ? "-" : "") + number;
    }
}
