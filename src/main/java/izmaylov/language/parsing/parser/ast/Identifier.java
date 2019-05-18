package izmaylov.language.parsing.parser.ast;

public class Identifier extends Expression {
    private final String name;

    public Identifier(String name, int lineNumber) {
        super(lineNumber);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
