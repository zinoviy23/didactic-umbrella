package izmaylov.language.parsing.parser.ast;

import java.util.Collections;
import java.util.List;

public class CallExpression implements Expression {
    private final String name;
    private final List<Expression> arguments;

    public CallExpression(String name, List<Expression> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public List<Expression> getArguments() {
        return Collections.unmodifiableList(arguments);
    }
}
