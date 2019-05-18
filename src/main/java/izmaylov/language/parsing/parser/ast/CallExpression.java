package izmaylov.language.parsing.parser.ast;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CallExpression extends Expression {
    private final String name;
    private final List<Expression> arguments;

    public CallExpression(String name, List<Expression> arguments, int lineNumber) {
        super(lineNumber);
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public List<Expression> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public String toString() {
        return name + "(" + arguments
                .stream()
                .map(Objects::toString)
                .collect(Collectors.joining(",")) + ")";
    }
}
