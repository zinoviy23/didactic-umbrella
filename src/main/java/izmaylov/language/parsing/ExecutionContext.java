package izmaylov.language.parsing;

import java.util.HashMap;
import java.util.Map;

public class ExecutionContext {
    private Map<String, Integer> variables = new HashMap<>();

    public int getVariable(String name) {
        throw new AssertionError();
    }
}
