package izmaylov.language.parsing.parser.ast;

import java.util.Collections;
import java.util.List;

public class FunctionDefinition {
    private final String name;
    private final List<String> parameters;
    private final Expression body;

    public FunctionDefinition(String name, List<String> parameters, Expression body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public List<String> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public Expression getBody() {
        return body;
    }
}
